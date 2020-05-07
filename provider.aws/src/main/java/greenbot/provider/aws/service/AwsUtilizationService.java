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
package greenbot.provider.aws.service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import greenbot.provider.service.UtilizationService;
import greenbot.rule.model.cloud.Compute;
import lombok.SneakyThrows;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsRequest;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsResponse;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;
import software.amazon.awssdk.services.cloudwatch.model.Statistic;

/**
 * @author Vinay Lodha
 */
@Service
public class AwsUtilizationService implements UtilizationService {

	@Override
	@Cacheable("AwsUtilizationService")
	public Map<Compute, Double> averageCpuUtilization(final int timeframe, Collection<Compute> computes) {
		Map<Compute, Double> retval = new HashMap<>();
		computes.forEach(c -> {
			Double average = averageCpuUtilization(c, timeframe);
			if (average != null)
				retval.put(c, average);
		});
		return retval;
	}

	@SneakyThrows
	private Double averageCpuUtilization(Compute c, final int timeframe) {
		Instant now = Instant.now();

		CloudWatchClient client = CloudWatchClient.builder().region(Region.of(c.getRegion())).build();
		Dimension dimension = Dimension.builder().name("InstanceId").value(c.getId()).build();

		GetMetricStatisticsRequest request = GetMetricStatisticsRequest.builder()
				.startTime(now.minusSeconds((10 + 1) * 60))
				.endTime(now)
				.period(10 * 60)
				.statistics(Statistic.AVERAGE, Statistic.MAXIMUM)
				.statisticsWithStrings("Average")
				.unit(StandardUnit.PERCENT)
				.namespace("AWS/EC2")
				.metricName("CPUUtilization")
				.dimensions(dimension)
				.build();

		GetMetricStatisticsResponse response = client.getMetricStatistics(request);
		if (response.hasDatapoints() && response.datapoints().size() > 0) {
			return response.datapoints().get(0).average();
		}
		return null;
	}

}
