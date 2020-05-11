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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithAny;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.ComputeService;
import greenbot.provider.utils.OptionalUtils;
import greenbot.rule.model.AnalysisConfidence;
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
					String name = instance.state().nameAsString();
					return equalsAnyIgnoreCase(name, "stopped", "running");
				})
				.map(instance -> convert(instance, region))
				.filter(compute -> predicates.stream().allMatch(predicate -> predicate.test(compute)))
				.collect(toList());
	}

	@Override
	public Map<Compute, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Compute> computes) {
		Map<Compute, List<PossibleUpgradeInfo>> retval = new HashMap<>();
		computes.forEach(compute -> {
			List<PossibleUpgradeInfo> checkUpgradePossibility = checkUpgradePossibility(compute);
			if (!checkUpgradePossibility.isEmpty()) {
				retval.put(compute, checkUpgradePossibility);
			}
		});
		return retval;
	}

	@Override
	public List<PossibleUpgradeInfo> checkUpgradePossibility(Compute compute) {
		Optional<PossibleUpgradeInfo> a = isFamilyCanBeUpgraded(compute);
		Optional<PossibleUpgradeInfo> b = armRecommendation(compute);
		Optional<PossibleUpgradeInfo> c = infChips(compute);
		return OptionalUtils.<PossibleUpgradeInfo>buildList(Arrays.asList(a, b, c));
	}

	private Optional<PossibleUpgradeInfo> infChips(Compute compute) {
		String family = compute.getInstanceType().getFamily();
		if (startsWithAny(family, "g3", "g2", "p3", "p2")) {
			PossibleUpgradeInfo possibleUpgradeInfo = PossibleUpgradeInfo.builder()
					.reason("Consider using inf1 instances if you performing machine learning inference")
					.confidence(AnalysisConfidence.MEDIUM)
					.build();
			return Optional.of(possibleUpgradeInfo);
		}
		return Optional.empty();
	}

	private Optional<PossibleUpgradeInfo> armRecommendation(Compute compute) {
		String family = compute.getInstanceType().getFamily();

		String message = null;
		if (startsWithAny(family, "t1", "t2", "t3")) {
			message = "Consider switching to a1 instances if your application workload support ARM";
		} else if (startsWithAny(family, "m1", "m2", "m3", "m4", "m5")) {
			message = "Consider switching to m6g instances if your application workload support ARM";
		}

		if (message == null) {
			return Optional.empty();
		}

		PossibleUpgradeInfo possibleUpgradeInfo = PossibleUpgradeInfo.builder()
				.reason(message)
				.confidence(AnalysisConfidence.LOW)
				.build();
		return Optional.of(possibleUpgradeInfo);
	}

	private Optional<PossibleUpgradeInfo> isFamilyCanBeUpgraded(Compute compute) {
		String family = compute.getInstanceType().getFamily();
		String upgradableFamily = INSTANCE_UPGRADE_MAP.get(family);
		if (upgradableFamily == null)
			return Optional.empty();

		PossibleUpgradeInfo possibleUpgradeInfo = PossibleUpgradeInfo.builder()
				.reason(String.format("Consider upgrading to newer instance family from %s to %s", family,
						upgradableFamily))
				.confidence(AnalysisConfidence.HIGH)
				.build();
		return Optional.of(possibleUpgradeInfo);
	}

	private Compute convert(Instance instance, Region region) {
		Compute compute = conversionService.convert(instance, Compute.class);
		compute.setRegion(region.toString());
		return compute;
	}

	private static Map<String, String> buildInstanceUpgradeMap() {

		Map<String, String> retval = new LinkedHashMap<>();

		retval.put("c1", "c5");
		retval.put("c3", "c5");
		retval.put("c4", "c5");
		retval.put("cc1", "c5");
		retval.put("cc2", "c5");

		retval.put("g2", "g4dn");
		retval.put("g3", "g4dn");
		retval.put("g3s", "g4dn");

		retval.put("i2", "i3");

		retval.put("m1", "m5a");
		retval.put("m3", "m5a");
		retval.put("m4", "m5a");
		retval.put("m5", "m5a");
		retval.put("m5d", "m5ad");

		retval.put("p2", "p3");

		retval.put("r3", "r5a");
		retval.put("r4", "r5a");
		retval.put("r5", "r5a");
		retval.put("r5d", "r5ad");

		retval.put("t1", "t3a");
		retval.put("t2", "t3a");
		retval.put("t3", "t3a");

		return retval;
	}
}
