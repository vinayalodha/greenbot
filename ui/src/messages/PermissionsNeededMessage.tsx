import React, {Component} from 'react'; // let's also import Component

import axios, {AxiosResponse} from 'axios';


type PermissionsNeededMessageState = {
    ruleInfo: any;
};
export class PermissionsNeededMessage extends Component<{}, PermissionsNeededMessageState> {

    constructor(props: {}) {
        super(props);
        this.state = {
            ruleInfo: ""
        };
    }

    componentDidMount(): void {
        axios.get("/rule/info")
            .then((value: AxiosResponse) => {
                console.log(value.data);
                this.setState({...this.state, ruleInfo: JSON.stringify(value.data, null, 4)});
            })
    }


    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Below is the list permissions needed for each rule.
                </div>
                <pre>
				<code className="language-json" data-lang="json">{this.state.ruleInfo}</code>
			</pre>
            </div>
        );
    }
}