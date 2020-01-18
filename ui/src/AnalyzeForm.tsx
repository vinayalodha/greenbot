import React, {ChangeEvent, Component} from "react";
import {AnalysisResponse} from "./model/AnalysisResponse";

type RequestFormDataState = {
    configJson: String;
};
type  AnalyzeFormProps = {
    callback: Function;
}

export class AnalyzeForm extends Component<AnalyzeFormProps, RequestFormDataState> {
    constructor(props: AnalyzeFormProps) {
        super(props);
        this.state = {
            configJson: ""
        };
    }

    handleFormSubmit() {
        let analysisResponse = new AnalysisResponse(['Apple', 'Orange', 'Banana']);
        this.props.callback(analysisResponse);
    }

    private onConfigJsonChanged = (e: ChangeEvent<HTMLTextAreaElement>) => {
        // Merge state
        this.setState({...this.state, configJson: e.target.value});
    };

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
                        >
                            Analyze
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}
