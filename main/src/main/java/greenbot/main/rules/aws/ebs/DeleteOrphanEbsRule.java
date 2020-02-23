package greenbot.main.rules.aws.ebs;

import java.util.Arrays;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;

public class DeleteOrphanEbsRule extends AbstractGreenbotRule {
    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildRuleId())
                .description("Any orphan EBS drives")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();
    }
}
