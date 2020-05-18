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

import greenbot.provider.aws.model.CloudWatchMetricStatisticsRequest;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Vinay Lodha
 */
@Service
public class CloudWatchService {

	public Optional<Double> getMetricStatistics(CloudWatchMetricStatisticsRequest cloudWatchMetricStatisticsRequest) {
		Instant now = Instant.now();

		CloudWatchClient client = CloudWatchClient.builder().region(Region.of(cloudWatchMetricStatisticsRequest.getRegion())).build();
		Dimension dimension = Dimension.builder()
				.name(cloudWatchMetricStatisticsRequest.getDimensionKey())
				.value(cloudWatchMetricStatisticsRequest.getDimensionValue())
				.build();

		GetMetricStatisticsRequest request = GetMetricStatisticsRequest.builder()
				.startTime(now.minusSeconds((cloudWatchMetricStatisticsRequest.getDuration() + 1) * 60))
				.endTime(now)
				.period(cloudWatchMetricStatisticsRequest.getDuration() * 60)
				.statistics(Statistic.AVERAGE)
				.statisticsWithStrings("Average")
				.unit(StandardUnit.PERCENT)
				.namespace(cloudWatchMetricStatisticsRequest.getNamespace())
				.metricName(cloudWatchMetricStatisticsRequest.getMetricName())
				.dimensions(dimension)
				.build();

		GetMetricStatisticsResponse response = client.getMetricStatistics(request);
		if (response.hasDatapoints() && response.datapoints().size() > 0) {
			return Optional.of(response.datapoints().get(0).average());
		}
		return Optional.empty();
	}
}