import React from 'react';
import axios from 'axios';
import './App.css';

export default class NavigationPanel extends React.Component{
  	render(){
		let contenu;
		let menu;
		let logo;
        //s'il n'est pas connecté
		if(this.props.isConnected===false){
			logo=(
				<img className="logo" src="/logo.png" alt="" onClick={() => this.props.logout()}/>
			);
		  	contenu = (
				<div>
			  		<button className="button buttonNavPanel" onClick={()=>this.props.logout()}>Log in</button>
			  	</div>
		  	);
		}
        //s'il est connecté
		else{
			logo=(
				<img className="logo" src="/logo.png" alt="" onClick={() => this.props.accueil()}/>
			);
		    contenu = (
				<div>
			   		<button className="button buttonNavPanel" onClick={()=>this.props.logout()}>Log Out</button>
			   	</div>
		   	);
			menu = <Menu accueil={this.props.accueil} setProfil={this.props.setProfil} search={this.props.search} searchavance={this.props.searchavance}/>;
		}

		if(this.props.page==="login"){
			contenu=(
				<div>
					<button className="button buttonNavPanel" onClick={()=>this.props.register()}>Sign up</button>
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

class Menu extends React.Component{
	render(){
		return (
			<div className="menu">
				<ul className="listeHorizontal">
				<li className="item" onClick={() => this.props.accueil()}>Accueil</li>
				<li className="separator"></li>
				<li className="item" onClick={() => this.props.setProfil()}>Profil</li>
				</ul>
				<span className="rechercheAvanceButton" description="Recherche Avancée"><img  className="iconButton" src="loupe.png" onClick={() => this.props.searchavance()} alt=""/></span>
				<SearchBar search={this.props.search}/>
			</div>
		);
  	}
}


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
            this.props.search(reponseUsers.data["Users"], reponseMessages.data["Messages"])
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
