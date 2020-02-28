/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.provider.service.ComputeService;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.paginators.DescribeInstancesIterable;

@Service
@AllArgsConstructor
public class AwsComputeService implements ComputeService {

	private RegionService regionService;
	private ConversionService conversionService;

	@Override
	@Cacheable("awsComputeService")
	public List<Compute> list(Tag includedTag, Tag excludedTag) {
		return regionService.regions()
				.parallelStream()
				.map(region -> Ec2Client.builder().region(region).build())
				.map(Ec2Client::describeInstancesPaginator)
				.flatMap(DescribeInstancesIterable::stream)
				.map(DescribeInstancesResponse::reservations)
				.flatMap(Collection::stream)
				.map(Reservation::instances)
				.flatMap(Collection::stream)
				.filter(instance -> {
					String name = instance.state().nameAsString();
					if (name.equalsIgnoreCase("stopped") || name.equalsIgnoreCase("running")) {
						return true;
					}
					return false;
				})
				.map(this::convert)
				.filter(compute -> {
					return includedTag == null || compute.getTags().contains(includedTag);
				})
				.filter(compute -> {
					return excludedTag == null || !compute.getTags().contains(excludedTag);
				})
				.collect(Collectors.toList());
	}

	private Compute convert(Instance instance) {
		return conversionService.convert(instance, Compute.class);
	}
}
