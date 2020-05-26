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
package greenbot.provider.service;

import greenbot.rule.model.cloud.Cache;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Vinay Lodha
 */
public interface CacheService {
    List<Cache> list(List<Predicate<Cache>> predicates);

    Map<Cache, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Cache> databases);
}
