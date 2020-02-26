import React, { Component } from 'react';


export class HeaderComponent extends Component<{}> {

	// render will know everything!
	render() {
		return (
			<div className="container">
				<nav role="navigation" className="navbar bd-navbar navbar has-shadow is-spaced" aria-label="main navigation">
					<div id="navbarBasicExample" className="navbar-menu">
						<div className="navbar-start">
							<a className="navbar-item" href="/"> Greenbot <span>🤖</span> </a> 
						</div>
						<div className="navbar-end">
							<div className="navbar-item">
							<div className="field is-grouped is-grouped-multiline">
							<p className="control">
            					<a className="button" rel="noopener noreferrer" target='_blank'  href="https://github.com/vinay-lodha/greenbot/issues">
              						<strong>Report Bug</strong>
            					</a>
          					</p>
							<p className="control">
            					<a className="button" rel="noopener noreferrer" target='_blank' href="https://github.com/vinay-lodha/greenbot">
              						<strong>Github <span>⭐</span></strong>
            					</a>
          					</p>
							<p className="control">
            					<a className="button is-primary" rel="noopener noreferrer" target='_blank' href="mailto:vinay.a.lodha@gmail.com">
              						<strong>Cloud consulting</strong>
            					</a>
          					</p>
							</div>
							</div>
						</div>
					</div>
				</nav>
			</div>
		);
	}
}