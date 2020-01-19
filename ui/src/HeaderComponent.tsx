import React, { Component } from 'react';


export class HeaderComponent extends Component<{}> {

	// render will know everything!
	render() {
		return (
			<div className="container">
				<nav className="navbar" role="navigation" aria-label="main navigation">
					<div id="navbarBasicExample" className="navbar-menu">
						<div className="navbar-start">
							<a className="navbar-item" href="/"> Greenbot 🤖 </a> <a className="navbar-item">
								Documentation </a>

							<div className="navbar-item has-dropdown is-hoverable">
								<a className="navbar-link"> More </a>

								<div className="navbar-dropdown">
									<a className="navbar-item"> About </a> <a className="navbar-item">
										Jobs </a>
									<a className="navbar-item"> Contact </a>
									<hr className="navbar-divider"></hr>
									<a className="navbar-item"> Report an issue </a>
								</div>
							</div>
						</div>
					</div>
				</nav>
			</div>
		);
	}
}