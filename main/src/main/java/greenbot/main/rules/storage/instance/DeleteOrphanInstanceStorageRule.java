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
import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Component
public class DeleteOrphanInstanceStorageRule extends AbstractGreenbotRule {

    @Autowired
    private InstanceStorageService instanceStorageService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<RuleResponseItem> items = instanceStorageService.orphans(singletonList(getTagPredicate(request)::test))
                .stream()
                .map(storage -> RuleResponseItem.fromResource(storage)
                        .confidence(AnalysisConfidence.MEDIUM)
                        .message("EBS storage is not attached to EC2, are they used?")
                        .ruleId(buildRuleId())
                        .build())
                .collect(toList());

        return RuleResponse.build(items);

    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeVolumes"));
    }
}
