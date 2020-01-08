package greenbot.main.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Tag {
	private final String key;
	private final String value;
}
