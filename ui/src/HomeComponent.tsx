import React, { Component } from 'react';
import { FormComponent } from "./FormComponent";
import { HeaderComponent } from "./HeaderComponent";
import { AboutComponent } from "./messages/AboutComponent";
import { AwsCliMessageComponent } from "./messages/AwsCliMessageComponent";
import { PermissionsMessageComponent } from "./messages/PermissionsMessageComponent";
import { AnalysisResponse } from "./model/AnalysisResponse";
import { MessagesListComponent } from "./messages/MessagesListComponent";
import { FooterComponent } from "./messages/FooterComponent";
import './css/HomeComponent.css'
import { ResultComponent } from "./ResultComponent";
import 'bulma/css/bulma.css'

type HomeComponentState = {
	analysisResponse: AnalysisResponse,
	message: string
}

export class HomeComponent extends Component<{}, HomeComponentState> {

	constructor(props: {}) {
		super(props);
		let analysisResponse = new AnalysisResponse();
		this.state = {
			analysisResponse: analysisResponse,
			message : ""
		};
	}

	callBackFunction(analysisResponse: AnalysisResponse) {
		if(analysisResponse.errorMessages.length === 0 
			&& analysisResponse.infoMessages.length === 0
			&& analysisResponse.warningMessages.length === 0
			&& analysisResponse.items.length === 0){
				this.setState({ ...this.state, message: "Everything looks good!" });
			}
		this.setState({ ...this.state, analysisResponse: analysisResponse });
	}

	render() {
		return (
			<div className="App">
				<HeaderComponent />
				<section className="container">
					<AboutComponent />
					<AwsCliMessageComponent />
					<PermissionsMessageComponent />
					<FormComponent
						callback={(analysisResponse: AnalysisResponse) => this.callBackFunction(analysisResponse)} />
					<br />
					<strong className="subtitle is-4 has-text-grey-light">{this.state.message}</strong>
					<MessagesListComponent messages={this.state.analysisResponse.errorMessages} type={"message is-danger"} />
					<MessagesListComponent messages={this.state.analysisResponse.warningMessages} type={"message is-warning"} />
					<MessagesListComponent messages={this.state.analysisResponse.infoMessages} type={"message is-info"} />
					<ResultComponent result={this.state.analysisResponse}></ResultComponent>
					<FooterComponent />
				</section>
			</div>
		);
	}
}

export default HomeComponent;
