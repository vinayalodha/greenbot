package greenbot.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import greenbot.main.model.ui.AnalysisRequest;
import greenbot.main.rules.service.RuleLifecycleManager;
import greenbot.rule.model.RuleResponse;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ResultController {

	private RuleLifecycleManager ruleLifecycleManager;
	@GetMapping("/resultok")
	public String getOk(){
		return "resultok";
	}
	

	@PostMapping("/resultnotok")
	public String getNotOk(@ModelAttribute AnalysisRequest request, final Model model){
		RuleResponse ruleResponse = ruleLifecycleManager.execute(request);
		model.addAttribute("ruleResponse", ruleResponse);
		return "resultnotok";
	}
	
	@GetMapping("resultnotok")
	public String get(final Model model){
		return "resultnotok";
	}
}
