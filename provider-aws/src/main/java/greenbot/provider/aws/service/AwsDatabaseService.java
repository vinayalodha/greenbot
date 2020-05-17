/*
 * Copyright 2019-2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenbot.provider.aws.service;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.DatabaseService;
import greenbot.provider.utils.OptionalUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.cloud.Database;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;
import software.amazon.awssdk.services.rds.paginators.DescribeDBInstancesIterable;

/**
 * 
 * Engine Types :
 * https://docs.aws.amazon.com/AmazonRDS/latest/APIReference/API_CreateDBInstance.html
 * 
 * @author Vinay Lodha
 */
@Service
public class AwsDatabaseService implements DatabaseService {

	private static final Map<String, String> INSTANCE_UPGRADE_MAP = buildMap();

	private static Map<String, String> buildMap() {
		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("db.t2", "db.t3");
		retVal.put("db.m3", "db.m5");
		retVal.put("db.m4", "db.m5");
		retVal.put("db.r3", "db.r5");
		retVal.put("db.r4", "db.r5");
		return retVal;
	}

	@Autowired
	private RegionService regionService;

	@Autowired
	private ConversionService conversionService;

	private List<PossibleUpgradeInfo> checkUpgradePossibility(Database database) {
		Optional<PossibleUpgradeInfo> a = migrationToAurora(database);
		Optional<PossibleUpgradeInfo> b = olderGenFamily(database);
		return OptionalUtils.<PossibleUpgradeInfo>buildList(Arrays.asList(a, b));
	}

	@Override
	public Map<Database, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Database> databases) {
		Map<Database, List<PossibleUpgradeInfo>> retval = new HashMap<>();
		databases.forEach(obj -> {
			List<PossibleUpgradeInfo> result = checkUpgradePossibility(obj);
			if (CollectionUtils.isNotEmpty(result)) {
				retval.put(obj, result);
			}
		});
		return retval;
	}

	@Override
	public List<Database> list(List<Predicate<Database>> predicates) {
		return regionService.regions()
				.stream()
				.map(this::list)
				.flatMap(Collection::stream)
				.collect(toList());
	}

	public List<Database> list(Region region) {
		RdsClient client = RdsClient.builder().region(region).build();
		DescribeDbInstancesRequest request = DescribeDbInstancesRequest.builder().build();

		DescribeDBInstancesIterable responseIterable = client.describeDBInstancesPaginator(request);
		return responseIterable.dbInstances()
				.stream()
				.filter(instance -> equalsAnyIgnoreCase(instance.dbInstanceStatus(), "available"))
				.map(dbInstance -> conversionService.convert(dbInstance, Database.class))
				.filter(Objects::nonNull)
				.collect(toList());
	}

	private Optional<PossibleUpgradeInfo> migrationToAurora(Database database) {
		String template = "Consider upgrading RDS engine from %s to %s. If offers better price to performance ratio";
		String message = null;
		if (equalsAnyIgnoreCase(database.getEngine(), "mariadb", "mysql")) {
			message = format(template, database.getEngine(), "aurora-mysql");
		} else if (equalsAnyIgnoreCase(database.getEngine(), "postgres")) {
			message = format(template, database.getEngine(), "aurora-postgresql");
		}
		if (message != null) {
			return of(PossibleUpgradeInfo.builder()
					.resourceId(database.getId())
					.service("RDS")
					.reason(message)
					.confidence(AnalysisConfidence.LOW)
					.build());
		}
		return empty();
	}

	private Optional<PossibleUpgradeInfo> olderGenFamily(Database database) {
		String instanceClass = database.getInstanceClass();
		if (isEmpty(instanceClass)) {
			return Optional.empty();
		}
		String key = StringUtils.substringBeforeLast(instanceClass, ".");
		String value = INSTANCE_UPGRADE_MAP.get(key);
		if (isEmpty(value)) {
			return empty();
		}

		String message = format("Consider upgrading instance class from %s to %s", key, value);
		return of(PossibleUpgradeInfo.builder()
				.resourceId(database.getId())
				.service("RDS")
				.reason(message)
				.confidence(AnalysisConfidence.HIGH)
				.build());
	}
}
