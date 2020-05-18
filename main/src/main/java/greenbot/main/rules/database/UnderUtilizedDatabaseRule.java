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
import greenbot.rule.model.cloud.Database;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class UnderUtilizedDatabaseRule extends AbstractGreenbotRule {

    private final DatabaseService databaseService;

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        List<Database> databases = databaseService.list(Collections.emptyList());
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Check if RDS instances are under-utilized")
                .permissions(Arrays.asList("ec2:DescribeRegions", "rds:DescribeDBInstances"))
                .build();
    }

}
