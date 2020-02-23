package greenbot.main.rules.aws.misc;

import java.util.Arrays;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;

public class UnusedElasticIpRule extends AbstractGreenbotRule {
    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Check if unused elastic IP exists")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();
    }
}
