package greenbot.rule.model;

public interface GreenbotRule {

	RuleResponse doWork(RuleRequest ruleRequest);

	RuleInfo ruleInfo();
}
