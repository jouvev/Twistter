import React from 'react';
import ResumeProfil from './ResumeProfil';
import ListeTweests from './ListeTweests';
import {ContextConnection} from './ContextConnection';
import './App.css';

export default class PageResult extends React.Component{
	render(){
		let users;
		let messages;
		if(this.props.users.length!==0){
			users = (
				<div className="styleDeBase">
					{this.props.users.map((username,id)=>
						<div key={id}>
							<ResumeProfil  username={username} />
						</div>
					)}
				</div>);
		}
		if(this.props.messages.length!==0){
			messages = (
				<ListeTweests setProfil={this.context.setProfil} messages={this.props.messages} />
			);
		}
		return (
			<div className="corps">
				{users}	
				<div className="spacerVertical"></div>
				{messages}
			</div>
		);
	}
}
PageResult.contextType=ContextConnection;//abonnement au context