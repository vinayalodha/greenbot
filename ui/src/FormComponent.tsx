import React, { ChangeEvent, Component } from "react";
import axios, { AxiosResponse } from "axios";
import './css/FormComponent.css'

type FormComponentState = {
	configJson: string;
	analyzeButtonText : string;
	disableForm: boolean;
};

type FormComponentProps = {
	callback: Function;
}

export class FormComponent extends Component<FormComponentProps, FormComponentState> {
	constructor(props: FormComponentProps) {
		super(props);
		this.state = {
			configJson: "",
			analyzeButtonText : "Analyze",
			disableForm: false
		};
	}

	handleFormSubmit() {
		this.setState({ ...this.state, analyzeButtonText: "Working on it! this may take a while" , disableForm: true});

		axios.post("/rule",
			{
				configJson: this.state.configJson
			})
			.then((analysisResponse: AxiosResponse) => {
				this.props.callback(analysisResponse.data);
				this.setState({ ...this.state, analyzeButtonText: "Analyze", disableForm: false });
			})
			.catch((err:any )=>{
				this.setState({ ...this.state, disableForm: true, analyzeButtonText: "Something went wrong, please check application console. Raise bug report if needed" });
			})
	}

	private onConfigJsonChanged = (e: ChangeEvent<HTMLTextAreaElement>) => {
		// Merge state
		this.setState({ ...this.state, configJson: e.target.value });
	};

	componentDidMount(): void {

		axios.get("/rule/config")
			.then((value: AxiosResponse) => {
				this.setState({ ...this.state, configJson: JSON.stringify(value.data, null, 4) });
			})
			.catch((_err:any )=>{
				alert('Something went wrong, please check application console');
			})

	}

	render() {
		return (
			<div>
				<h1 className='heading is-large'>Let's Begin</h1>
				<div className="field">
					<label className="label">
						Configuration parameters to be used for each rule. Please refer
                        documentation for more info.If you are unsure keep it as is
                    </label>
					<div className="control">
						<textarea
							className="textarea"
							id="config-json"
							name="configJson"
							value={this.state.configJson}
							onChange={
								(e: ChangeEvent<HTMLTextAreaElement>) =>
									this.onConfigJsonChanged(e)
							}
						/>
					</div>
				</div>
				<div className="field is-grouped">
					<div className="control">
						<button
							className="button is-link"
							id="analyze"
							type="submit"
							onClick={this.handleFormSubmit.bind(this)}
							disabled={this.state.disableForm}
						>
							{this.state.analyzeButtonText}
                        </button>
					</div>
				</div>				
			</div>
			
		);
	}
}
