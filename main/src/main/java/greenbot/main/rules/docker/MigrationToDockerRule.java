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
package greenbot.main.rules.docker;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.ComputeService;
import greenbot.provider.service.DockerService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vinay Lodha
 */
@Service
public class MigrationToDockerRule extends AbstractGreenbotRule {

    @Autowired
    private DockerService dockerService;

    @Autowired
    private ComputeService computeService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<Compute> computes = computeService.list(Collections.singletonList(getTagPredicate(request)::test));
        Map<Compute, List<PossibleUpgradeInfo>> upgradeMap = dockerService.checkUpgradePossibility(computes);

        List<RuleResponseItem> items = upgradeMap.values().stream()
                .flatMap(Collection::stream)
                .map(obj -> ConversionUtils.toRuleResponseItem(obj, buildRuleId()))
                .collect(Collectors.toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"));
    }

}
