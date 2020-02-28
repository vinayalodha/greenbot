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
package greenbot.main.rules.aws.ebs;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.InstanceStorage;
import lombok.AllArgsConstructor;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class DeleteOrphanInstanceStorageRule extends AbstractGreenbotRule {

	private InstanceStorageService instanceStorageService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		List<InstanceStorage> orphans = instanceStorageService.orphans(ruleRequest.getIncludedTag(),
				ruleRequest.getExcludedTag());
		if (CollectionUtils.isEmpty(orphans))
			return null;

		RuleResponseItem item = RuleResponseItem.builder()
				.resourceIds(orphans.stream().map(InstanceStorage::getId).collect(toList()))
				.confidence(AnalysisConfidence.MEDIUM)
				.message("EBS storage is not attached to EC2, are they used?")
				.ruleId(buildRuleId())
				.build();

		return RuleResponse.builder()
				.item(item)
				.build();

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
