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
import React, {Component} from 'react'; // let's also import Component

import axios, {AxiosResponse} from 'axios';


type PermissionsMessageComponentState = {
    ruleInfo: any;
};
export class PermissionsMessageComponent extends Component<{}, PermissionsMessageComponentState> {

    constructor(props: {}) {
        super(props);
        this.state = {
            ruleInfo: ""
        };
    }

    componentDidMount(): void {
        axios.get("/rule/info")
            .then((value: AxiosResponse) => {
                this.setState({...this.state, ruleInfo: JSON.stringify(value.data, null, 4)});
            })
			.catch((err:any )=>{
				alert('Something went wrong, please check application console');
			})

    }


    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Below is the list permissions needed for each rule.
                <pre>
					<code className="language-json" data-lang="json">{this.state.ruleInfo}</code>
				</pre>
                </div>
            </div>
        );
    }
}