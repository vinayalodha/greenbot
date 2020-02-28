import React, {Component} from 'react'; // let's also import Component

export class AboutComponent extends Component<{}> {

    render() {
        return (
            <div className="message">
                <div className="message-body">
                    Greenbot analyze AWS infrastructure and find ways to reduce costs. 
                </div>
            </div>
        );
    }
}