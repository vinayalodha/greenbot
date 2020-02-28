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
import React, {Component} from 'react';

export class AwsCliMessageComponent extends Component<{}> {

    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Do make sure <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html" target="_blank" rel="noopener noreferrer">aws cli</a> is configured with correct IAM roles and you are using <a href='https://github.com/vinay-lodha/greenbot/releases'>most recent version</a> of Greenbot (current version <strong>v1.0.0</strong>).
                </div>
            </div>
        );
    }
}