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
package greenbot.main.rules.aws.db;

import java.util.Arrays;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;

public class UnderUtilizedRdsRule extends AbstractGreenbotRule {

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		return RuleResponse.builder().infoMessage("Info Message 1").errorMessage("Error Message 1")
				.warningMessage("Warning Message 1")
				.item(RuleResponseItem.builder().resourceId("resourceId").approxCostSaving("30")
						.message("Use T3 instead of T2").confidence(AnalysisConfidence.HIGH).build())
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Is RDS instances are under-utilized")
				.permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
				.build();
	}

}
