package greenbot.rule.model.utils;

import java.util.function.BinaryOperator;

import greenbot.rule.model.AnalysisResponse;

public class RuleResponseReducer implements BinaryOperator<AnalysisResponse>{
	
	@Override
	public AnalysisResponse apply(AnalysisResponse one, AnalysisResponse two) {
		return AnalysisResponse.builder()
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
