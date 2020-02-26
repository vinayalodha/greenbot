package greenbot.main.config;

import java.util.Arrays;
import java.util.List;

import greenbot.rule.model.ConfigParam;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigParamUtils {

	public static final String EXCLUDED_TAG = "excludedTag";
	public static final String INCLUDED_TAG = "includedTag";

	public List<ConfigParam> buildEmpty() {
		ConfigParam excludedTag = ConfigParam.builder()
				.key(EXCLUDED_TAG)
				.value("")
				.description("Resources with this tag will not be analyzed. Format <key>=<value>")
				.build();

		ConfigParam includedTag = ConfigParam.builder()
				.key(INCLUDED_TAG)
				.value("")
				.description("Only resources with this tag will be analyzed. Format <key>=<value>")
				.build();

		return Arrays.asList(excludedTag, includedTag);
	}

}
