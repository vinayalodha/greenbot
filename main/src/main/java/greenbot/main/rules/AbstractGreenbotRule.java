package greenbot.main.rules;

import org.apache.commons.lang3.StringUtils;

import greenbot.rule.model.GreenbotRule;

public abstract class AbstractGreenbotRule implements GreenbotRule {

	protected String buildRuleId() {
		char separator = '_';
		String[] tokens = StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName());
		String name = StringUtils.join(tokens, separator);
		return StringUtils.lowerCase(name);
	}

}
