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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.DatabaseService;
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

	@Autowired
	private RegionService regionService;

	@Autowired
	private ConversionService conversionService;

	@Override
	public List<PossibleUpgradeInfo> checkUpgradePossibility(Database database) {
		String template = "Consider upgrading RDS engine from %s to %s. If offers better price to performance ratio";
		String message = null;
		if (equalsAnyIgnoreCase(database.getEngine(), "mariadb", "mysql")) {
			message = String.format(template, database.getEngine(), "aurora-mysql");
		} else if (equalsAnyIgnoreCase(database.getEngine(), "postgres")) {
			message = String.format(template, database.getEngine(), "aurora-postgresql");
		}
		if (message != null)
			return Collections.singletonList(PossibleUpgradeInfo.builder().reason(message).build());

		return Collections.emptyList();
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
				.map(dbInstance -> conversionService.convert(dbInstance, Database.class))
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
