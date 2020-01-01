package greenbot.template;

import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import greenbot.plugin.model.GreenbotExtension;
import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;

@Extension
public class TemplatePlugin extends Plugin implements ExtensionPoint {

	public TemplatePlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Extension(ordinal = 1)
	public static class TemplateExtension implements GreenbotExtension {
		@Override
		public PluginResponse doWork(PluginRequest pluginRequest) {
			return new PluginResponse();
		}

	}

}
