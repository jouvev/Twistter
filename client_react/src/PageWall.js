import React from 'react';
import axios from 'axios';
import ResumeProfil from './ResumeProfil';
import Wall from './Wall';
import './App.css';

export default class PageWall extends React.Component{
	constructor(props){
		super(props);
		this.state = {nbLikes:0, nbTwists:0, error:""};
		this.updateStats = this.updateStats.bind(this);
		this.updateStats();
	}

	updateStats(){
		axios.get(window.addressServer + '/user/stats?username=' + this.props.infos.username).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({nbLikes:reponse.data["nbLikes"], nbTwists:reponse.data["nbTwists"], error:""});
			}else{
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	render(){
		let error;
		if(this.state.error!==""){
			error=<p className="erreur">{this.state.error}</p>
		}
		return (
			<div className="corps">
				{error}
				<Profil infos={this.props.infos} setProfil={this.props.setProfil} nbLikes={this.state.nbLikes} nbTwists={this.state.nbTwists}/>
				<Wall infos={this.props.infos} setProfil={this.props.setProfil} updateStats={this.updateStats}/>
			</div>
		);
	}
}


class Profil extends React.Component{
	render(){
		return (
			<div className="styleDeBase profilgauche">
				<ResumeProfil username={this.props.infos.username} setProfil={this.props.setProfil}/>
				<div className="statistique">Twists: {this.props.nbTwists} | Likes: {this.props.nbLikes}</div>
			</div>
		);
	}
}
