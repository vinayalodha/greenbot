package greenbot.main.rules.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.GreenbotRule;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import greenbot.rule.model.utils.RuleResponseReducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RuleLifecycleManager {

    private final List<GreenbotRule> rules;
    private final RuleResponseReducer responseReducer;

    public RuleResponse execute(RuleRequest request) {
        List<String> errorMessages = new ArrayList<>();
        RuleResponse response = rules.stream()
                .map(rule -> {
                    try {
                        return rule.doWork(request);
                    } catch (Exception e) {
                    	log.error("", e);
                        errorMessages.add(StringUtils.abbreviate(ExceptionUtils.getRootCauseMessage(e), 200));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(responseReducer)
                .orElse(RuleResponse.builder().build());
        return response.toBuilder().errorMessages(errorMessages).build();
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
