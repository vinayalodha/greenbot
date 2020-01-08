package greenbot.main.rules.aws.ec2;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import greenbot.main.config.ConfigParamUtils;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;

@Component
public class UnusedElasticIpRule extends AbstractGreenbotRule {

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
				.id(buildId())
				.name("Replace older generation instance with Newer generation AWS EC2 Instance Rule")
				.permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
				.build();
	}

	@Override
	public List<ConfigParam> configParams() {
		return ConfigParamUtils.awsTagConfigs();
	}
}