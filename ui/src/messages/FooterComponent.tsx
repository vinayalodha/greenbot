/*
 * Copyright 2019-2020 Vinay Lodha (mailto:vinay.a.lodha@gmail.com)
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

export class FooterComponent extends Component<{}> {

    render() {
        return (
            <footer className="footer">
                <div className="content has-text-centered">
                    <p><strong>Maintained by <a href="https://www.linkedin.com/in/vinaylodha/" target='_blank' rel="noopener noreferrer">Vinay Lodha</a>
                    <br/>
                    <a href="mailto:vinay.a.lodha@gmail.com">Feedback</a> | <a target="_blank" href="https://github.com/vinay-lodha/greenbot/issues">Report Bug</a> </strong></p>
                </div>
            </footer>
        );
    }
}