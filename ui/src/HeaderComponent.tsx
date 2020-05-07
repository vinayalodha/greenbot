/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React, { Component } from 'react';
import './css/HeaderComponent.css'


export class HeaderComponent extends Component<{}> {

	// render will know everything!
	render() {
		return (
			<div className="container">
				<nav role="navigation" className="navbar bd-navbar navbar has-shadow is-spaced" aria-label="main navigation">
					<div id="navbarBasicExample" className="navbar-menu">
						<div className="navbar-start">
							<a className="" href="/"> 
								<strong className="green-color">Greenbot</strong> 
								<img src="favicon.svg" alt="GreenBot"/>  
							</a> 
						</div>
						<div className="navbar-end">
							<div className="navbar-item">
							<div className="field is-grouped is-grouped-multiline">
							<p className="control">
            					<a className="button" rel="noopener noreferrer" target='_blank' href="https://vinay-lodha.gitbook.io/greenbot/">
              						<strong>Docs</strong>
            					</a>
          					</p>
							<p className="control">
            					<a className="button" rel="noopener noreferrer" target='_blank' href="https://github.com/vinay-lodha/greenbot">
              						<strong>Github <span role="img" aria-label="GreenBot">⭐</span></strong>
            					</a>
          					</p>
							<p className="control">
            					<a className="button is-primary" rel="noopener noreferrer" target='_blank' href="mailto:vinay.a.lodha@gmail.com">
              						<strong>Consulting</strong>
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