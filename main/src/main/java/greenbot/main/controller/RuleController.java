package greenbot.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import greenbot.main.model.ui.AnalysisRequest;
import greenbot.main.rules.service.RuleLifecycleManager;
import greenbot.rule.model.ConfigParam;
import greenbot.rule.model.RuleInfo;
import greenbot.rule.model.RuleRequest;
import greenbot.rule.model.RuleResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RuleController {

    private final ConversionService conversionService;
    private final RuleLifecycleManager ruleLifecycleManager;

    @GetMapping("rule/info")
    public List<RuleInfo> getRulePermission() {
        return ruleLifecycleManager.getRuleInfos();
    }

    @GetMapping("rule/config")
    public Map<String, List<ConfigParam>> getConfig() {
        return ruleLifecycleManager.getConfigParams();
    }

    @PostMapping("rule")
    public RuleResponse post(@RequestBody AnalysisRequest request) {
        return ruleLifecycleManager.execute(conversionService.convert(request, RuleRequest.class));
    }
}
