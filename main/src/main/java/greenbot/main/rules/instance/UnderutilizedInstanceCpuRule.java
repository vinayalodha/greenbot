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
package greenbot.main.rules.instance;

import static greenbot.provider.aws.utils.InstanceTypeUtils.CPU_INTENSIVE_CPU_FAMILY_LIST;
import static greenbot.provider.aws.utils.InstanceTypeUtils.GENERAL_PURPOSE_FAMILY_LIST;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import greenbot.main.config.ConfigService;
import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.predicates.InstanceFamilyPredicate;
import greenbot.provider.predicates.InstanceTypePredicate;
import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.provider.service.UtilizationService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;

/**
 * @author Vinay Lodha
 */
@Component
public class UnderutilizedInstanceCpuRule extends AbstractGreenbotRule implements InitializingBean {

	private static Collection<String> GP_CPU_FAMILIES = Stream.of(
			GENERAL_PURPOSE_FAMILY_LIST, CPU_INTENSIVE_CPU_FAMILY_LIST)
			.flatMap(Collection::stream)
			.collect(toList());

	private static final String INSATNCE_CSV = join(GP_CPU_FAMILIES);

	@Value("${rules.UnderutilizedInstanceCpuRule.instancetypes_to_ignore}")
	private String instanceTypesToIgnore;

	@Autowired
	private UtilizationService utilizationService;

	@Autowired
	private ComputeService computeService;

	private InstanceFamilyPredicate instanceFamilyPredicate;
	private InstanceTypePredicate instanceTypePredicate;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		TagPredicate tagPredicate = TagPredicate.builder()
				.includedTag(ruleRequest.getIncludedTag())
				.excludedTag(ruleRequest.getExcludedTag())
				.build();

		List<Compute> computes = computeService
				.list(Arrays.asList(tagPredicate, instanceFamilyPredicate, instanceTypePredicate));
		if (CollectionUtils.isEmpty(computes))
			return null;

		Map<Compute, Double> averageCpuUtilzationMap = utilizationService
				.averageCpuUtilization(ruleRequest.getCloudwatchTimeframeDuration(), computes);

		List<RuleResponseItem> ruleResponseItems = averageCpuUtilzationMap.entrySet()
				.stream()
				.map(entry -> {
					if (entry.getValue() > ruleRequest.getUnderUtilizaedCpuPercentageThreshold()) {
						return null;
					}
					Compute compute = entry.getKey();
					return RuleResponseItem.builder()
							.resourceId(compute.getId())
							.service("EC2")
							.confidence(AnalysisConfidence.MEDIUM)
							.message(String.format(
									"CPU is underutilized, average CPU usage is %.2f. Consider using smaller instance size",
									entry.getValue()))
							.ruleId(buildRuleId())
							.build();
				})
				.filter(Objects::nonNull)
				.collect(toList());

		return RuleResponse.build(ruleResponseItems);
	}

	@Override
	public RuleInfo ruleInfo() {
		String desc = String.format(
				"Find Under-utilized machines based on average CPU usage (AWS don't capture memory utilization by default). "
						+ "Only %s instance family are analyzed. CPU threshold value can be changed using %s config param",
				INSATNCE_CSV, ConfigService.UNDER_UTILIZED_CPU_PERCENTAGE);
		return RuleInfo.builder()
				.id(buildRuleId())
				.description(desc)
				.permissions(
						Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances", "cloudwatch:GetMetricStatistics"))
				.build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		instanceFamilyPredicate = InstanceFamilyPredicate.builder()
				.allowededFamilies(GP_CPU_FAMILIES)
				.build();

		String[] split = StringUtils.split(instanceTypesToIgnore, ",");
		instanceTypePredicate = InstanceTypePredicate.builder()
				.instaceTypesToIgnore(Arrays.asList(split))
				.build();
	}

}
