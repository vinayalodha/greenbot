/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
					<td>{item.resourceId}</td>
					<td>{item.service}</td>
					<td>{item.ruleId}</td>
					<td>{item.confidence}</td>
					<td>{item.message}</td>
				</tr>
			)
		}
		var url = "/report/" + this.props.result.id;
		return (
			<div>
				<a href={url} className="is-pulled-right">Download Report as CSV</a>
				<table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
					<thead>
						<tr>
							<th>Resource Id</th>
							<th>Service</th>
							<th>Rule Id</th>
							<th>Confidence</th>
							<th>Message</th>
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
