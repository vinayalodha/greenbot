package greenbot.rule.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RuleInfo {
	private final String id;
	private final List<String> permissions;
}
