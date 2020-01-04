package greenbot.plugin.ec2;

import org.springframework.stereotype.Component;

import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;
import greenbot.plugin.model.optional.Task;

@Component
public class NewGenInstanceFinderTask implements Task {
	@Override
	public PluginResponse apply(PluginRequest t) {
		return null;
	}
}
