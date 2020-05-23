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
package greenbot.main.rules.instance;

import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * https://aws.amazon.com/ec2/previous-generation/
 *
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class OlderGenerationInstanceRule extends greenbot.main.rules.AbstractGreenbotRule {

    private final ComputeService computeService;
    private final ConversionService conversionService;

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        TagPredicate predicate = conversionService.convert(ruleRequest, TagPredicate.class);

        List<Compute> computes = computeService.list(Collections.singletonList(predicate::test));
        Map<Compute, List<PossibleUpgradeInfo>> possibleUpgradeInfos = computeService.checkUpgradePossibility(computes);

        List<RuleResponseItem> items = possibleUpgradeInfos.values()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(info -> ConversionUtils.toRuleResponseItem(info, buildRuleId()))
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"));
    }

}
