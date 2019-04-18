import React from 'react';
import './App.css';

export default class ResumeProfil extends React.Component{
	constructor(props){
		super(props);
		this.goToProfil= this.goToProfil.bind(this);
	}

	goToProfil(){
		this.props.setProfil(this.props.username);
	}

	render(){
		return(
			<div className="resumeProfil" onClick={this.goToProfil}>
				<img className="imgProfil" src="/photo.png" alt="" />
				<span>{this.props.username}</span>
			</div>
		);
	}
}
