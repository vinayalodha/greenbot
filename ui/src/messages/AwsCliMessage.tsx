import React, {Component} from 'react';

export class AwsCliMessage extends Component<{}> {

    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Do make sure <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html"
                                    target="_blank">AWS profile</a> is updated with below IAM roles.
                </div>
            </div>
        );
    }
}