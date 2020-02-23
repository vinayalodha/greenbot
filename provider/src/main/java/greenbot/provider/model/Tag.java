package greenbot.provider.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tag {
	private String key;
	private String value;
}
