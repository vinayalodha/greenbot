package greenbot.rule.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConfigParam {
	private final String key;
	private final Object value;
	private final String description;
}
