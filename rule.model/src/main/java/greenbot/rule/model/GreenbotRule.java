package greenbot.rule.model;

import java.util.List;

public interface GreenbotRule {

	AnalysisResponse doWork(RuleRequest ruleRequest);

	RuleInfo ruleInfo();

	List<ConfigParam> configParams();
}
