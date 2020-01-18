package greenbot.rule.model.utils;

import java.util.function.BinaryOperator;

import greenbot.rule.model.RuleResponse;

public class RuleResponseReducer implements BinaryOperator<RuleResponse>{
	
	@Override
	public RuleResponse apply(RuleResponse one, RuleResponse two) {
		return RuleResponse.builder()
				.infoMessages(one.getInfoMessages())
				.infoMessages(two.getInfoMessages())
				.warningMessages(one.getWarningMessages())
				.warningMessages(two.getWarningMessages())
				.errorMessages(one.getErrorMessages())
				.errorMessages(two.getErrorMessages())
				.items(one.getItems())
				.items(two.getItems())
				.build();
		
	}

}
