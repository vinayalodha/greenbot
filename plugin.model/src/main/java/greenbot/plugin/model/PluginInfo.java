package greenbot.plugin.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PluginInfo {
	private final List<String> permissions;
	private final String name;
	private final List<ConfigParam> configParams;
}
