package greenbot.main.rules.aws.ec2;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import greenbot.main.config.ConfigParamUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.GreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;

@Component
public class UnderUtilizedRdsRule implements GreenbotRule {

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		return RuleResponse.builder()
				.infoMessage("Info Message 1")
				.errorMessage("Error Message 1")
				.warningMessage("Warning Message 1")
				.item(RuleResponseItem.builder()
						.resourceId("resourceId")
						.approxCostSaving(30)
						.message("Use T3 instead of T2")
						.confidence(AnalysisConfidence.HIGH)
						.build())
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(null)
				.name("Check if RDS instances are under utilized rule")
				.permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
				.build();
	}

	@Override
	public List<ConfigParam> configParams() {
		return ConfigParamUtils.awsTagConfigs();
	}
}
