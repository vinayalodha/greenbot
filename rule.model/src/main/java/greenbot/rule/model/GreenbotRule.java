package greenbot.rule.model;

import java.util.List;

public interface GreenbotRule {

	public RuleResponse doWork(RuleRequest ruleRequest);

	public RuleInfo ruleInfo();

	public List<ConfigParam> configParams();
}
