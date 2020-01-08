package greenbot.main.converter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.stereotype.Component;

import greenbot.main.model.AnalysisRequest;
import greenbot.rule.model.RuleRequest;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AnalysisRequestToRuleRequest implements Converter<AnalysisRequest, RuleRequest>, InitializingBean{

	private final ConverterRegistry converterRegistry;
	@Override
	public RuleRequest convert(AnalysisRequest source) {
		return RuleRequest.builder()
				.configParams(source.getConfigParams())
				.build();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		converterRegistry.addConverter(this);
	}
}
