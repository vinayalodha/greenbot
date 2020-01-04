package greenbot.plugin.helloworld;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import greenbot.plugin.model.PluginInfo;

@Configuration
@ComponentScan(basePackages = "greenbot.plugin.helloworld")
public class Beans {
	
	@Bean
	public PluginInfo getPluginInfo(){
		return PluginInfo.builder()
				.name("Hello World")
				.permissions(Arrays.asList())
				.configParams(Arrays.asList())
				.build();
	}

}
