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
import greenbot.provider.service.ComputeService;
import greenbot.provider.service.DatabaseService;
import greenbot.rule.model.*;
import greenbot.rule.model.cloud.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Service
public class DevResourcesRule extends AbstractGreenbotRule {

    @Autowired
    private TagAnalyzer devTagAnalyzer;

    @Autowired
    private ComputeService computeService;

    @Autowired
    private DatabaseService databaseService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        // TODO also check for Fargate
        List<? extends Resource> computes = computeService.list(Collections.singletonList(getTagPredicate(request)::test));
        List<? extends Resource> databases = databaseService.list(Collections.singletonList(getTagPredicate(request)::test));
        List<Resource> resources = new ArrayList<>();
        resources.addAll(databases);
        resources.addAll(computes);
        List<RuleResponseItem> items = resources.stream()
                .filter(compute -> devTagAnalyzer.isDevTagPresent(compute.getTags().values()))
                .map(resource -> {
                    return RuleResponseItem.fromResource(resource)
                            .confidence(AnalysisConfidence.LOW)
                            .message("Usually dev/staging/test resources don't need to run for 24 hours. Consider adding lifecycle to stop them for part of day/weekend when it is unused")
                            .ruleId(buildRuleId())
                            .build();
                })
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "ec2:DescribeInstances", "rds:DescribeDBInstances"));
    }

}
