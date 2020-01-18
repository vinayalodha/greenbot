package greenbot.main.rules.aws.db;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.AnalysisResponse;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponseItem;

@Component
public class UnderUtilizedRdsRule extends AbstractGreenbotRule {

    @Override
    public AnalysisResponse doWork(RuleRequest ruleRequest) {
        return AnalysisResponse.builder().infoMessage("Info Message 1").errorMessage("Error Message 1")
                .warningMessage("Warning Message 1")
                .item(RuleResponseItem.builder().resourceId("resourceId").approxCostSaving(30)
                        .message("Use T3 instead of T2").confidence(AnalysisConfidence.HIGH).build())
                .build();
    }

    @Override
    public RuleInfo ruleInfo() {
        return RuleInfo.builder()
                .id(buildId())
                .description("Is RDS instances are under-utilized")
                .permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
                .build();
    }

}
