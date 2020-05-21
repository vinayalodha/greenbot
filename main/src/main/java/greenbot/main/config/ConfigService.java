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
package greenbot.main.config;

import greenbot.main.converter.AnalysisRequestToRuleRequest;
import greenbot.rule.model.ConfigParam;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Update {@link AnalysisRequestToRuleRequest} also
 *
 * @author Vinay Lodha
 */
@Service
public class ConfigService implements InitializingBean {

    public static final String TOO_MANY_AMI_THRESHOLD = "too_many_ami_threshold";
    public static final String EXCLUDED_TAG = "excluded_tag";
    public static final String INCLUDED_TAG = "included_tag";
    public static final String UNDER_UTILIZED_CPU_PERCENTAGE = "under_utilized_cpu_percentage";
    public static final String UNDER_UTILIZED_SWAP_SPACE_PERCENTAGE = "under_utilized_swap_space_percentage";

    public static final String CLOUDWATCH_CONFIG_DURATION = "cloudwatch_config_duration";
    public static final String RULES_TO_IGNORE = "rules_to_ignore";

    @Value("${config.threshold.max_ami_count}")
    private int amiThreshold;

    @Value("${config.threshold.under_utilized_cpu_percentage}")
    private double underUtilizedCpuPercentageThreshold;

    @Value("${config.threshold.under_utilized_swap_space_percentage}")
    private double underUtilizedSwapSpacePercentageThreshold;

    @Value("${config.cloudwatch.timeframe}")
    private int cloudwatchTimeframeDuration;

    private List<ConfigParam> emptyConfigParams = Collections.emptyList();

    public static void main(String[] args) {
        System.out.println(TimeUnit.DAYS.toMinutes(60));
    }

    public List<ConfigParam> getDefaultConfig() {
        return emptyConfigParams;
    }

    @Override
    public void afterPropertiesSet() {
        // cloud watch can accommodate maximum of 1440 datapoints with resolution of 1 hour
        // 60mins * 24hours * 60days = 86400 mins
        val minutes = TimeUnit.DAYS.toMinutes(60);
        if (cloudwatchTimeframeDuration <= 9 || cloudwatchTimeframeDuration >= minutes) {
            throw new RuntimeException(String.format("config.cloudwatch.timeframe greater than 9 minutes and less than %s minutes", minutes));
        }

        ConfigParam excludedTag = ConfigParam.builder()
                .key(EXCLUDED_TAG)
                .value("")
                .description("Resources with this tag will not be analyzed. Format <key>=<value>")
                .build();

        ConfigParam includedTag = ConfigParam.builder()
                .key(INCLUDED_TAG)
                .value("")
                .description("Only resources with this tag will be analyzed. Format <key>=<value>")
                .build();

        ConfigParam tooManyAmiTag = ConfigParam.builder()
                .key(TOO_MANY_AMI_THRESHOLD)
                .value(String.valueOf(amiThreshold))
                .description("Threshold AMI count above which too_many_instance_images_rule rule will raise a concern")
                .build();

        ConfigParam underUtilizedCpuPercentage = ConfigParam.builder()
                .key(UNDER_UTILIZED_CPU_PERCENTAGE)
                .value(String.valueOf(underUtilizedCpuPercentageThreshold))
                .description("Average CPU utilization threshold for under-utilized machine")
                .build();

        ConfigParam underUtilizedSwapSpacePercentage = ConfigParam.builder()
                .key(UNDER_UTILIZED_SWAP_SPACE_PERCENTAGE)
                .value(String.valueOf(underUtilizedSwapSpacePercentageThreshold))
                .description("NOT USED YET : Average swap memory percentage threshold for under-utilized database")
                .build();

        ConfigParam cloudwatchTimeframeDurationConfig = ConfigParam.builder()
                .key(CLOUDWATCH_CONFIG_DURATION)
                .value(String.valueOf(cloudwatchTimeframeDuration))
                .description(
                        "Duration for which cloudwatch data to be analyzed(in minutes), should be multiple of 5 with min value of 10")
                .build();

        ConfigParam rulesToIgnore = ConfigParam.builder()
                .key(RULES_TO_IGNORE)
                .value("")
                .description(
                        "Comma separated rule ids to ignore for example too_many_instance_images_rule,delete_orphan_instance_storage_rule")
                .build();

        emptyConfigParams = Arrays.asList(excludedTag,
                includedTag,
                tooManyAmiTag,
                underUtilizedCpuPercentage,
                underUtilizedSwapSpacePercentage,
                cloudwatchTimeframeDurationConfig,
                rulesToIgnore);
    }

}
