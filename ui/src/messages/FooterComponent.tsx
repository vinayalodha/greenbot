import React, {Component} from 'react'; // let's also import Component

export class FooterComponent extends Component<{}> {

    render() {
        return (
            <footer className="footer">
                <div className="content has-text-centered">
                    <p className="subtitle">Feel free to
                        <a href="mailto:vinay.a.lodha@gmail.com"> reach me</a> if you need cloud consultation service
                    </p>
                </div>
            </footer>
        );
    }
}