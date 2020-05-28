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
package greenbot.provider.aws.converter;

import greenbot.provider.aws.utils.AwsTags;
import greenbot.provider.converter.StringToInstanceType;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;
import greenbot.rule.utils.TagUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ec2.model.Instance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class InstanceToComputeConverter implements Converter<Instance, Compute> {
    // aws:ec2spot:fleet-request-id
    private final Ec2TagToTagConverter ec2TagToTagConverter;

    @Override
    public Compute convert(Instance instance) {
        Map<String, Tag> tags = new HashMap<>();
        if (instance.hasTags()) {
            tags = instance.tags()
                    .stream()
                    .map(ec2TagToTagConverter::convert)
                    .collect(toMap(Tag::getKey, Function.identity()));
        }
        return Compute.builder()
                .id(instance.instanceId())
                .instanceType(new StringToInstanceType().convert(instance.instanceTypeAsString()))
                .tags(tags)
                .serviceType(tags.get(AwsTags.ELASTIC_BEANSTALK_APP_NAME) != null ? "Elastic Beanstalk/EC2" : "EC2")
                .name(TagUtils.getValue(tags.get("Name")).orElse(""))
                .build();
    }
}
