import React from 'react';
import axios from 'axios';
import './App.css';
import NavigationPanel from './NavigationPanel';
import PageWall from './PageWall';
import PageLogin from './PageLogin';
import PageRegister from './PageRegister';
import PageProfil from './PageProfil';
import PageResult from './PageResult';
import PageParametre from './PageParametre';
import PageRechercheAvance from './PageRechercheAvance';
import {ContextConnection} from './ContextConnection';

export default class MainPage extends React.Component{
	constructor(props){
		super(props);
		if(sessionStorage.getItem('isConnected')){
			this.state = {isConnected: true, page:"wall", infos : {key:sessionStorage.getItem('key'), id:parseInt(sessionStorage.getItem('id'),10), username:sessionStorage.getItem('username')}};
		}else{
			this.state = {isConnected:false, page:"login" };
		}
		
		this.getConnected=this.getConnected.bind(this);
		this.setLogout=this.setLogout.bind(this);
		this.register=this.register.bind(this);
		this.accueil=this.accueil.bind(this);
		this.search=this.search.bind(this);
		this.setProfil=this.setProfil.bind(this);
		this.searchavance=this.searchavance.bind(this);
		this.parametre=this.parametre.bind(this);
		
		let func = {
			getConnected: this.getConnected,
			setLogout : this.setLogout,
			register : this.register,
			accueil : this.accueil,
			search : this.search,
			setProfil : this.setProfil,
			searchavance : this.searchavance,
			parametre : this.parametre
		};
		
		this.state = Object.assign({}, this.state, func); //concat toutes les fonctions
	}
	
	parametre(){
		this.setState({page:"parametre"});
	}
	
	searchavance(){
		this.setState({page:"searchavance"});
	}

	search(dataUsers, dataMessages){
		this.listeUsers = dataUsers;
		this.listeMessages = dataMessages;
		this.setState({page:"result"});	
	}

	setProfil(username){
		if(username===undefined){
			username=this.state.infos.username;
		}
		this.setState({page:"profil"});
		this.dequi=username;
	}

	accueil(){
		this.setState({page:"wall"});
	}

	getConnected(key,id,username){
		this.setState({page:"wall", isConnected:true, infos:{key:key, id:id, username:username}});
		sessionStorage.setItem('isConnected', "true");
		sessionStorage.setItem('key', key);
		sessionStorage.setItem('id', id);
		sessionStorage.setItem('username', username);
	}

	setLogout(){
		if (this.state.infos === undefined)
			this.setState({page:"login", isConnected:false});
		else {
			axios.get(window.addressServer + '/authentification/logout?key='+this.state.infos.key).then(reponse => {
				this.setState({page:"login", isConnected:false});
			});
		}
		sessionStorage.clear();
	}

	register() {
		this.setState({page:"register"});
	}

	render(){
		let contenu;

		if(this.state.page === "login"){
	  		contenu=(
	  			<PageLogin />
	  		);
		}

		if(this.state.page === "register"){
		  	contenu=(
		  		<PageRegister />
		  	);
		}

		if(this.state.page === "wall"){
			contenu=(
				<PageWall />
			);
		}

		if(this.state.page === "profil"){
			contenu=(
				<PageProfil dequi={this.dequi}/>
			);
		}

		if(this.state.page === "result"){
			contenu=(
				<PageResult users={this.listeUsers} messages={this.listeMessages}/>
			);
		}
			
		if(this.state.page === "searchavance"){
			contenu=(
				<PageRechercheAvance />
			);
		}
		
		if(this.state.page === "parametre"){
			contenu=(
				<PageParametre />
			);
		}
			
		return (
			<div>
				<ContextConnection.Provider value={this.state}>
					<NavigationPanel />
					{contenu}
				</ContextConnection.Provider>
			</div>
		);
	}
}
