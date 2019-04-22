import React from 'react';
import axios from 'axios';
import {ContextConnection} from './ContextConnection';
import './App.css';

export default class NavigationPanel extends React.Component{
  	render(){
		let contenu;
		let menu;
		let logo;
		let context = this.context;
        //s'il n'est pas connecté
		if(context.isConnected===false){
			logo=(
				<img className="logo" src="/logo.png" alt="" onClick={() => context.setLogout()}/>
			);
		  	contenu = (
				<div>
			  		<button className="button buttonNavPanel" onClick={()=>context.setLogout()}>Log in</button>
			  	</div>
		  	);
		}
        //s'il est connecté
		else{
			logo=(
				<img className="logo" src="/logo.png" alt="" onClick={() => context.accueil()}/>
			);
		    contenu = (
				<div>
			   		<button className="button buttonNavPanel" onClick={()=>context.setLogout()}>Log Out</button>
			   	</div>
		   	);
			menu = <Menu />;
		}

		if(context.page==="login"){
			contenu=(
				<div>
					<button className="button buttonNavPanel" onClick={()=>context.register()}>Sign up</button>
				</div>
			);
		}

		return (
			
			<nav className="navPanel">
				{logo}
				{menu}
				{contenu}
			</nav>
		);
  	}
}
NavigationPanel.contextType=ContextConnection;//abonnement au context

class Menu extends React.Component{
	render(){
		let context = this.context;
		return (
			<div className="menu">
				<ul className="listeHorizontal">
				<li className="item" onClick={() => context.accueil()}>Accueil</li>
				<li className="separator"></li>
				<li className="item" onClick={() => context.setProfil()}>Profil</li>
				</ul>
				<span className="rechercheAvanceButton" description="Recherche Avancée"><img  className="iconButton" src="loupe.png" onClick={() => context.searchavance()} alt=""/></span>
				<SearchBar />
			</div>
		);
  	}
}
Menu.contextType=ContextConnection;//abonnement au context


class SearchBar extends React.Component{
	constructor(props){
		super(props);
		this.state={pattern:""};
		this.onSubmit=this.onSubmit.bind(this);
		this.onChangeSearch=this.onChangeSearch.bind(this);
		
	}

	onSubmit(event){
		const url = new URLSearchParams();
		url.append('pattern',this.state.pattern);

        axios.all([
            axios.get(window.addressServer + '/user/search?'+url),
            axios.get(window.addressServer + '/message/search?'+url)
        ]).then(axios.spread((reponseUsers, reponseMessages) => {
            this.context.search(reponseUsers.data["Users"], reponseMessages.data["Messages"])
        }))

		event.preventDefault();
	}

	onChangeSearch(event){
		this.setState({pattern: event.target.value});
	}

	render(){
		return (
			<form className="droite formSearch" onSubmit={this.onSubmit}>
				<input className="champtext champSearch" type="text" placeholder="Search" onChange={this.onChangeSearch}/>
			</form>
		);
	}
}
SearchBar.contextType=ContextConnection;//abonnement au context
