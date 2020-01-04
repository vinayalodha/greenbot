package greenbot.plugin.ec2;

import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import greenbot.plugin.model.GreenbotExtension;
import greenbot.plugin.model.PluginInfo;
import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;

@Extension
public class Ec2Plugin extends Plugin implements ExtensionPoint {

	private static AnnotationConfigApplicationContext applicationContext;

	public Ec2Plugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		if(applicationContext!=null)
			return;
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
        applicationContext.register(Beans.class);
        applicationContext.refresh();        
	}

	@Override
	public void stop() {
		applicationContext.stop();
		applicationContext = null;
	}

	public static AnnotationConfigApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Extension(ordinal = 1)
	public static class Ec2Extension implements GreenbotExtension {

	
		@Override
		public PluginResponse doWork(PluginRequest pluginRequest) {
			return PluginResponse.builder()
					.build();
		}

		@Override
		public PluginInfo pluginInfo() {
			return getApplicationContext().getBean(PluginInfo.class);
		}

	}

}
