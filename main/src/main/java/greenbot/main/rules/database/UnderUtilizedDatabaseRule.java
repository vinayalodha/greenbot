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
import greenbot.provider.predicates.RdsInstanceClassPredicate;
import greenbot.provider.service.DatabaseService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Database;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Component
public class UnderUtilizedDatabaseRule extends AbstractGreenbotRule implements InitializingBean {
    @Autowired
    private DatabaseService databaseService;

    @Value("${rules.UnderUtilizedDatabaseRule.instance_types_to_ignore}")
    private String instanceTypesToIgnore;

    private RdsInstanceClassPredicate rdsInstanceClassPredicate;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<Database> databases = databaseService.list(Arrays.asList(getTagPredicate(request)::test, rdsInstanceClassPredicate));
        List<PossibleUpgradeInfo> underUtilized = databaseService.findUnderUtilized(databases,
                request.getCloudwatchTimeframeDuration(),
                // TODO
                request.getCpuThresholdDatabase(),
                // TODO aws cloudwatch get-metric-statistics --namespace "AWS/RDS" --metric-name SwapUsage --start-time 2020-05-18T05:24:12.555Z --end-time 2020-05-19T09:25:12.555Z --period 3600 --statistics Average --unit Percent
                0d);

        List<RuleResponseItem> items = underUtilized
                .stream()
                .map(info -> ConversionUtils.toRuleResponseItem(info, buildRuleId()))
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "rds:DescribeDBInstances", "cloudwatch:GetMetricStatistics"));
    }

    public void afterPropertiesSet() {
        String[] split = StringUtils.split(instanceTypesToIgnore, ",");
        List<String> instanceTypesToIgnoreList = Arrays.asList(split);
        rdsInstanceClassPredicate = RdsInstanceClassPredicate.builder()
                .instanceTypesToIgnore(instanceTypesToIgnoreList)
                .build();
    }

}
