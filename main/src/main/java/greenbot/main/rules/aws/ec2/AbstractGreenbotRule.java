package greenbot.main.rules.aws.ec2;

import org.apache.commons.lang3.StringUtils;

import greenbot.rule.model.GreenbotRule;

public abstract class AbstractGreenbotRule implements GreenbotRule{

	protected String buildId() {
		return StringUtils.lowerCase(
				StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName()), '_'));
	}
}
