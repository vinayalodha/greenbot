package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class RuleResponseItem {
	
	@Singular
	private final List<String> resourceIds;
	
	private final String ruleId;
	
	private final AnalysisConfidence confidence;
	
	private final String message;
	
	private final int approxCostSaving;
}
