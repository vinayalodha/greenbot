package greenbot.rule.model;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RuleRequest {
    private final Provider provider;
    private final Map<String, ConfigParam> configParams;
    
}
