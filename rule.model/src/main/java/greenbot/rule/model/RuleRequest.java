package greenbot.rule.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuleRequest {
	private Provider provider;

	private Tag excludedTag;
	private Tag includedTag;

}
