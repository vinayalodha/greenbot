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

import java.util.Arrays;

import org.springframework.stereotype.Component;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.InstanceImageService;
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
public class TooManyInstanceImagesRule extends AbstractGreenbotRule {

	private final InstanceImageService instanceImageService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		boolean result = instanceImageService.isGreaterThanThreshold(ruleRequest.getAmiThreshold(),
				ruleRequest.getIncludedTag(),
				ruleRequest.getExcludedTag());
		if (!result)
			return null;

		RuleResponseItem item = RuleResponseItem.builder()
				.confidence(AnalysisConfidence.MEDIUM)
				.service("AMI")
				.message("AMI count exceeds " + ruleRequest.getAmiThreshold()
						+ " (refer too_many_ami_threshold config param)")
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
				.description("Too may AMI, do cleanup policy exits?")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeImages"))
				.build();
	}
}
