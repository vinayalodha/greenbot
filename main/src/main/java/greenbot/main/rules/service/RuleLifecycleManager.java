package greenbot.main.rules.service;

import java.util.*;
import java.util.stream.Collectors;

import greenbot.rule.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import greenbot.main.model.ui.AnalysisRequest;
import greenbot.rule.model.AnalysisResponse;
import greenbot.rule.model.utils.RuleResponseReducer;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RuleLifecycleManager {

	private final List<GreenbotRule> rules;

	private final RuleResponseReducer responseReducer;

	private ConversionService conversionService;

	public AnalysisResponse execute(AnalysisRequest request) {
		return execute(conversionService.convert(request, RuleRequest.class));
	}

	public AnalysisResponse execute(RuleRequest request) {
		List<String> errorMessages = new ArrayList<>();
		AnalysisResponse response = rules.stream().map(rule -> {
			try {
				return rule.doWork(request);
			} catch (Exception e) {
				errorMessages.add(StringUtils.abbreviate(ExceptionUtils.getRootCauseMessage(e), 200));
			}
			return null;
		}).filter(Objects::nonNull).reduce(responseReducer).orElse(AnalysisResponse.builder().build());
		response.getErrorMessages().addAll(errorMessages);
		return response;
	}

	public Map<String, List<ConfigParam>> getConfigParams() {
		Map<String, List<ConfigParam>> retval = new TreeMap<>();
		rules.forEach(rule -> {
			if (CollectionUtils.isNotEmpty(rule.configParams()))
				retval.put(rule.ruleInfo().getId(), rule.configParams());
		});
		return retval;
	}

	public List<RuleInfo> getRuleInfos() {
		return rules.stream().map(GreenbotRule::ruleInfo).collect(Collectors.toList());
	}

}
