import React, {Component} from "react";

class MessagesListComponentProps {
    messages: String[] = [];
    type!: string
}


export class MessagesListComponent extends Component<MessagesListComponentProps, {}> {

    render() {
        if (this.props.messages.length === 0) {
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
