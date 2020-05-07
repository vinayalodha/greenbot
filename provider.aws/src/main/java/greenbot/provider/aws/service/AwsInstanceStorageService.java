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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.InstanceStorageService;
import greenbot.rule.model.cloud.InstanceStorage;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVolumesResponse;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.ec2.paginators.DescribeVolumesIterable;

/**
 * 
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsInstanceStorageService implements InstanceStorageService {

	private final RegionService regionService;
	private final ConversionService conversionService;

	@Override
	@Cacheable("AwsInstanceStorageService")
	public List<InstanceStorage> orphans(Tag includedTag, Tag excludedTag) {

		return regionService.regions()
				.parallelStream()
				.map(region -> Ec2Client.builder().region(region).build())
				.map(Ec2Client::describeVolumesPaginator)
				.flatMap(DescribeVolumesIterable::stream)
				.map(DescribeVolumesResponse::volumes)
				.flatMap(Collection::stream)
				.filter(volume -> equalsAnyIgnoreCase("available", volume.stateAsString()))
				.map(this::convert)
				.filter(compute -> {
					return includedTag == null || compute.getTags().containsValue(includedTag);
				})
				.filter(compute -> {
					return excludedTag == null || !compute.getTags().containsValue(excludedTag);
				})
				.collect(toList());
	}

	private InstanceStorage convert(Volume volume) {
		return conversionService.convert(volume, InstanceStorage.class);
	}
}
