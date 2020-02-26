package greenbot.rule.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstanceStorage {

	private String id;
	@Builder.Default
	private List<Tag> tags = new ArrayList<Tag>();

}
