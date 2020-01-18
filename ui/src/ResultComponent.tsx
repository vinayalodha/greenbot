import { Component } from "react";
import { AnalysisResponse } from "./model/AnalysisResponse";
import React from "react";

type ResultProps = {
	result: AnalysisResponse
}

export class ResultComponent extends Component<ResultProps, {}> {

	render() {
		if (this.props.result.items.length === 0) {
			return null;
		}
		const items = []

		for (const item of this.props.result.items) {
			items.push(
				<tr>
					<td>{item.resourceIds}</td>
					<td>{item.ruleId}</td>
					<td>{item.confidence}</td>
					<td>{item.message}</td>
					<td>{item.approxCostSaving}</td>
				</tr>
			)
		}
		return (
			<div>
				<table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
					<thead>
						<tr>
							<th>Resource Ids</th>
							<th>Rule Id</th>
							<th>Confidence</th>
							<th>Message</th>
							<th>Approx Cost Saving</th>
						</tr>
					</thead>
					<tbody>
						{items}
					</tbody>
				</table>
			</div>
		);
	}
}
