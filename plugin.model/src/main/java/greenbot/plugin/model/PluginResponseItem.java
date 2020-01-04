package greenbot.plugin.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PluginResponseItem {
	private final AnalysisConfidence confidence;
	private final String message;
	private final String resourceId;
	private final int approxCostSaving;
}
