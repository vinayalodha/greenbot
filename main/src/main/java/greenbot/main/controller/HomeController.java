package greenbot.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import greenbot.main.rules.service.RuleLifecycleManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Controller
@AllArgsConstructor
public class HomeController {
	private final RuleLifecycleManager ruleLifecycleManager;

	private final ObjectMapper ObjectMapper;

	@SneakyThrows
	@GetMapping("/")
	public String get(final Model model) {

		model.addAttribute("ruleInfosAsString", ObjectMapper.writeValueAsString(ruleLifecycleManager.getRuleInfos()));
		model.addAttribute("configParamsAsString",
				ObjectMapper.writeValueAsString(ruleLifecycleManager.getConfigParams()));
		return "index";
	}

}