package greenbot.main.rules.aws.docker;

import java.util.Arrays;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;

public class UnderUtilizedFargateRule extends AbstractGreenbotRule {

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Is fargate container under-utilized")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();
    }
}
