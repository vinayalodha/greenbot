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

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.predicates.TagPredicate;
import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.*;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class DeleteOrphanInstanceStorageRule extends AbstractGreenbotRule {

    private final InstanceStorageService instanceStorageService;
    private final ConversionService conversionService;

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        TagPredicate predicate = conversionService.convert(ruleRequest, TagPredicate.class);
        List<RuleResponseItem> items = instanceStorageService.orphans(Collections.singletonList(predicate::test))
                .stream()
                .map(storage -> RuleResponseItem.builder()
                        .resourceId(storage.getId())
                        .service("EBS")
                        .confidence(AnalysisConfidence.MEDIUM)
                        .message("EBS storage is not attached to EC2, are they used?")
                        .ruleId(buildRuleId())
                        .build())
                .collect(toList());

        return RuleResponse.build(items);

    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Check for orphan EBS drives")
                .permissions(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeVolumes"))
                .build();
    }
}
