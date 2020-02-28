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
package greenbot.main.rules.aws.ec2;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import greenbot.provider.service.ComputeService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.InstanceUpgradeInfo;
import lombok.AllArgsConstructor;

/**
 * https://aws.amazon.com/ec2/previous-generation/
 * 
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class OlderEc2GenerationRule extends greenbot.main.rules.AbstractGreenbotRule {

	private static final String RULE_DESC = "\"%s\" ec2 family can be replaced with \"%s\" ec2 family";

	private ComputeService computeService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		List<Compute> computes = computeService.list(ruleRequest.getIncludedTag(), ruleRequest.getExcludedTag());
		if (CollectionUtils.isEmpty(computes))
			return null;

		Map<String, List<InstanceUpgradeInfo>> upgradeMap = computes.stream()
				.map(c -> computeService.checkUpgradePossibility(c))
				.filter(Objects::nonNull)
				.collect(groupingBy(InstanceUpgradeInfo::getCurrentFamily));

		List<RuleResponseItem> ruleResponseItems = upgradeMap.keySet()
				.stream()
				.map(currentInstanceFamily -> {
					List<InstanceUpgradeInfo> upgrades = upgradeMap.get(currentInstanceFamily);
					List<String> ids = upgrades.stream()
							.map(instanceUpgradeInfo -> instanceUpgradeInfo.getCompute().getId())
							.collect(toList());
					return RuleResponseItem.builder()
							.resourceIds(ids)
							.confidence(AnalysisConfidence.HIGH)
							.message(
									String.format(RULE_DESC, currentInstanceFamily, upgrades.get(0).getNewFamily()))
							.ruleId(buildRuleId())
							.build();
				})
				.collect(toList());

		return RuleResponse.builder()
				.items(ruleResponseItems)
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Are older generation of EC2 instances being used?")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"))
				.build();
	}

}
