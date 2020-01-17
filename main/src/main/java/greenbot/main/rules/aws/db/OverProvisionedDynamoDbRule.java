package greenbot.main.rules.aws.db;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;

public class OverProvisionedDynamoDbRule extends AbstractGreenbotRule {

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        throw new RuntimeException();
    }

    @Override
    public RuleInfo ruleInfo() {
        throw new RuntimeException();
    }

}
