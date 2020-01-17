import React, {Component} from 'react'; // let's also import Component

export class PermissionsNeededMessage extends Component<{}> {

    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Below is the list permissions needed for each rule.
                </div>
                <pre>
				<code className="language-json" data-lang="json">TODO</code>
			</pre>
            </div>
        );
    }
}