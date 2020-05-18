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
import greenbot.main.rules.service.TagAnalyzer;
import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.*;
import greenbot.rule.model.cloud.Compute;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class DevResourcesRule extends AbstractGreenbotRule {

    private final TagAnalyzer devTagAnalyzer;
    private final ComputeService computeService;

    private final ConversionService conversionService;

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO also check for RDS, Fargate
        TagPredicate predicate = conversionService.convert(ruleRequest, TagPredicate.class);

        List<Compute> computes = computeService.list(Collections.singletonList(predicate::test));
        List<RuleResponseItem> items = computes.stream()
                .filter(compute -> devTagAnalyzer.isDevTagPresent(compute.getTags().values()))
                .map(compute -> RuleResponseItem.builder()
                        .resourceId(compute.getId())
                        .service("EC2")
                        .confidence(AnalysisConfidence.MEDIUM)
                        .message(
                                "Usually dev/staging/test resources don't need to run for 24 hours. Consider adding mechanism to stop them for part of day/weekend when it is unused")
                        .ruleId(buildRuleId())
                        .build())
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Does dev/staging/test resources running 24 hours?")
                .permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances"))
                .build();
    }

}
