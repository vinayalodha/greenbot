import { Component } from "react";
import { AnalysisResponse } from "./model/AnalysisResponse";

type ResultProps = {
	result: AnalysisResponse
}

export class ResultComponent extends Component<ResultProps, {}> {


	render() {

		if (this.props.result.items.length === 0) {
			return null;
		}
		const items = []
		return null;

	}
}
