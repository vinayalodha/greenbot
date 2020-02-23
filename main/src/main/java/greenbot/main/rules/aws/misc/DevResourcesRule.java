package greenbot.main.rules.aws.misc;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import greenbot.main.rules.AbstractGreenbotRule;
import greenbot.main.rules.service.TagAnalyzer;
import greenbot.provider.model.Compute;
import greenbot.provider.service.ComputeService;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.RuleResponseItem;
import lombok.AllArgsConstructor;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;

@Service
@AllArgsConstructor
public class DevResourcesRule extends AbstractGreenbotRule {

	private TagAnalyzer devTagAnalyzer;
	private ComputeService computeService;

	@Override
	public RuleResponse doWork(RuleRequest ruleRequest) {
		// TODO also check for RDS
		List<Compute> computes = computeService.list();
		List<String> filteredComputes = computes.stream()
				.filter(compute -> devTagAnalyzer.isDevTagPresent(compute.getTags()))
				.map(Compute::getId)
				.collect(toList());

		if (filteredComputes.isEmpty())
			return null;

		RuleResponseItem item = RuleResponseItem.builder()
				.resourceIds(filteredComputes)
				.confidence(AnalysisConfidence.LOW)
				.message("Consider adding lifecycle around dev/staging/test resources")
				.ruleId(buildRuleId())
				.build();
		
		return RuleResponse.builder()
				.item(item)
				.build();
	}

	@Override
	public RuleInfo ruleInfo() {
		return RuleInfo.builder()
				.id(buildRuleId())
				.description("Does dev/staging/test resources running 24 hours?")
				.permissions(Arrays.asList("ReadEc2State"))
				.build();
	}

}
