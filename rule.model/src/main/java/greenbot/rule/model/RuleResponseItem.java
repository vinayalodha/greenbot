package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.Value;

@Data
@Builder
public class RuleResponseItem {

	@Singular
	private List<String> resourceIds;

	private String ruleId;

	private AnalysisConfidence confidence;

	private String message;

	private String approxCostSaving;
}
