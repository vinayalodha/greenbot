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
package greenbot.main.converter;

import greenbot.main.config.ConfigService;
import greenbot.main.model.ui.AnalysisRequest;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Vinay Lodha
 */
@Component
@AllArgsConstructor
public class AnalysisRequestToRuleRequest implements Converter<AnalysisRequest, RuleRequest>, InitializingBean {

    private final ConverterRegistry converterRegistry;
    private final ConversionService conversionService;

    @Override
    public RuleRequest convert(AnalysisRequest analysisRequest) {
        List<ConfigParam> configParams = analysisRequest.getConfigParams();

        String parameterValue = getParamValue(configParams, ConfigService.EXCLUDED_TAG);
        Tag excludedTag = conversionService.convert(parameterValue, Tag.class);

        parameterValue = getParamValue(configParams, ConfigService.INCLUDED_TAG);
        Tag includedTag = conversionService.convert(parameterValue, Tag.class);

        int amiThreshold = getIntParam(configParams, ConfigService.TOO_MANY_AMI_THRESHOLD);
        Double underUtilizedCpuPercentageThreshold = getDoubleParam(configParams,
                ConfigService.UNDER_UTILIZED_CPU_PERCENTAGE);
        Double underUtilizedSwapSpacePercentageThreshold = getDoubleParam(configParams,
                ConfigService.UNDER_UTILIZED_SWAP_SPACE_PERCENTAGE);
        int cloudwatchTimeFrameDuration = getIntParam(configParams, ConfigService.CLOUDWATCH_CONFIG_DURATION);

        parameterValue = getParamValue(configParams, ConfigService.RULES_TO_IGNORE);
        List<String> rules = Arrays.stream(StringUtils.split(parameterValue, ",")).collect(toList());
        return RuleRequest.builder()
                .includedTag(includedTag)
                .excludedTag(excludedTag)
                .amiThreshold(amiThreshold)
                .underUtilizedCpuPercentageThreshold(underUtilizedCpuPercentageThreshold)
                .swapSwapPercentage(underUtilizedSwapSpacePercentageThreshold)
                .cloudwatchTimeframeDuration(cloudwatchTimeFrameDuration)
                .rulesToIgnore(rules)
                .build();
    }

    private int getIntParam(List<ConfigParam> params, String key) {
        return Integer.parseInt(getParamValue(params, key));
    }

    private Double getDoubleParam(List<ConfigParam> params, String key) {
        return Double.parseDouble(getParamValue(params, key));
    }

    private String getParamValue(List<ConfigParam> params, String key) {
        return params
                .stream()
                .filter(cp -> cp.getKey().equals(key))
                .findAny()
                .map(ConfigParam::getValue).orElse(null);
    }

    @Override
    public void afterPropertiesSet() {
        converterRegistry.addConverter(this);
    }
}
