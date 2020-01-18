package greenbot.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import greenbot.rule.model.utils.RuleResponseReducer;

@Configuration
public class Beans {

    @Bean
    public RuleResponseReducer getRuleResponseReducer() {
        return new RuleResponseReducer();
    }

}
