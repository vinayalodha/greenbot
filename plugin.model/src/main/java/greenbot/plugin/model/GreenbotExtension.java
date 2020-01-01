package greenbot.plugin.model;

import org.pf4j.ExtensionPoint;

public interface GreenbotExtension extends ExtensionPoint{
	
	
public PluginResponse doWork(PluginRequest pluginRequest);

}
