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
package greenbot.provider.aws.service;

import greenbot.provider.aws.UpgradeMapUtils;
import greenbot.provider.service.CacheService;
import greenbot.provider.utils.OptionalUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.cloud.Cache;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.elasticache.ElastiCacheClient;
import software.amazon.awssdk.services.elasticache.paginators.DescribeCacheClustersIterable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsCacheService implements CacheService {

    private final RegionService regionService;
    private final ConversionService conversionService;

    @Override
    public List<Cache> list(List<Predicate<Cache>> predicates) {
        return regionService.regions().stream()
                .map(region -> list(predicates, region))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Cache> list(List<Predicate<Cache>> predicates, Region region) {
        ElastiCacheClient client = ElastiCacheClient.builder().region(region).build();
        DescribeCacheClustersIterable describeCacheClustersResponses = client.describeCacheClustersPaginator();
//      ListTagsForResourceRequest request = ListTagsForResourceRequest.builder().resourceName("cluster-example").build();
//      ListTagsForResourceResponse resourceResponse = client.listTagsForResource(request);
//      Input ARN string does not have 7 components
        return describeCacheClustersResponses.cacheClusters().stream()
                .map(cluster -> conversionService.convert(cluster, Cache.class))
                .filter(Objects::nonNull)
                .filter(cache -> predicates.stream().allMatch(predicate -> predicate.test(cache)))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Cache, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Cache> caches) {
        Map<Cache, List<PossibleUpgradeInfo>> retval = new HashMap<>();
        caches.forEach(cache -> {
            List<PossibleUpgradeInfo> possibleUpgradeInfos = checkUpgradePossibility(cache);
            if (!possibleUpgradeInfos.isEmpty()) {
                retval.put(cache, possibleUpgradeInfos);
            }
        });
        return retval;
    }

    private List<PossibleUpgradeInfo> checkUpgradePossibility(Cache cache) {
        String currentFamily = cache.getInstanceType().getFamily();
        Optional<String> recommendedFamilyOptional = UpgradeMapUtils.elasticCacheUpgradeMap(currentFamily);
        Optional<PossibleUpgradeInfo> optional = recommendedFamilyOptional.map(recommendedFamily -> {
            return PossibleUpgradeInfo.fromResource(cache)
                    .reason(String.format("Consider upgrading ElastiCache instance class from %s to %s", currentFamily, recommendedFamily))
                    .confidence(AnalysisConfidence.HIGH)
                    .build();
        });
        return OptionalUtils.toList(optional);
    }
}
