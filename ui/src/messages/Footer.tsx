import React, {Component} from 'react'; // let's also import Component

export class Footer extends Component<{}> {

    render() {
        return (
            <div className="content has-text-centered">
                <p className="subtitle">
                    <a href="mailto:vinay.a.lodha@gmail.com">Contact me</a> if you need Sidekick to optimize AWS cost.
                </p>
            </div>
        );
    }
}