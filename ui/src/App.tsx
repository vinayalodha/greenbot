import React, {Component} from 'react';
import {AnalyzeForm} from "./AnalyzeForm";
import {PageHeader} from "./PageHeader";
import {AboutGreenbot} from "./messages/AboutGreenbot";
import {AwsCliMessage} from "./messages/AwsCliMessage";
import {PermissionsNeededMessage} from "./messages/PermissionsNeededMessage";
import {AnalysisResponse} from "./model/AnalysisResponse";
import {MessagesList} from "./messages/MessagesList";
import './css/App.css'
import {Footer} from "./messages/Footer";
type AppState = {
    analysisResponse: AnalysisResponse
}

export class App extends Component<{}, AppState> {

    constructor(props: {}) {
        super(props);
        let analysisResponse = new AnalysisResponse([]);
        this.state = {
            analysisResponse: analysisResponse
        };
    }

    callBackFunction(analysisResponse: AnalysisResponse) {
        this.setState({...this.state, analysisResponse: analysisResponse});
    }

    render() {
        return (
            <div className="App">
                <PageHeader/>
                <section className="container">
                    <AboutGreenbot/>
                    <AwsCliMessage/>
                    <PermissionsNeededMessage/>

                    <AnalyzeForm
                        callback={(analysisResponse: AnalysisResponse) => this.callBackFunction(analysisResponse)}/>
                    <br/>
                    <MessagesList messages={this.state.analysisResponse.errors} type={"message is-danger"}/>
                    <MessagesList messages={this.state.analysisResponse.errors} type={"message is-warning"}/>
                    <MessagesList messages={this.state.analysisResponse.errors} type={"message is-info"}/>
                    <Footer/>
                </section>
            </div>
        );
    }
}

export default App;
