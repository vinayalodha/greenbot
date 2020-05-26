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

    /**
     * Max datapoint is 1440
     *
     * @param cloudWatchMetricStatisticsRequest
     * @return
     */
    public Optional<Double> getMetricStatistics(CloudWatchMetricStatisticsRequest cloudWatchMetricStatisticsRequest) {
        Instant endTime = Instant.now();

        CloudWatchClient client = CloudWatchClient.builder().region(Region.of(cloudWatchMetricStatisticsRequest.getRegion())).build();
        Dimension dimension = Dimension.builder()
                .name(cloudWatchMetricStatisticsRequest.getDimensionKey())
                .value(cloudWatchMetricStatisticsRequest.getDimensionValue())
                .build();

        Instant startTime = endTime.minusSeconds(cloudWatchMetricStatisticsRequest.getDuration() * 60);
        GetMetricStatisticsRequest request = GetMetricStatisticsRequest.builder()
                .startTime(startTime)
                .endTime(endTime)
                .period(calculatePeriod(startTime, endTime))
                .statistics(Statistic.AVERAGE)
                .statisticsWithStrings("Average")
                .unit(StandardUnit.PERCENT)
                .namespace(cloudWatchMetricStatisticsRequest.getNamespace())
                .metricName(cloudWatchMetricStatisticsRequest.getMetricName())
                .dimensions(dimension)
                .build();

        GetMetricStatisticsResponse response = client.getMetricStatistics(request);
        if (response.hasDatapoints() && response.datapoints().size() > 0) {
            Optional<Double> sum = response.datapoints().stream().map(Datapoint::average).reduce(Double::sum);
            return Optional.of(sum.get() / response.datapoints().size());
        }
        return Optional.empty();
    }

    /**
     * https://aws.amazon.com/premiumsupport/knowledge-center/cloudwatch-getmetricstatistics-data/
     * <p>
     * The granularity, in seconds, of the returned data points. For metrics with regular resolution, a period can be as short as one minute (60 seconds) and must be a multiple of 60. For high-resolution metrics that are collected at intervals of less than one minute, the period can be 1, 5, 10, 30, 60, or any multiple of 60. High-resolution metrics are those metrics stored by a PutMetricData call that includes a StorageResolution of 1 second.
     * <p>
     * If the StartTime parameter specifies a time stamp that is greater than 3 hours ago, you must specify the period as follows or no data points in that time range is returned:
     * <p>
     * Start time between 3 hours and 15 days ago - Use a multiple of 60 seconds (1 minute).
     * <p>
     * Start time between 15 and 63 days ago - Use a multiple of 300 seconds (5 minutes).
     * <p>
     * Start time greater than 63 days ago - Use a multiple of 3600 seconds (1 hour).
     * <p>
     * Type: Integer
     * <p>
     * Valid Range: Minimum value of 1.
     * <p>
     * Required: Yes
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private int calculatePeriod(Instant startTime, Instant endTime) {
        long durationInSec = endTime.getEpochSecond() - startTime.getEpochSecond();
        // max datapoint 1440 and resolution of each datapoint is 5 mins aka 300 sec
        if ((1439 * 300) >= durationInSec) {
            // sec
            return 300;
        } else if ((1439 * 3600) >= durationInSec) {
            return 3600;
        }
        throw new RuntimeException("cloudwatch window is too big to fit in 1440 datapoints of 1 hour");
    }

}