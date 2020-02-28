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
package greenbot.main.converter;

import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.stereotype.Component;

import greenbot.main.config.ConfigParamUtils;
import greenbot.main.model.ui.AnalysisRequest;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AnalysisRequestToRuleRequest implements Converter<AnalysisRequest, RuleRequest>, InitializingBean {

	private final ConverterRegistry converterRegistry;
	private final ConversionService conversionService;

	@Override
	public RuleRequest convert(AnalysisRequest source) {
		Tag excludedTag = source.getConfigParams()
				.stream()
				.filter(cp -> cp.getKey().equals(ConfigParamUtils.EXCLUDED_TAG))
				.map(cp -> conversionService.convert(cp.getValue(), Tag.class))
				.filter(Objects::nonNull)
				.findAny()
				.orElseGet(() -> null);

		Tag includedTag = source.getConfigParams()
				.stream()
				.filter(cp -> cp.getKey().equals(ConfigParamUtils.INCLUDED_TAG))
				.map(cp -> conversionService.convert(cp.getValue(), Tag.class))
				.filter(Objects::nonNull)
				.findAny()
				.orElseGet(() -> null);

		Integer amiThreshold = source.getConfigParams()
				.stream()
				.filter(cp -> cp.getKey().equals(ConfigParamUtils.TOO_MANY_AMI_THRESHOLD))
				.map(cp -> Integer.valueOf(cp.getValue()))
				.findAny()
				.get();

		return RuleRequest.builder()
				.includedTag(includedTag)
				.excludedTag(excludedTag)
				.amiThreshold(amiThreshold)
				.build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		converterRegistry.addConverter(this);
	}
}
