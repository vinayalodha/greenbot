package greenbot.main.model.ui;

import java.util.ArrayList;
import java.util.List;

import greenbot.main.model.Tag;
import greenbot.rule.model.ConfigParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AnalysisRequest {
	
	@Builder.Default
	private final boolean untaggedResourcesOnly=false;
	
	@Builder.Default
	public final List<Tag> tags = new ArrayList<Tag>();
	
	private final List<ConfigParam> configParams;

}
