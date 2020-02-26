import React, {Component} from 'react'; // let's also import Component

export class FooterComponent extends Component<{}> {

    render() {
        return (
            <footer className="footer">
                <div className="content has-text-centered">
                    <p className="subtitle"><strong>Build by <a href="https://www.linkedin.com/in/vinaylodha/" target='_blank' rel="noopener noreferrer">Vinay</a></strong>
                    </p>
                </div>
            </footer>
        );
    }
}