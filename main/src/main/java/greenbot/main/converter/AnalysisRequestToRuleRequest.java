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
import greenbot.rule.model.Tag;
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

		return RuleRequest.builder()
				.includedTag(includedTag)
				.excludedTag(excludedTag)
				.build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		converterRegistry.addConverter(this);
	}
}
