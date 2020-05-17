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

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;

/**
 * 
 * @author Vinay Lodha
 */
@Service
@Profile("default")
public class ProdRegionService implements RegionService {

	@Override
	@Cacheable("ProdRegionService")
	public List<Region> regions() {
		Ec2Client client = Ec2Client.builder().build();

		DescribeRegionsRequest request = DescribeRegionsRequest.builder().allRegions(true).build();
		DescribeRegionsResponse response = client.describeRegions(request);
		return response.regions()
				.stream()
				.filter(re -> !"not-opted-in".equalsIgnoreCase(re.optInStatus()))
				.map(re -> Region.of(re.regionName()))
				.collect(toList());
	}
}
