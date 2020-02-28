package greenbot.main.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import greenbot.rule.model.ConfigParam;

@Component
public class ConfigParamUtils {

	public static final String TOO_MANY_AMI_THRESHOLD = "too_many_ami_threshold";
	public static final String EXCLUDED_TAG = "excluded_tag";
	public static final String INCLUDED_TAG = "included_tag";

	@Value("${threshold.ami}")
	private String amiThreshold;

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

		ConfigParam tooManyAmiTag = ConfigParam.builder()
				.key(TOO_MANY_AMI_THRESHOLD)
				.value(amiThreshold)
				.description("Threshold AMI count above which TooManyInstanceImagesRule will raise concern")
				.build();
		return Arrays.asList(excludedTag, includedTag, tooManyAmiTag);
	}

}
