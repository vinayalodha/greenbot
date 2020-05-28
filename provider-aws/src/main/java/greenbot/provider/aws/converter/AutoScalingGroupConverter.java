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
package greenbot.provider.aws.converter;

import greenbot.rule.model.cloud.Tag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.InstancesDistribution;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author Vinay Lodha
 */
@Component
public class AutoScalingGroupConverter implements Converter<AutoScalingGroup, greenbot.provider.aws.model.AutoScalingGroup> {

    @Override
    public greenbot.provider.aws.model.AutoScalingGroup convert(AutoScalingGroup source) {

        greenbot.provider.aws.model.AutoScalingGroup.AutoScalingGroupBuilder<?, ?> builder = greenbot.provider.aws.model.AutoScalingGroup.builder()
                .id(source.autoScalingGroupName())
                .name(source.autoScalingGroupName())
                .serviceType("Auto Scaling Group")
                .minCapacity(source.minSize())
                .maxCapacity(source.maxSize())
                .desiredCapacity(source.desiredCapacity())
                .targetGroups(source.hasTargetGroupARNs() ? source.targetGroupARNs() : Collections.emptyList())
                .lbNames(source.hasLoadBalancerNames() ? source.loadBalancerNames() : Collections.emptyList());

        if (source.hasTags()) {
            Map<String, Tag> tags = source.tags().stream()
                    .map(t -> Tag.builder().key(t.key())
                            .value(t.value())
                            .build()
                    )
                    .collect(toMap(Tag::getKey, Function.identity()));
            builder.tags(tags);
        }
        builder.mixedInstancesPolicy(source.mixedInstancesPolicy() != null);

        if (source.mixedInstancesPolicy() != null) {
            InstancesDistribution instancesDistribution = source.mixedInstancesPolicy().instancesDistribution();
            builder.onDemandBaseCapacity(instancesDistribution.onDemandBaseCapacity());
            builder.onDemandPercentageAboveBaseCapacity(instancesDistribution.onDemandPercentageAboveBaseCapacity());
        }
        return builder.build();
    }
}
