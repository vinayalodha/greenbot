package greenbot.main.rules.aws.ec2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;

public class OlderEc2GenerationRule extends greenbot.main.rules.AbstractGreenbotRule {

	private static final Map<String, String> INSTANCE_REPLACE_MAP = buildInstanceReplaceMap();
	private static final String RULE_DESC = "%s EC2 instance can be replaced with newer generation EC2 %s instance";

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		return RuleResponse.builder()
				.infoMessage("Info Message 1")
				.errorMessage("Error Message 1")
				.warningMessage("Warning Message 1")
				.item(RuleResponseItem.builder()
						.resourceId("resourceId")
						.approxCostSaving("30")
						.message("Use T3 instead of T2")
						.confidence(AnalysisConfidence.HIGH).build())
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Is older generation of instances are used")
				.permissions(Arrays.asList("ReadEc2State", "ReadCloudWatch"))
				.build();
	}

	private static Map<String, String> buildInstanceReplaceMap() {
		Map<String, String> retval = new HashMap<>();
		retval.put("t3", "t3a");
		retval.put("t2", "t3a");
		retval.put("m4", "m5a");
		retval.put("m5", "m5a");
		retval.put("c4", "c5");
		retval.put("r4", "r5a");
		retval.put("r5", "r5a");

		// TODO
		// Memory Optimized
		// Accelerated Computing
		return retval;
	}

}
