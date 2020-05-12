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

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import lombok.AllArgsConstructor;

/**
 * https://aws.amazon.com/ec2/previous-generation/
 * 
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class OlderGenerationInstanceRule extends greenbot.main.rules.AbstractGreenbotRule {

	private final ComputeService computeService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		TagPredicate predicate = TagPredicate.builder()
				.includedTag(ruleRequest.getIncludedTag())
				.excludedTag(ruleRequest.getExcludedTag())
				.build();
		List<Compute> computes = computeService.list(Collections.singletonList(predicate));
		Map<Compute, List<PossibleUpgradeInfo>> possibleUpgradeInfos = computeService.checkUpgradePossibility(computes);

		List<RuleResponseItem> ruleResponseItems = possibleUpgradeInfos
				.entrySet()
				.stream()
				.map(entry -> {
					return entry.getValue().stream()
							.map(info -> {
								return RuleResponseItem.builder()
										.resourceId(entry.getKey().getId())
										.service("EC2")
										.confidence(info.getConfidence())
										.message(info.getReason())
										.ruleId(buildRuleId())
										.build();
							})
							.collect(toList());

				})
				.flatMap(Collection::stream)
				.collect(toList());

		return RuleResponse.builder()
				.items(ruleResponseItems)
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Check if Compute instances can be optimized")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"))
				.build();
	}

}
