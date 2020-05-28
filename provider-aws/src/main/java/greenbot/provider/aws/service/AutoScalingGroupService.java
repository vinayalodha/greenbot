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

import greenbot.provider.aws.model.AutoScalingGroup;
import greenbot.provider.utils.OptionalUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.paginators.DescribeAutoScalingGroupsIterable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AutoScalingGroupService {

    private final RegionService regionService;
    private final ConversionService conversionService;

    public List<AutoScalingGroup> list(List<Predicate<AutoScalingGroup>> predicates) {
        return regionService.regions().stream()
                .map(region -> list(predicates, region))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<AutoScalingGroup> list(List<Predicate<AutoScalingGroup>> predicates, Region region) {
        AutoScalingClient client = AutoScalingClient.builder().region(region).build();
        DescribeAutoScalingGroupsIterable responses = client.describeAutoScalingGroupsPaginator();
        return responses.autoScalingGroups().stream()
                .map(awsAutoScalingGroup -> conversionService.convert(awsAutoScalingGroup, AutoScalingGroup.class))
                .filter(Objects::nonNull)
                .filter(asg -> predicates.stream().allMatch(predicate -> predicate.test(asg)))
                .collect(toList());
    }

    public Map<AutoScalingGroup, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<AutoScalingGroup> autoScalingGroups) {
        Map<AutoScalingGroup, List<PossibleUpgradeInfo>> retval = new HashMap<>();
        autoScalingGroups.forEach(autoScalingGroup -> {
            List<PossibleUpgradeInfo> checkUpgradePossibility = checkUpgradePossibility(autoScalingGroup);
            if (!checkUpgradePossibility.isEmpty()) {
                retval.put(autoScalingGroup, checkUpgradePossibility);
            }
        });
        return retval;

    }

    private List<PossibleUpgradeInfo> checkUpgradePossibility(AutoScalingGroup autoScalingGroup) {
        Optional<PossibleUpgradeInfo> a = checkUselessElb(autoScalingGroup);
        Optional<PossibleUpgradeInfo> b = checkIfSpotIsUsedOrNot(autoScalingGroup);
        Optional<PossibleUpgradeInfo> c = considerMigrationToSpotFleet(autoScalingGroup);

        return OptionalUtils.toList(Arrays.asList(a, b, c));
    }

    private Optional<PossibleUpgradeInfo> considerMigrationToSpotFleet(AutoScalingGroup autoScalingGroup) {
        return Optional.ofNullable(PossibleUpgradeInfo.fromResource(autoScalingGroup)
                .confidence(AnalysisConfidence.LOW)
                .reason("Consider migrating to Spot Fleet as it can dynamically allocate spot and on-demand instances based on spot availability without need to configuring ASG MixedInstancesPolicy")
                .build());
    }

    private Optional<PossibleUpgradeInfo> checkIfSpotIsUsedOrNot(AutoScalingGroup autoScalingGroup) {
        if (!autoScalingGroup.isMixedInstancesPolicy() ||
                autoScalingGroup.getMaxCapacity() <= autoScalingGroup.getOnDemandBaseCapacity() ||
                Integer.valueOf(100).equals(autoScalingGroup.getOnDemandPercentageAboveBaseCapacity())) {
            return Optional.ofNullable(PossibleUpgradeInfo.fromResource(autoScalingGroup)
                    .confidence(AnalysisConfidence.MEDIUM)
                    .reason("Consider using spot instances in ASG MixedInstancesPolicy, it can significantly reduce cost associated with on-demand instances")
                    .build());
        }
        return Optional.empty();
    }

    private Optional<PossibleUpgradeInfo> checkUselessElb(AutoScalingGroup asg) {
        if (CollectionUtils.isEmpty(asg.getLbNames()) && CollectionUtils.isEmpty(asg.getTargetGroups()))
            return Optional.empty();

        String message = null;
        Integer ZERO = 0;
        if (ZERO.equals(asg.getMinCapacity()) && ZERO.equals(asg.getMaxCapacity()) && ZERO.equals(asg.getDesiredCapacity()))
            message = "ASG have min, max, and desired capacity as 0 and attached to ELB, " +
                    "Is Load balancer needed? If not it can be deleted to save cost";

        Integer ONE = 1;

        if (ONE.equals(asg.getMaxCapacity()))
            message = "ASG have max capacity as 1 and attached to ELB, " +
                    "If Autoscaling is not needed then you may directly attach IP to Route 53 to save cost associated with ELB";

        if (message == null)
            return Optional.empty();

        return Optional.ofNullable(PossibleUpgradeInfo.fromResource(asg)
                .confidence(AnalysisConfidence.HIGH)
                .reason(message)
                .build());
    }
}
