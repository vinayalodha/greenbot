export class AnalysisResponse {
	id: String = "";
	infoMessages: String[] = [];
	warningMessages: String[] = [];
	errorMessages: String[] = [];
	items: RuleResponseItem[] = [];
}

class RuleResponseItem {
	resourceId: String = "";
	service: String = "";
	ruleId: String = "";
	confidence: String = "";
	message: string = "";
	approxCostSaving: String = "";
}