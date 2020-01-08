package greenbot.main.rules.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.main.model.AnalysisRequest;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.GreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.utils.RuleResponseReducer;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RuleLifecycleManager {

	private final List<GreenbotRule> rules;

	private final RuleResponseReducer responseReducer;

	private ConversionService conversionService;

	public RuleResponse execute(AnalysisRequest request) {
		return execute(conversionService.convert(request, RuleRequest.class));
	}
	public RuleResponse execute(RuleRequest request) {
		return rules.stream()
				.map(rule -> rule.doWork(request))
				.filter(Objects::nonNull)
				.reduce(responseReducer)
				.orElse(RuleResponse.builder().build());
	}

	public Map<String, List<ConfigParam>> getConfigParams() {
		Map<String, List<ConfigParam>> retval = new TreeMap<String, List<ConfigParam>>();
		rules.forEach(rule -> {
			retval.put(rule.ruleInfo().getId(), rule.configParams());
		});
		return retval;
	}

	public List<RuleInfo> getRuleInfos() {
		return rules.stream()
				.map(GreenbotRule::ruleInfo)
				.collect(Collectors.toList());
	}
}
