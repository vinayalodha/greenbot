import React, {Component} from 'react'; // let's also import Component

export class Footer extends Component<{}> {

    render() {
        return (
            <footer className="footer">
                <div className="content has-text-centered">
                    <p className="subtitle">
                        <a href="mailto:vinay.a.lodha@gmail.com">Contact me</a> if you need cloud consultation service
                    </p>
                </div>
            </footer>
        );
    }
}