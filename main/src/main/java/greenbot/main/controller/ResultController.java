package greenbot.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import greenbot.main.rules.service.RuleLifecycleManager;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ResultController {
	private final RuleLifecycleManager ruleLifecycleManager;

	@GetMapping("/result")
	public String get(final AnalyzeRequest request, final Model model) {
		model.addAttribute("name", "Hello World");

		model.addAttribute("ruleInfos", ruleLifecycleManager.getRuleInfos());
		model.addAttribute("configParams", ruleLifecycleManager.getConfigParams());
		return "result";
	}

	@PostMapping("/result")
	public String post(@ModelAttribute AnalyzeRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("request", request);

		return "redirect:/result";
	}

}
