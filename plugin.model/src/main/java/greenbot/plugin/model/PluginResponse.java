package greenbot.plugin.model;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class PluginResponse {
	@Singular
	private final List<String> infoMessages;
	@Singular
	private final List<String> warningMessages;
	@Singular
	private final List<String> errorMessages;
	@Singular
	private final List<PluginResponseItem> items;;
}
