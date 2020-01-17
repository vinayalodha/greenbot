package greenbot.main.rules.aws.misc;

import java.util.Arrays;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;

public class EcrLifecycleMissingRule extends AbstractGreenbotRule {

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildId())
                .description("ECR lifecycle to cleanup old images exits?")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();
    }

}
