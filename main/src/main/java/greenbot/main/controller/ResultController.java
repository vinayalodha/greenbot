package greenbot.main.controller;

import greenbot.rule.model.AnalysisResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import greenbot.main.model.ui.AnalysisRequest;
import greenbot.main.rules.service.RuleLifecycleManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResultController {

	private RuleLifecycleManager ruleLifecycleManager;

	public String getOk() {
		return "resultok";
	}

	public String getNotOk(@ModelAttribute AnalysisRequest request, final Model model) {
		AnalysisResponse analysisResponse = ruleLifecycleManager.execute(request);
		model.addAttribute("ruleResponse", analysisResponse);
		return "resultnotok";
	}

	public String get(final Model model) {
		return "resultnotok";
	}
}
