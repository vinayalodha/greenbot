package greenbot.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import greenbot.rule.model.utils.RuleResponseReducer;

@Configuration
public class Beans {

	@Bean
	public RuleResponseReducer getRuleResponseReducer() {
		return new RuleResponseReducer();
	}

}
