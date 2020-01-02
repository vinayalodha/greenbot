package greenbot.ec2;

import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import greenbot.plugin.model.GreenbotExtension;
import greenbot.plugin.model.PluginInfo;
import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;

@Extension
public class Ec2Plugin extends Plugin implements ExtensionPoint {

	public Ec2Plugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Extension(ordinal = 1)
	public static class Ec2Extension implements GreenbotExtension {
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
