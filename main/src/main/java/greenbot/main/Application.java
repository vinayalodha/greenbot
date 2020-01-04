package greenbot.main;

import java.util.List;

import org.pf4j.PluginManager;
import org.pf4j.RuntimeMode;
import org.pf4j.ZipPluginManager;

import greenbot.plugin.model.GreenbotExtension;

//@SpringBootApplication
public class Application {
	

	public static void main(String[] args) {
		PluginManager pluginManager = new ZipPluginManager(){
			public org.pf4j.RuntimeMode getRuntimeMode() {
				return RuntimeMode.DEVELOPMENT;
			};
		};
		// or "new ZipPluginManager() / new	// DefaultPluginManager()"
		// start and load all plugins of application
		pluginManager.loadPlugins();
		pluginManager.startPlugins();

		// retrieve all extensions for "Greeting" extension point
		List<GreenbotExtension> greetings = pluginManager.getExtensions(GreenbotExtension.class);
		for (GreenbotExtension greeting : greetings) {
			System.out.println(">>> " + greeting.pluginInfo());

		}

		// stop and unload all plugins
		pluginManager.stopPlugins();
	}

}
