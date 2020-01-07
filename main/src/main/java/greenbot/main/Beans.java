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
	@Bean
	WebMvcConfigurer configurer() {
	   return new WebMvcConfigurer() {
	      @Override
	      public void addResourceHandlers(ResourceHandlerRegistry registry) {
	          registry
	                .addResourceHandler("/resources/**")
	                .addResourceLocations("classpath:/static/")
	                              .setCachePeriod(0);
	          
	          registry
              .addResourceHandler("/webjars/**")
              .addResourceLocations("/webjars/")
                            .setCachePeriod(0);
	      }
	   };
	}

}
