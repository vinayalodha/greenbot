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

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.aws.model.AutoScalingGroup;
import greenbot.provider.aws.service.AutoScalingGroupService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * @author Vinay Lodha
 */
@Service
public class AutoScalingGroupOptimizationRule extends AbstractGreenbotRule {

    @Autowired
    private AutoScalingGroupService autoScalingGroupService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<AutoScalingGroup> resources = autoScalingGroupService.list(singletonList(tagPredicate(request)::test));
        Map<AutoScalingGroup, List<PossibleUpgradeInfo>> possibleUpgradeInfos = autoScalingGroupService.checkUpgradePossibility(resources);

        return ConversionUtils.toRuleResponse(possibleUpgradeInfos, buildRuleId());
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "autoscaling:DescribeAutoScalingGroups"));
    }

}
