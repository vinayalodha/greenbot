/*
 * Copyright 2019-2020 Vinay Lodha (https://github.com/vinay-lodha)
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
package greenbot.main.rules.database;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.DatabaseService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Database;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;

/**
 * @author Vinay Lodha
 */
@Component
public class DatabseUpgradeRule extends AbstractGreenbotRule {

	@Autowired
	private DatabaseService databaseService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		List<Database> databases = databaseService.list(Collections.emptyList());
		Map<Database, List<PossibleUpgradeInfo>> upgrades = databaseService.checkUpgradePossibility(databases);

		List<RuleResponseItem> items = upgrades.entrySet()
				.stream()
				.map(entry -> {
					return entry.getValue().stream()
							.map(info -> {
								return RuleResponseItem.builder()
										.resourceId(entry.getKey().getId())
										.confidence(AnalysisConfidence.LOW)
										.message(info.getReason())
										.ruleId(buildRuleId())
										.build();
							})
							.collect(toList());
				})
				.flatMap(Collection::stream)
				.collect(toList());

		return RuleResponse.build(items);
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Check if RDS instances can be optimized")
				.permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"))
				.build();
	}

}
