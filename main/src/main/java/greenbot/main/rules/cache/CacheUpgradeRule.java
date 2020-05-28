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

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.provider.service.CacheService;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.cloud.Cache;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * @author Vinay Lodha
 */
@Component
public class CacheUpgradeRule extends AbstractGreenbotRule {

    @Autowired
    private CacheService cacheService;

    @Override
    public RuleResponse doWork(RuleRequest request) {
        List<Cache> caches = cacheService.list(emptyList());
        Map<Cache, List<PossibleUpgradeInfo>> upgrades = cacheService.checkUpgradePossibility(caches);
        return ConversionUtils.toRuleResponse(upgrades, buildRuleId());
    }

    @Override
    public RuleInfo ruleInfo() {
        return buildRuleInfo(Arrays.asList("ec2:DescribeRegions", "elasticache:DescribeCacheClusters"));
    }
}
