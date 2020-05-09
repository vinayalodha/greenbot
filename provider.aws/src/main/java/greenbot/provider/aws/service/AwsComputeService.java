/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
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

import static greenbot.provider.aws.utils.InstanceTypeUtils.isAmd;
import static greenbot.provider.aws.utils.InstanceTypeUtils.isG2G3;
import static greenbot.provider.aws.utils.InstanceTypeUtils.isG4;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.ComputeService;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.paginators.DescribeInstancesIterable;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsComputeService implements ComputeService {

	private static final Map<String, String> INSTANCE_UPGRADE_MAP = buildInstanceUpgradeMap();

	private static final String REASON1 = "\"%s\" ec2 family can be replaced with \"%s\" ec2 family";
	private static final String REASON2 = "\"%s\" ec2 family can be replaced with \"inf1\" ec2 family if you performing machine learning inference or with g4";
	private static final String REASON3 = "\"g4\" ec2 family can be replaced with \"inf1\" ec2 family if you performing machine learning inference ";

	private final RegionService regionService;
	private final ConversionService conversionService;

	@Override
	@Cacheable("awsComputeService")
	public List<Compute> list(List<Predicate<Compute>> predicates) {
		return regionService.regions()
				.parallelStream()
				.map(region -> list(predicates, region))
				.flatMap(Collection::stream)
				.collect(toList());
	}

	private List<Compute> list(List<Predicate<Compute>> predicates, Region region) {
		Ec2Client ec2Client = Ec2Client.builder().region(region).build();
		DescribeInstancesIterable describeInstancesResponses = ec2Client.describeInstancesPaginator();
		return describeInstancesResponses.reservations()
				.stream()
				.map(Reservation::instances)
				.flatMap(Collection::stream)
				.filter(instance -> {
					String name = instance.state().nameAsString().toLowerCase();
					return StringUtils.equalsAny(name, "stopped", "running");
				})
				.map(instance -> convert(instance, region))
				.filter(compute -> predicates.stream().allMatch(predicate -> predicate.test(compute)))
				.collect(toList());
	}

	@Override
	public Map<Compute, PossibleUpgradeInfo> checkUpgradePossibility(List<Compute> computes) {
		Map<Compute, PossibleUpgradeInfo> retval = new HashMap<Compute, PossibleUpgradeInfo>();
		computes.forEach(compute -> {
			Optional<PossibleUpgradeInfo> checkUpgradePossibility = checkUpgradePossibility(compute);
			checkUpgradePossibility.ifPresent(a -> retval.put(compute, a));
		});
		return retval;
	}

	@Override
	public Optional<PossibleUpgradeInfo> checkUpgradePossibility(Compute compute) {
		return INSTANCE_UPGRADE_MAP.keySet()
				.stream()
				.map(key -> {
					// TODO simplify this
					if (compute.getInstanceType().toString().startsWith(key)
							&& !isAmd(compute.getInstanceType())) {
						String reason;
						if (isG4(compute.getInstanceType())) {
							reason = REASON3;
						} else if (isG2G3(compute.getInstanceType())) {
							reason = String.format(REASON2, key);
						} else {
							reason = String.format(REASON1, key, INSTANCE_UPGRADE_MAP.get(key));
						}
						return PossibleUpgradeInfo.builder()
								.reason(reason)
								.build();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.findAny();
	}

	private Compute convert(Instance instance, Region region) {
		Compute compute = conversionService.convert(instance, Compute.class);
		compute.setRegion(region.toString());
		return compute;
	}

	private static Map<String, String> buildInstanceUpgradeMap() {
		Map<String, String> retval = new LinkedHashMap<>();
		retval.put("cc1", "c5");
		retval.put("c1", "c5");
		retval.put("c3", "c5");
		retval.put("c4", "c5");

		retval.put("m1", "t3a");
		retval.put("t1", "t3a");
		retval.put("t2", "t3a");
		retval.put("t3", "t3a");

		retval.put("m3", "m5a");
		retval.put("m4", "m5a");
		retval.put("m5", "m5a");

		retval.put("cr1", "r5a");
		retval.put("m2", "r5a");
		retval.put("r3", "r5a");
		retval.put("r4", "r5a");
		retval.put("r5", "r5a");

		retval.put("g2", "g4");
		retval.put("g3", "g4");

		retval.put("hs1", "d2");

		return retval;
	}
}
