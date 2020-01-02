package greenbot.helloworld;

import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import greenbot.plugin.model.GreenbotExtension;
import greenbot.plugin.model.PluginInfo;
import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;

@Extension
public class HelloWorldPlugin extends Plugin implements ExtensionPoint {

	public HelloWorldPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Extension(ordinal = 1)
	public static class HelloWorldExtension implements GreenbotExtension {
		@Override
		public PluginResponse doWork(PluginRequest pluginRequest) {
			return new PluginResponse();
		}
		
		@Override
		public PluginInfo getPluginInfo() {
			return null;
		}

	}

}
