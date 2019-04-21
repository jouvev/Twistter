import React from 'react';
import axios from 'axios';
import ResumeProfil, {ImageProfil} from './ResumeProfil';
import Wall from './Wall';
import './App.css';

export default class PageProfil extends React.Component{
	constructor(props){
		super(props);
		this.state = {dequi: this.props.dequi};
	}

	componentWillReceiveProps(nextProps){
		if(nextProps.dequi !== this.state.dequi)
			this.setState({dequi: nextProps.dequi});
	}

	render(){
		return (
			<div className="corps">
				<ListeFriends  infos={this.props.infos} username={this.state.dequi} setProfil={this.props.setProfil} />
				<div className="gauche contenerCentrale">
					<DescriptionProfil username={this.state.dequi} infos={this.props.infos} logout={this.props.logout} parametre={this.props.parametre}/>
					<Wall infos={this.props.infos} setProfil={this.props.setProfil} dequi={this.state.dequi}/>
				</div>
			</div>
		);
	}
}


class DescriptionProfil extends React.Component{
	constructor(props){
		super(props);
		this.state = {name:"", firstName:"", username:"", email:"", error:"", listeAmis:[]};
		this.updateListAmis();
	}
	
	componentDidMount(){
		this.getProfil(this.props.username);
	}

	componentWillReceiveProps(nextProps){
		//on compare les 2 username en paramètre (les 'dequi')
		if(nextProps.username !== this.props.username)
			this.getProfil(nextProps.username);
	}

	getProfil(username){
		axios.get(window.addressServer + '/user/get?username=' + username).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({name:reponse.data["name"], firstName:reponse.data["firstName"], username:reponse.data["username"], email:reponse.data["email"], error:""});
			}else{
				this.setState({username:"", error:reponse.data["Message"]});
			}
		});
	}

	updateListAmis(){
		axios.get( window.addressServer + '/friend/list?username=' + this.props.infos.username).then(reponse => {
			this.setState({listeAmis:reponse.data["Friends"]});
		});
	}

	addFriend(){
		const url = new URLSearchParams();
		url.append('key',this.props.infos.key);
		url.append('username',this.state.username);
		axios.get(window.addressServer + '/friend/add?' + url).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.updateListAmis();
			} else {
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	deleteFriend(){
		const url = new URLSearchParams();
		url.append('key',this.props.infos.key);
		url.append('username',this.state.username);
		axios.get(window.addressServer + '/friend/delete?' + url).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.updateListAmis();
			} else {
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	deleteAccount(){
		axios.get(window.addressServer + '/user/delete?key=' + this.props.infos.key).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.props.logout();
			} else {
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	render(){
		let error;
		if(this.state.error!==""){
			error=<p className="erreur">{this.state.error}</p>
		}
		let parametre;
		if(this.state.username === this.props.infos.username){
			parametre=(
				<img className="iconButton parametre" src="parametre.png" alt="" onClick={() => this.props.parametre()}/>
			);
		}
		let friendButton;
		if (this.state.username !== "" && this.state.username !== this.props.infos.username){
			if(this.state.listeAmis.includes(this.state.username)){
				friendButton = (<button className="button addFriend" onClick={() => this.deleteFriend()}><img className="iconButton" src="delete_friend_icon.png" alt="" /></button>); //ils sont amis
			} else {
				friendButton = (<button className="button addFriend" onClick={() => this.addFriend()}><img className="iconButton" src="add_friend_icon.png" alt="" /></button>); //ils ne sont pas amis
			}
		}

		let deleteButton;
		if (this.state.username === this.props.infos.username){
			deleteButton = (<button className="button supUser" onClick={() => this.deleteAccount()}>SUPPRIMER LE COMPTE</button>);
		}
		return (
			<div className="styleDeBase descriptionProfil">
				{error}
				{friendButton}
				{parametre}
				<ImageProfil className="imgProfil" username={this.state.username} /><br/>
				<span>{this.state.username}</span><br/>
				<span>{this.state.firstName} {this.state.name}</span><br/>
				<span>{this.state.email}</span><br/>
				{deleteButton}
			</div>
		);
	}
}



class ListeFriends extends React.Component{
	constructor(props){
		super(props);
		this.state = {listeFriends:[], error:""};
		this.getListeFriends(this.props.username);
	}

	componentWillReceiveProps(nextProps){
		if(nextProps.username !== this.props.username)
			this.getListeFriends(nextProps.username);
	}

	getListeFriends(username){
		axios.get(window.addressServer + '/friend/list?username='+username).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({listeFriends:reponse.data["Friends"], error:""});
			}else{
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	render(){
		let error;
		if(this.state.error!=="")
			error=<p className="erreur">{this.state.error}</p>
		return (
			<div className="styleDeBase listeFriends">
				{error}
				<p >{this.props.infos.username===this.props.username ? 'Mes Amis :' : 'Ses Amis :'}</p>
				{this.state.listeFriends.map((username, id) => <Friend key={id} username={username} setProfil={this.props.setProfil} />)}
			</div>
		);
	}
}

class Friend extends React.Component{
	render(){
		return (
			<ResumeProfil username={this.props.username} setProfil={this.props.setProfil}/>
		);
	}
}
