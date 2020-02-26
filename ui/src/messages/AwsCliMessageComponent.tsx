import React, {Component} from 'react';

export class AwsCliMessageComponent extends Component<{}> {

    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Do make sure <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html" target="_blank" rel="noopener noreferrer">AWS cli</a> is configured with correct IAM roles
                </div>
            </div>
        );
    }
}