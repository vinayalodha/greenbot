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
package greenbot.main.rules.storage.instance;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import lombok.AllArgsConstructor;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class DeleteOrphanInstanceStorageRule extends AbstractGreenbotRule {

	private final InstanceStorageService instanceStorageService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		List<RuleResponseItem> items = instanceStorageService.orphans(ruleRequest.getIncludedTag(),
				ruleRequest.getExcludedTag()).stream()
				.map(storage -> {
					return RuleResponseItem.builder()
							.resourceId(storage.getId())
							.confidence(AnalysisConfidence.MEDIUM)
							.message("EBS storage is not attached to EC2, are they used?")
							.ruleId(buildRuleId())
							.build();
				})
				.collect(toList());

		return RuleResponse.build(items);

	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Is orphan EBS drives present?")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeVolumes"))
				.build();
	}
}
