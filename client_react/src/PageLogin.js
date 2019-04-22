import React from 'react';
import axios from 'axios';
import {ContextConnection} from './ContextConnection';
import './App.css';

export default class PageLogin extends React.Component{
	constructor(props){
		super(props);

		this.state={ username:"", password:"", error:""};
		this.onChangeUsername = this.onChangeUsername.bind(this);
		this.onChangePassword = this.onChangePassword.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
	}

	onChangeUsername(event){
		this.setState({username: event.target.value});
	}

	onChangePassword(event){
		this.setState({password: event.target.value});
	}

  	onSubmit(event){
		if (this.state.username === "" || this.state.password === ""){
			this.setState({error:"Champs invalides"});
		}
		else {
	  		const url = new URLSearchParams();
			url.append('username',this.state.username);
			url.append('password',this.state.password);
			
			axios.get(window.addressServer + '/authentification/login?'+url).then(reponse => {	
				if(reponse.data["Code"]===undefined){
					this.context.getConnected(reponse.data["key"],reponse.data["id"],reponse.data["username"]);
				}else{
					this.setState({error : reponse.data["Message"]});
				}
			}).catch(rep => {console.log(rep);} );
		}
		event.preventDefault();
  	}

	render(){
		let error;
		if(this.state.error!==""){
			error=<p className="erreur">{this.state.error}</p>
		}
		return (
			<div className="corps">
				<div className="login">

					<form className="formLogin" onSubmit={this.onSubmit}>
						{error}
						<fieldset>
							<input className="champtext" type="text" value={this.state.username} onChange={this.onChangeUsername} placeholder="Username" autoFocus/><br/>
							<input className="champtext" type="password" value={this.state.password} onChange={this.onChangePassword} placeholder="Password"/> <br/>
							<p className="link" onClick={()=>this.context.register()} >Pas encore de compte ?</p>
						</fieldset>
						<input className="button" type="submit" value="Se connecter" />
					</form>
				</div>
			</div>
		);
	}
}
PageLogin.contextType=ContextConnection;//abonnement au context