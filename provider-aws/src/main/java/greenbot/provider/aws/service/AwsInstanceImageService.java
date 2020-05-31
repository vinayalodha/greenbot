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

import greenbot.provider.service.InstanceImageService;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest.Builder;
import software.amazon.awssdk.services.ec2.model.DescribeImagesResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Image;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsInstanceImageService implements InstanceImageService {

    private final RegionService regionService;

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    @Cacheable("AwsInstanceImageService")
    public boolean isGreaterThanThreshold(int threshold, Tag includedTag, Tag excludedTag) {

        List<Image> images = regionService.regions()
                .stream()
                .map(region -> Ec2Client.builder().region(region).build())
                .map(client -> query(includedTag, client))
                .flatMap(Collection::stream)
                .filter(image -> {
                    if (excludedTag == null)
                        return true;
                    return image.tags().stream()
                            .noneMatch(tag -> tag.key().contentEquals(excludedTag.getKey()) && tag.value().contentEquals(excludedTag.getValue()));
                })
                .limit(threshold + 1)
                .collect(Collectors.toList());

        return images.size() > threshold;
    }

    private List<Image> query(Tag includedTag, Ec2Client client) {
        Builder describeImagesRequest = DescribeImagesRequest.builder().owners("self");

        if (includedTag != null) {
            Filter filter = Filter.builder().name("tag:" + includedTag.getKey()).values(includedTag.getValue()).build();
            describeImagesRequest.filters(filter);
        }
        DescribeImagesResponse response = client.describeImages(describeImagesRequest.build());
        return response.images();
    }

}
