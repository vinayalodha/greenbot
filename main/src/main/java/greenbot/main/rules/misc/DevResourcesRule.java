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
package greenbot.main.rules.misc;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.main.rules.service.TagAnalyzer;
import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import lombok.AllArgsConstructor;

/**
 * 
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class DevResourcesRule extends AbstractGreenbotRule {

	private final TagAnalyzer devTagAnalyzer;
	private final ComputeService computeService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		// TODO also check for RDS, Fargate
		TagPredicate predicate = TagPredicate.builder()
				.includedTag(ruleRequest.getIncludedTag())
				.excludedTag(ruleRequest.getExcludedTag())
				.build();

		List<Compute> computes = computeService.list(Collections.singletonList(predicate));
		List<String> filteredComputes = computes.stream()
				.filter(compute -> devTagAnalyzer.isDevTagPresent(compute.getTags().values()))
				.map(Compute::getId)
				.collect(toList());

		if (isEmpty(filteredComputes))
			return null;

		RuleResponseItem ruleResponseItem = RuleResponseItem.builder()
				.resourceIds(filteredComputes)
				.confidence(AnalysisConfidence.LOW)
				.message(
						"Usually dev/staging/test resources don't need to run for 24 hours. Consider adding mechanism to stop them for part of day/weekend when it is unused.")
				.ruleId(buildRuleId())
				.build();

		return RuleResponse.builder()
				.item(ruleResponseItem)
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Does dev/staging/test resources running 24 hours?")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"))
				.build();
	}

}
