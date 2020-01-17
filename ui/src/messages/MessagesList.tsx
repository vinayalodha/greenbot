import React, {Component} from "react";

class MessagesListProps {
    constructor(messages: Array<String>, type: string) {
        this.messages = messages;
        this.type = type;
    }

    messages: Array<String>;
    type: string
}


export class MessagesList extends Component<MessagesListProps, {}> {


    render() {
        debugger;
        if (this.props.messages.length == 0) {
            return null;
        }
        const items = []

        for (const message of this.props.messages) {
            items.push(<li>{message}</li>)
        }
        return (
            <article className={this.props.type}>
                <div className="message-body">
                    <ul>
                        {items}
                    </ul>
                </div>
            </article>

        );
    }
}
