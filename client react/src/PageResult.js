import React from 'react';
import ResumeProfil from './ResumeProfil';
import ListeTweests from './ListeTweests';
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
							<ResumeProfil  username={username} setProfil={this.props.setProfil}/>
						</div>
					)}
				</div>);
		}
		if(this.props.messages.length!==0){
			messages = (
				<ListeTweests setProfil={this.props.setProfil} messages={this.props.messages} />
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
