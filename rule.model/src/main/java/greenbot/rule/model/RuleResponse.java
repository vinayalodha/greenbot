package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class RuleResponse {
	@Singular
	private final List<String> infoMessages;
	@Singular
	private final List<String> warningMessages;
	@Singular
	private final List<String> errorMessages;
	@Singular
	private final List<RuleResponseItem> items;;
}
