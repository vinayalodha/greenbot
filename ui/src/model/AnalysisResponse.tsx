export class AnalysisResponse {
	infoMessages: String[] = [];
	warningMessages: String[] = [];
	errorMessages: String[] = [];
	items: RuleResponseItem[] = [];
}

class RuleResponseItem {
	resourceId: String = "";
	ruleId: String = "";
	confidence: String = "";
	message: string = "";
	approxCostSaving: String = "";
}