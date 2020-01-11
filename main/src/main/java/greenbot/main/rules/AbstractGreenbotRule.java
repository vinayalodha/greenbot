package greenbot.main.rules;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.GreenbotRule;

public abstract class AbstractGreenbotRule implements GreenbotRule {

	protected String buildId() {
		return StringUtils.lowerCase(
				StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName()), '_'));
	}
	

	@Override
	public List<ConfigParam> configParams() {
		return null;
	}

}
