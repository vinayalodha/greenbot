package greenbot.plugin.ec2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import greenbot.plugin.model.ConfigParam;
import greenbot.plugin.model.PluginInfo;

@Configuration
@ComponentScan(basePackages = "greenbot.plugin.ec2")
public class Beans {
	
	@Bean
	public PluginInfo getPluginInfo(){
		return PluginInfo.builder()
				.name("AWS EC2 Plugin")
				.permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
				.configParams(buildConfigParams()).build();
	}

	private List<ConfigParam> buildConfigParams() {
		return Collections.emptyList();
	}
	
}
