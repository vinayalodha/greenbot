package greenbot.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import greenbot.main.rules.service.RuleLifecycleManager;
import greenbot.rule.model.RuleInfo;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
	private final RuleLifecycleManager ruleLifecycleManager;

	private final ObjectMapper ObjectMapper;

	@GetMapping("/")
	public String get(Model model) throws JsonProcessingException {
		model.addAttribute("name", "Hello World");
		List<RuleInfo> aInfos = new ArrayList<RuleInfo>();
		aInfos.addAll(ruleLifecycleManager.getRuleInfos());
		aInfos.addAll(ruleLifecycleManager.getRuleInfos());
		aInfos.addAll(ruleLifecycleManager.getRuleInfos());
		
		model.addAttribute("ruleInfos", ObjectMapper.writeValueAsString(aInfos));
		model.addAttribute("configParams", ObjectMapper.writeValueAsString(ruleLifecycleManager.getConfigParams()));
		return "index";
	}

}