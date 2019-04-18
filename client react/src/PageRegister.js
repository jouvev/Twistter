import React from 'react';
import axios from 'axios';
import './App.css';

export default class PageRegister extends React.Component{
	constructor(props){
		super(props);
		this.state = {name:"", firstName:"", email:"", username:"", password:"", passwordConfirm:"", error:""}
		this.onChangeName = this.onChangeName.bind(this);
		this.onChangeFirstName = this.onChangeFirstName.bind(this);
		this.onChangeEmail = this.onChangeEmail.bind(this);
		this.onChangeUsername = this.onChangeUsername.bind(this);
		this.onChangePassword = this.onChangePassword.bind(this);
		this.onChangePasswordConfirm = this.onChangePasswordConfirm.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
	}

	onChangeName(event){
		this.setState({name: event.target.value});
	}

	onChangeFirstName(event){
		this.setState({firstName: event.target.value});
	}

	onChangeEmail(event){
		this.setState({email: event.target.value});
	}

	onChangeUsername(event){
		this.setState({username: event.target.value});
	}

	onChangePassword(event){
		this.setState({password: event.target.value});
	}

	onChangePasswordConfirm(event){
		this.setState({passwordConfirm: event.target.value});
	}

	onSubmit(event){
		//si les mots de passes sont différents on fait une erreur
		if (this.state.name === "" || this.state.firstName === "" || this.state.username === "" || this.state.password === "" || this.state.email === ""){
			this.setState({error:"Champs invalides"});
		}
		else {
			if (this.state.password !== this.state.passwordConfirm){
				this.setState({error:"Les mots de passe ne correspondent pas !"});
			}
			else{
				const url = new URLSearchParams();
				url.append('name',this.state.name);
				url.append('firstName',this.state.firstName);
				url.append('email',this.state.email);
				url.append('username',this.state.username);
				url.append('password',this.state.password);
				//on créé l'utilisateur
				axios.get(window.addressServer + '/user/create?'+url).then(reponse => {
					if(reponse.data["Code"] === undefined){
						this.props.logout();
					}else{
						this.setState({error:reponse.data["Message"]});
					}
				});
			}
		}
		event.preventDefault();
	}

	render(){
		let error;
		if(this.state.error !== ""){
			error=<p className="erreur">{this.state.error}</p>
		}
		return (
			<div className="corps">
				<div className="register">
					<form className="formRegister" onSubmit={this.onSubmit}>
				  	{error}
					<h1 className="titre">Créer votre compte</h1>
						<fieldset>
							<legend>Vos renseignements</legend>
							<input className="champtext champGrandeTaille" type="text" value={this.state.name} onChange={this.onChangeName} placeholder="Nom" autoFocus /><br/>
							<input className="champtext champGrandeTaille" type="text" value={this.state.firstName} onChange={this.onChangeFirstName}  placeholder="Prénom"/> <br/>
							<input className="champtext champGrandeTaille" type="text" value={this.state.email} onChange={this.onChangeEmail}  placeholder="blabla@email.fr"/> <br/>
						</fieldset>
						<span className="barre"></span>
						<fieldset>
							<legend>Vos identifiants</legend>
							<input className="champtext champGrandeTaille" type="text" value={this.state.username} onChange={this.onChangeUsername} placeholder="Username"/><br/>
							<input className="champtext champGrandeTaille" type="password" value={this.state.password} onChange={this.onChangePassword}  placeholder="Password"/> <br/>
							<input className="champtext champGrandeTaille" type="password" value={this.state.passwordConfirm} onChange={this.onChangePasswordConfirm} placeholder="Confirm Password"/> <br/>
						</fieldset>
						<input className="button" type="submit" value="S'enregistrer" />
					</form>
				</div>
			</div>
		);
	}
}
