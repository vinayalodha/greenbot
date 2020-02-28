package greenbot.rule.model.cloud;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tag {
	private String key;
	private String value;
}
