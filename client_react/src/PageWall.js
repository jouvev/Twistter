import React from 'react';
import axios from 'axios';
import ResumeProfil from './ResumeProfil';
import Wall from './Wall';
import {ContextConnection} from './ContextConnection';
import './App.css';

export default class PageWall extends React.Component{
	constructor(props){
		super(props);
		this.state = {nbLikes:0, nbTwists:0, error:""};
		this.updateStats = this.updateStats.bind(this);
		
	}
	
	componentDidMount(){
		this.updateStats();
	}

	updateStats(){
		axios.get(window.addressServer + '/user/stats?username=' + this.context.infos.username).then(reponse => {
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
				<Profil nbLikes={this.state.nbLikes} nbTwists={this.state.nbTwists}/>
				<Wall updateStats={this.updateStats}/>
			</div>
		);
	}
}
PageWall.contextType=ContextConnection;//abonnement au context


class Profil extends React.Component{
	render(){
		return (
			<div className="styleDeBase profilgauche">
				<ResumeProfil username={this.context.infos.username} />
				<div className="statistique">Tweests: {this.props.nbTwists} | Likes: {this.props.nbLikes}</div>
			</div>
		);
	}
}
Profil.contextType=ContextConnection;//abonnement au context