import React from 'react';
import ResumeProfil from './ResumeProfil';
import './App.css';

export default class ListeTweests extends React.Component{
	render(){
		return (
			<div className="styleDeBase">
			{
				this.props.messages.map((message,id) =>
					<div className="containerPost" key={id}>
						<div className="post">
							<ResumeProfil username={message.auteur} setProfil={this.props.setProfil}/>
							<p className="date">{message.date}</p>
							<p className="message">{message.message}</p>
						</div>
						<div className="barre100"></div>
					</div>
				)
			}
			</div>
		);
	}
}
