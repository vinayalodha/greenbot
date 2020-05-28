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
package greenbot.rule.utils;

import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.model.cloud.Resource;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
public class ConversionUtils {
    private ConversionUtils() {
    }

    public static RuleResponse toRuleResponse(Map<? extends Resource, List<PossibleUpgradeInfo>> possibleUpgradeInfoMap, String ruleId) {
        List<RuleResponseItem> items = possibleUpgradeInfoMap.values()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(info -> ConversionUtils.toRuleResponseItems(Collections.singletonList(info), ruleId))
                .flatMap(Collection::stream)
                .collect(toList());

        return RuleResponse.build(items);
    }

    public static List<RuleResponseItem> toRuleResponseItems(List<PossibleUpgradeInfo> possibleUpgradeInfos, String ruleId) {
        return possibleUpgradeInfos
                .stream()
                .map(possibleUpgradeInfo -> RuleResponseItem.builder()
                        .resourceId(possibleUpgradeInfo.getResourceId())
                        .service(possibleUpgradeInfo.getService())
                        .confidence(possibleUpgradeInfo.getConfidence())
                        .message(possibleUpgradeInfo.getReason())
                        .ruleId(ruleId)
                        .build())
                .collect(toList());
    }

}
