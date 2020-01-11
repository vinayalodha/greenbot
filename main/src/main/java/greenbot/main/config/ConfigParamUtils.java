package greenbot.main.config;

import java.util.Arrays;
import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigParamUtils {
	public List<ConfigParam> awsTagConfigs() {
		ConfigParam unTagged = ConfigParam.builder().key("onlyIncludeUntaggedResources").value("false")
				.description("Only analyze AWS resources which are un-tagged").build();

		ConfigParam tagged = ConfigParam.builder().key("filterBasedOnTags").value("").description(
				"comma separated value for tags. Analyze AWS resources which matches given tag. Keep it empty to analyze all tags. For example tag1=value1,tag2-value2")
				.build();

		return Arrays.asList(unTagged, tagged);

	}
}
