package greenbot.main.rules.aws.ec2;

import java.util.Arrays;
import java.util.List;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;

public class SwitchToInf1InstanceRule extends AbstractGreenbotRule {

    private static final List<String> SWITCH_MAP = Arrays.asList("g4", "g3", "p3", "p2");
    private static final String MESSAGE = "Consider switching to EC2 Inf1 instances";

    @Override
    public RuleResponse doWork(RuleRequest ruleRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildId())
                .description("Consider switching to EC2 Inf1 instances for inference workload")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();

    }
}
