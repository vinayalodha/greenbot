/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.ComputeService;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.InstanceUpgradeInfo;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
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

	private RegionService regionService;
	private ConversionService conversionService;

	@Override
	@Cacheable("awsComputeService")
	public List<Compute> list(Tag includedTag, Tag excludedTag) {
		return regionService.regions()
				.parallelStream()
				.map(region -> Ec2Client.builder().region(region).build())
				.map(Ec2Client::describeInstancesPaginator)
				.flatMap(DescribeInstancesIterable::stream)
				.map(DescribeInstancesResponse::reservations)
				.flatMap(Collection::stream)
				.map(Reservation::instances)
				.flatMap(Collection::stream)
				.filter(instance -> {
					String name = instance.state().nameAsString();
					if (name.equalsIgnoreCase("stopped") || name.equalsIgnoreCase("running")) {
						return true;
					}
					return false;
				})
				.map(this::convert)
				.filter(compute -> {
					return includedTag == null || compute.getTags().contains(includedTag);
				})
				.filter(compute -> {
					return excludedTag == null || !compute.getTags().contains(excludedTag);
				})
				.collect(Collectors.toList());
	}

	@Override
	public InstanceUpgradeInfo checkUpgradePossibility(Compute compute) {
		Optional<InstanceUpgradeInfo> obj = INSTANCE_UPGRADE_MAP.keySet()
				.stream()
				.map(key -> {
					if (compute.getInstanceType().startsWith(key) && !isAmd(compute.getInstanceType())) {
						String reason;
						if (isG4(compute.getInstanceType())) {
							reason = REASON3;
						} else if (isG2G3(compute.getInstanceType())) {
							reason = String.format(REASON2, key);
						} else {
							reason = String.format(REASON1, key, INSTANCE_UPGRADE_MAP.get(key));
						}
						return InstanceUpgradeInfo.builder()
								.currentFamily(key)
								.compute(compute)
								.reason(reason)
								.newFamily(INSTANCE_UPGRADE_MAP.get(key))
								.build();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.findAny();
		return obj.orElse(null);
	}

	private boolean isG2G3(String instanceType) {
		return instanceType.startsWith("g2") || instanceType.startsWith("g3");
	}

	private boolean isG4(String instanceType) {
		return instanceType.startsWith("g4");
	}

	private boolean isAmd(String instanceType) {
		String temp = instanceType.split("\\.")[0];
		return temp.length() > 2 && temp.substring(2).contains("a");
	}

	private Compute convert(Instance instance) {
		return conversionService.convert(instance, Compute.class);
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
