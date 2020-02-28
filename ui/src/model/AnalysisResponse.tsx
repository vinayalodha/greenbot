export class AnalysisResponse {
	infoMessages: String[] = [];
	warningMessages: String[] = [];
	errorMessages: String[] = [];
	items: RuleResponseItem[] = [];
}

class RuleResponseItem {
	resourceIds: String[] = [];
	ruleId: String = "";
	confidence: String = "";
	message: string = "";
	approxCostSaving: String = "";
}