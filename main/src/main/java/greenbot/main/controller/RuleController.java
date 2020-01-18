package greenbot.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greenbot.main.rules.service.RuleLifecycleManager;
import greenbot.rule.model.RuleInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class RuleController {

    RuleLifecycleManager ruleLifecycleManager;
ObjectMapper objectMapper;
    @GetMapping("rule/info")
    @SneakyThrows
    public String getRulePermission(){
        return objectMapper.writeValueAsString(ruleLifecycleManager.getRuleInfos());
    }
}
