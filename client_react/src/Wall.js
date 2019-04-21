import React from 'react';
import axios from 'axios';
import ResumeProfil from './ResumeProfil';
import './App.css';

export default class Wall extends React.Component{
	constructor(props){
		super(props);
		this.state={listeMessages:[],error:"",dequi:this.props.dequi};
		if (this.props.idParent !== undefined)
			this.getReplies();
		else
			this.getMessages(this.state.dequi);
		this.update=this.update.bind(this);
	}

	componentWillReceiveProps(nextProps){
		if(nextProps.dequi !== this.props.dequi){
			this.setState({dequi:nextProps.dequi});
			this.getMessages(nextProps.dequi);
		}
	}

	getMessages(dequi){
		const url = new URLSearchParams();
		if(dequi!==undefined){
			url.append('username',dequi);
		}
		axios.get(window.addressServer + '/message/list?'+url).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({listeMessages:reponse.data['Messages']});
			}else{
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	getReplies(){
		axios.get(window.addressServer + '/message/list?idParent='+this.props.idParent).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({listeMessages:reponse.data['Messages']});
			}else{
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	update(){
		if (this.props.idParent !== undefined)
			this.getReplies();
		else
			this.getMessages(this.state.dequi);
	}

	render(){
		let error;
		if(this.state.error!==undefined){
			error=<p className="erreur">{this.state.error}</p>
		}
		return (
			<div className="styleDeBase wall">
				<FormTweester infos={this.props.infos} update={this.update} idParent={this.props.idParent} updateStats={this.props.updateStats}/>
				{error}
				{
					this.state.listeMessages.map((message,id)=>
						<div className="containerPost" key={id}>
							<Post infos={this.props.infos} id={message._id} contenu={message.message}
									auteur={message.auteur} date={message.date} setProfil={this.props.setProfil}
									update={this.update} likes={message.likes} updateStats={this.props.updateStats}/>
							<div className="barre"></div>
						</div>
					)
				}
			</div>
		);
  }
}

class FormTweester extends React.Component{
	constructor(props){
		super(props);
		this.state={message:""};
		this.onSubmit=this.onSubmit.bind(this);
		this.messageChange=this.messageChange.bind(this);
	}

	messageChange(event){
		this.setState({message: event.target.value});
	}

	onSubmit(event){
		const url = new URLSearchParams();
		url.append('message',this.state.message);
		url.append('key',this.props.infos.key);
		if (this.props.idParent !== undefined)
			url.append('idMessage',this.props.idParent);

		axios.get(window.addressServer + '/message/add?'+url).then(reponse => {
			if (this.props.updateStats !== undefined)
				this.props.updateStats();
			this.props.update();
			this.setState({message:""});
		});
		event.preventDefault();
	}

	render(){
		return (
			<div>
				<form className="formTweeste" onSubmit={this.onSubmit}>
					<textarea className="champtext champtextarea" placeholder="add message..." value={this.state.message} onChange={this.messageChange} ></textarea>
					<input className="button buttonTweester" type="submit" value="tweester" disabled={this.state.message===""}/>
				</form>
				<div className="barre"></div>
			</div>
		);
	}
}

class Post extends React.Component{
	constructor(props){
		super(props);
		this.state = {listeLikes:this.props.likes, replies:"hidden"};

		this.supprimerMessage=this.supprimerMessage.bind(this);
		this.likeMessage=this.likeMessage.bind(this);
	}

	componentWillReceiveProps(nextProps){
		if(nextProps.id !== this.props.id){
			this.setState({listeLikes:nextProps.likes});
		}
	}

	updateListeLikes(){
		//si il est dedans
		if (this.state.listeLikes.includes(this.props.infos.id)){
			this.state.listeLikes.pop(this.props.infos.id);
			this.setState({listeLikes: this.state.listeLikes})
		}
		else {
			this.state.listeLikes.push(this.props.infos.id);
			this.setState({listeLikes: this.state.listeLikes})
		}
	}

	likeMessage(){
		const url = new URLSearchParams();
		url.append('key',this.props.infos.key);
		url.append('idMessage',this.props.id);
		axios.get(window.addressServer + '/message/like?'+url).then(reponse => {
			if (this.props.updateStats !== undefined)
				this.props.updateStats();
			this.updateListeLikes();
		});
	}

	supprimerMessage(){
		const url = new URLSearchParams();
		url.append('idMessage',this.props.id);
		url.append('key',this.props.infos.key);
		axios.get(window.addressServer + '/message/delete?'+url).then(reponse => {
			if (this.props.updateStats !== undefined)
				this.props.updateStats();
			this.props.update();
		});
	}

	showReplies(){
		this.setState({replies:"visible"});
	}

	hideReplies(){
		this.setState({replies:"hidden"});
	}

	render(){
		let supprime;
		if(this.props.auteur === this.props.infos.username){
			supprime=<img className="supMessage" src="delete_message.png" onClick={this.supprimerMessage} alt="" />
		}
		let likeStatus = "like";
		if (this.state.listeLikes.includes(this.props.infos.id))
			likeStatus = "unlike";

		let replies;
		if (this.state.replies === 'visible')
			replies = (<Wall infos={this.props.infos} setProfil={this.props.setProfil} idParent={this.props.id} updateStats={this.props.updateStats}/>);

		return (
			<div className="post">
				{supprime}
				<ResumeProfil username={this.props.auteur} setProfil={this.props.setProfil}/>
				<p className="date">{this.props.date}</p>
				<p className="message">{this.props.contenu}</p>
				<div className={likeStatus} onClick={() => this.likeMessage()}><p className="centre">{this.state.listeLikes.length}</p></div>
				<img className={"iconButton replyButton "+ (this.state.replies === 'visible' ? "replyButtonActiv" : "")} alt="" src="fleche.png" onClick={() => (this.state.replies === 'hidden' ? this.showReplies() : this.hideReplies())}/>
				{replies}
			</div>
		);
	}
}
