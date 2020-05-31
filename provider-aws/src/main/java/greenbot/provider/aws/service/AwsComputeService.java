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
import greenbot.provider.aws.model.CloudWatchMetricStatisticsRequest;
import greenbot.provider.aws.utils.AwsTags;
import greenbot.provider.service.ComputeService;
import greenbot.provider.utils.OptionalUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.paginators.DescribeInstancesIterable;

import java.util.*;
import java.util.function.Predicate;

import static greenbot.provider.aws.UpgradeMapUtils.armInstanceUpgradeMap;
import static greenbot.provider.aws.UpgradeMapUtils.inf1InstanceUpgradeMap;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsComputeService implements ComputeService {

    private final RegionService regionService;
    private final ConversionService conversionService;
    private final CloudWatchService cloudWatchService;

    @Override
    public List<PossibleUpgradeInfo> findUnderUtilized(List<Compute> computes, int duration, double threshold) {
        return computes.stream()
                .map(compute -> findUnderUtilized(compute, threshold, duration))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private PossibleUpgradeInfo findUnderUtilized(Compute compute, double threshold, int duration) {
        CloudWatchMetricStatisticsRequest request = CloudWatchMetricStatisticsRequest.builder()
                .dimensionKey("InstanceId")
                .dimensionValue(compute.getId())
                .region(compute.getRegion())
                .duration(duration)
                .metricName("CPUUtilization")
                .namespace("AWS/EC2")
                .build();

        Optional<Double> averageValue = cloudWatchService.getMetricStatistics(request);
        return averageValue
                .filter(value -> value < threshold)
                .map(value ->
                        PossibleUpgradeInfo.fromResource(compute)
                                .confidence(AnalysisConfidence.MEDIUM)
                                .reason(String.format("CPU is underutilized, average CPU usage is %.2f. Consider using smaller instance size", averageValue.get()))
                                .build())
                .orElse(null);
    }

    @Override
    @Cacheable("awsComputeService")
    public List<Compute> list(List<Predicate<Compute>> predicates) {
        return regionService.regions()
                .parallelStream()
                .map(region -> list(predicates, region))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<Compute> list(List<Predicate<Compute>> predicates, Region region) {
        Ec2Client ec2Client = Ec2Client.builder().region(region).build();
        DescribeInstancesIterable describeInstancesResponses = ec2Client.describeInstancesPaginator();
        return describeInstancesResponses.reservations()
                .stream()
                .map(Reservation::instances)
                .flatMap(Collection::stream)
                .filter(instance -> {
                    String name = instance.state().nameAsString();
                    return equalsAnyIgnoreCase(name, "stopped", "running");
                })
                .map(instance -> convert(instance, region))
                .filter(Objects::nonNull)
                .filter(compute -> predicates.stream().allMatch(predicate -> predicate.test(compute)))
                .collect(toList());
    }

    @Override
    public Map<Compute, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Compute> computes) {
        Map<Compute, List<PossibleUpgradeInfo>> retval = new HashMap<>();
        computes.forEach(compute -> {
            List<PossibleUpgradeInfo> checkUpgradePossibility = checkUpgradePossibility(compute);
            if (!checkUpgradePossibility.isEmpty()) {
                retval.put(compute, checkUpgradePossibility);
            }
        });
        return retval;
    }

    private List<PossibleUpgradeInfo> checkUpgradePossibility(Compute compute) {
        Optional<PossibleUpgradeInfo> familyCanBeUpgraded = isFamilyCanBeUpgraded(compute);
        Optional<PossibleUpgradeInfo> armRecommendation = armRecommendation(compute);
        Optional<PossibleUpgradeInfo> infChips = infChips(compute);

        return OptionalUtils.toList(Arrays.asList(familyCanBeUpgraded, armRecommendation, infChips));
    }

    private Optional<PossibleUpgradeInfo> infChips(Compute compute) {
        String currentFamily = compute.getInstanceType().getFamily();
        return inf1InstanceUpgradeMap(currentFamily)
                .map(recommendedFamily -> PossibleUpgradeInfo.fromResource(compute)
                        .reason("Consider using inf1 instances if you performing machine learning inference")
                        .confidence(AnalysisConfidence.MEDIUM)
                        .build()
                );
    }

    private Optional<PossibleUpgradeInfo> armRecommendation(Compute compute) {
        String currentFamily = compute.getInstanceType().getFamily();
        return armInstanceUpgradeMap(currentFamily)
                .map(recommendedFamily -> PossibleUpgradeInfo.fromResource(compute)
                        .reason(String.format("Consider switching to %s instances if your application workload support ARM", recommendedFamily))
                        .confidence(AnalysisConfidence.LOW)
                        .build()
                );
    }

    private Optional<PossibleUpgradeInfo> isFamilyCanBeUpgraded(Compute compute) {
        String currentFamily = compute.getInstanceType().getFamily();
        AnalysisConfidence confidence;
        if (compute.getTags().get(AwsTags.FLEET_ID) != null || (compute.getTags().get(AwsTags.ASG_NAME) != null && compute.isSpot()))
            confidence = AnalysisConfidence.LOW;
        else
            confidence = AnalysisConfidence.HIGH;

        return UpgradeMapUtils.instanceUpgradeMap(currentFamily)
                .map(recommendedFamily ->
                        PossibleUpgradeInfo.fromResource(compute)
                                .reason(String.format("Consider upgrading to newer instance family from %s to %s", currentFamily, recommendedFamily))
                                .confidence(confidence)
                                .build()
                );
    }

    private Compute convert(Instance instance, Region region) {
        Compute compute = conversionService.convert(instance, Compute.class);
        if (compute == null)
            return null;
        compute.setRegion(region.toString());
        return compute;
    }
}
