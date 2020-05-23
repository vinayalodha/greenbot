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

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.DatabaseService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Database;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Component
public class DatabaseUpgradeRule extends AbstractGreenbotRule {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<Database> databases = databaseService.list(Collections.emptyList());
        Map<Database, List<PossibleUpgradeInfo>> upgrades = databaseService.checkUpgradePossibility(databases);

        List<RuleResponseItem> items = upgrades.values()
                .stream()
                .flatMap(Collection::stream)
                .map(info -> ConversionUtils.toRuleResponseItem(info, buildRuleId()))
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Check if RDS instances can be optimized")
                .permissions(Arrays.asList("ec2:DescribeRegions", "rds:DescribeDBInstances"))
                .build();
    }

}
