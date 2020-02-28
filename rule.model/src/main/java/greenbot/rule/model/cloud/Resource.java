package greenbot.rule.model.cloud;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Resource {

	private String id;

	@Builder.Default
	private List<Tag> tags = new ArrayList<Tag>();

}
