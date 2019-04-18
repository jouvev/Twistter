import React from 'react';
import axios from 'axios';
import './App.css';
import NavigationPanel from './NavigationPanel';
import PageWall from './PageWall';
import PageLogin from './PageLogin';
import PageRegister from './PageRegister';
import PageProfil from './PageProfil';
import PageResult from './PageResult';

export default class MainPage extends React.Component{
	constructor(props){
		super(props);
		this.state = {isConnected:false, page:"login"}
		this.getConnected=this.getConnected.bind(this);
		this.setLogout=this.setLogout.bind(this);
		this.register=this.register.bind(this);
		this.accueil=this.accueil.bind(this);
		this.search=this.search.bind(this);
		this.setProfil=this.setProfil.bind(this);
	}

	search(dataUsers, dataMessages){
		this.setState({page:"result", users:dataUsers, messages:dataMessages});
	}

	setProfil(username){
		if(username===undefined){
			username=this.state.infos.username;
		}
		this.setState({page:"profil", dequi:username});
	}

	accueil(){
		this.setState({page:"wall"});
	}

	getConnected(key,id,username){
		this.setState({page:"wall", isConnected:true, infos:{key:key, id:id, username:username}});
	}

	setLogout(){
		if (this.state.infos === undefined)
			this.setState({page:"login", isConnected:false});
		else {
			axios.get(window.addressServer + '/authentification/logout?key='+this.state.infos.key).then(reponse => {
				this.setState({page:"login", isConnected:false});
			});
		}
	}

	register() {
		this.setState({page:"register"});
	}

	render(){
		let contenu;

		if(this.state.page === "login"){
	  		contenu=(
	  			<PageLogin register={this.register} login={this.getConnected} />
	  		);
		}

		if(this.state.page === "register"){
		  	contenu=(
		  		<PageRegister logout={this.setLogout}/>
		  	);
		}

		if(this.state.page === "wall"){
			contenu=(
				<PageWall infos={this.state.infos} setProfil={this.setProfil}/>
			);
		}

		if(this.state.page === "profil"){
			contenu=(
				<PageProfil infos={this.state.infos} dequi={this.state.dequi} setProfil={this.setProfil} logout={this.setLogout}/>
			);
		}

		if(this.state.page === "result"){
			contenu=(
				<PageResult users={this.state.users} setProfil={this.setProfil} messages={this.state.messages}/>
			);
		}

		return (
			<div>
				<NavigationPanel login={this.getConnected} logout={this.setLogout} isConnected={this.state.isConnected}
					page={this.state.page} register={this.register} setProfil={this.setProfil} accueil={this.accueil} search={this.search}/>
				{contenu}
			</div>
		);
	}
}
