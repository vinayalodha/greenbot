package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Compute {
	private String id;
	private List<Tag> tags;
}
