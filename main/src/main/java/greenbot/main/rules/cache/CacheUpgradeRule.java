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
package greenbot.main.rules.cache;

import greenbot.provider.service.CacheService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.Cache;
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
public class CacheUpgradeRule extends greenbot.main.rules.AbstractGreenbotRule {

    @Autowired
    private CacheService cacheService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<Cache> caches = cacheService.list(Collections.emptyList());
        Map<Cache, List<PossibleUpgradeInfo>> upgrades = cacheService.checkUpgradePossibility(caches);
        List<RuleResponseItem> items = upgrades.values()
                .stream()
                .flatMap(Collection::stream)
                .map(info -> ConversionUtils.toRuleResponseItem(info, buildRuleId()))
                .collect(toList());

        return RuleResponse.build(items);
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "elasticache:DescribeCacheClusters"));
    }

}
