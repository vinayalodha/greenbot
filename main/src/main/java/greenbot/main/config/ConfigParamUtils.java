package greenbot.main.config;

import java.util.Arrays;
import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigParamUtils {
	public List<ConfigParam> awsTagConfig(){
		ConfigParam unTagged = ConfigParam.builder()
				.key("onlyIncludeUntaggedResources")
				.value("false")
				.description("Analyze AWS resources which are un-taged")
				.build();
		
		ConfigParam tagged = ConfigParam.builder()
				.key("filterBasedOnTags")
				.value("")
				.description("Comman sperated value for tags. Analyze AWS resources which matches given tag. Keep it empty to analyze all tags. For example tag1=value1,tag2-value2")
				.build();
		
		return Arrays.asList(unTagged, tagged);
		
	}
}
