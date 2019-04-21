import React from 'react';
import axios from 'axios';
import './App.css';

export default class PageRechercheAvance extends React.Component{
	constructor(props){
		super(props);

		this.state={ from:"", to:"", popularite:"", error:""};
		this.onChangeFrom = this.onChangeFrom.bind(this);
		this.onChangeTo = this.onChangeTo.bind(this);
		this.onChangePopularite = this.onChangePopularite.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
	}

	onChangeFrom(event){
		this.setState({from: event.target.value});
	}

	onChangeTo(event){
		this.setState({to: event.target.value});
	}
	
	onChangePopularite(event){
		this.setState({popularite: event.target.value});
	}

  	onSubmit(event){
		if(this.state.from==="" || this.state.to===""  || this.state.popularite==="" ){
			this.setState({error : "Champs obligatoires"});
		}
  		const url = new URLSearchParams();
		url.append('from',this.state.from);
		url.append('to',this.state.to);
		url.append('popularite',this.state.popularite);
		
		axios.get(window.addressServer + '/search?'+url).then(reponse => {
				this.props.search([], reponse.data["messages"]);
		});
		event.preventDefault();
  	}

	render(){
		let error;
		if(this.state.error !== ""){
			error =(<p className="erreur" >{this.state.error}</p>);
		}
		return (
			<div className="corps centre">
				<form className="formRechercheAvance" onSubmit={this.onSubmit}>
					{error}
					<h1 className="titre">Recherche Avancée de messages</h1>
					<fieldset>
						<legend>Date</legend>
						<input className="champtext champGrandeTaille" type="text" value={this.state.from} onChange={this.onChangeFrom} placeholder="From" autoFocus/><br/>
						<input className="champtext champGrandeTaille" type="text" value={this.state.to} onChange={this.onChangeTo} placeholder="To"/> <br/>
					</fieldset>
					<fieldset>
						<legend>Popularité</legend>
						<input className="champtext champGrandeTaille" type="text" value={this.state.popularite} onChange={this.onChangePopularite} placeholder="Nombre d'amis minimum"/> <br/>
					</fieldset>
					<input className="button" type="submit" value="Rechercher" />
				</form>
			</div>
		);
	}
}
