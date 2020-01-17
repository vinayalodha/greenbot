package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RuleRequest {
    private final Provider provider;
    private final List<ConfigParam> configParams;

}
