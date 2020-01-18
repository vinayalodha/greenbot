import React, {ChangeEvent, Component} from "react";
import {AnalysisResponse} from "./model/AnalysisResponse";
import axios, {AxiosResponse} from "axios";
import './css/FormComponent.css'
type FormComponentState = {
    configJson: string;
};
type  FormComponentProps = {
    callback: Function;
}

export class FormComponent extends Component<FormComponentProps, FormComponentState> {
    constructor(props: FormComponentProps) {
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

    componentDidMount(): void {
        debugger
        axios.get("/rule/config")
            .then((value: AxiosResponse) => {
                debugger
                console.log(value.data);
                this.setState({...this.state, configJson: JSON.stringify(value.data, null, 4)});
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
                        >
                            Analyze
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}
