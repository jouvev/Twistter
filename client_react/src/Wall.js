import React from 'react';
import axios from 'axios';
import ResumeProfil from './ResumeProfil';
import {ContextConnection} from './ContextConnection';
import './App.css';

export default class Wall extends React.Component{
	constructor(props){
		super(props);
		this.state={listeMessages:[],error:"",dequi:this.props.dequi};
		
		this.update=this.update.bind(this);
	}
	
	componentDidMount(){
		if (this.props.idParent !== undefined)
			this.getReplies();
		else
			this.getMessages(this.state.dequi);
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
		if (this.props.updateStats !== undefined)
			this.props.updateStats();
		if (this.props.idParent !== undefined){
			this.props.updatenbrep();//fonction doit etre donner si idparent existe
			this.getReplies();
		}
		else
			this.getMessages(this.state.dequi);
	}

	render(){
		let error;
		let boolTweester = (this.state.dequi===undefined || (this.state.dequi === this.context.infos.username));//bool pour savoir si on affiche le formTweester
		if(this.state.error!==undefined){
			error=<p className="erreur">{this.state.error}</p>
		}
		return (
			<div className="styleDeBase wall">
			{boolTweester && <FormTweester idParent={this.props.idParent} update={this.update}/>}
				{error}
				{
					this.state.listeMessages.map((message,id)=>
						<div className="containerPost" key={id}>
							<Post  id={message._id} contenu={message.message}
									auteur={message.auteur} date={message.date} 
									update={this.update} likes={message.likes} updateStats={this.props.updateStats} estReponse={message.parent!==null}/>
							<div className="barre"></div>
						</div>
					)
				}
			</div>
		);
  }
}
Wall.contextType=ContextConnection;//abonnement au context

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
		url.append('key',this.context.infos.key);
		if (this.props.idParent !== undefined)
			url.append('idMessage',this.props.idParent);

		axios.get(window.addressServer + '/message/add?'+url).then(reponse => {
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
FormTweester.contextType=ContextConnection;//abonnement au context

class Post extends React.Component{
	constructor(props){
		super(props);
		this.state = {idMessage:this.props.id, listeLikes:this.props.likes, replies:"hidden" , nbrep:0, estReponse:this.props.estReponse};

		this.supprimerMessage=this.supprimerMessage.bind(this);
		this.likeMessage=this.likeMessage.bind(this);
		this.updatenbrep=this.updatenbrep.bind(this);
		this.voirReponse=this.voirReponse.bind(this);
	}

	componentDidMount(){
		this.updatenbrep();
	}
	
	componentWillReceiveProps(nextProps){
		if(nextProps.id !== this.props.id){//quand on ajoute un message c'est ici qu'on doit bien faire l'update
			this.setState({idMessage:nextProps.id, listeLikes:nextProps.likes, replies:"hidden", estReponse: nextProps.estReponse});
			this.updatenbrep(nextProps.id);
		}
	}

	updateListeLikes(){
		//si il est dedans
		if (this.state.listeLikes.includes(this.context.infos.id)){
			this.state.listeLikes.pop(this.context.infos.id);
			this.setState({listeLikes: this.state.listeLikes})
		}
		else {
			this.state.listeLikes.push(this.context.infos.id);
			this.setState({listeLikes: this.state.listeLikes})
		}
	}

	likeMessage(){
		const url = new URLSearchParams();
		url.append('key',this.context.infos.key);
		url.append('idMessage',this.state.idMessage);
		axios.get(window.addressServer + '/message/like?'+url).then(reponse => {
			this.props.update();
			this.updateListeLikes();
		});
	}

	supprimerMessage(){
		const url = new URLSearchParams();
		url.append('idMessage',this.state.idMessage);
		url.append('key',this.context.infos.key);
		axios.get(window.addressServer + '/message/delete?'+url).then(reponse => {
			this.props.update();
		});
	}
	
	updatenbrep(id){
		if(id===undefined)
			id=this.state.idMessage;
		axios.get(window.addressServer + '/message/list?idParent='+id).then(reponse => {
			if(reponse.data["Code"]===undefined){
				this.setState({nbrep:reponse.data['Messages'].length});
			}else{
				this.setState({error:reponse.data["Message"]});
			}
		});
	}

	showReplies(){
		this.setState({replies:"visible"});
	}

	hideReplies(){
		this.setState({replies:"hidden"});
	}
	
	voirReponse(){
		const url = new URLSearchParams();
		url.append('idMessage',this.state.idMessage);
		axios.get(window.addressServer + '/message/conversation?'+url).then(reponse => {
			this.context.search([],reponse.data["messages"]);
		});
		
	}

	render(){
		let supprime;
		if(this.props.auteur === this.context.infos.username){
			supprime=<img className="supMessage" src="delete_message.png" onClick={this.supprimerMessage} alt="" />
		}
		let likeStatus = "like";
		if (this.state.listeLikes.includes(this.context.infos.id)){
			likeStatus = "unlike";
		}
		
		let replies;
		if (this.state.replies === 'visible')
			replies = (<Wall idParent={this.state.idMessage} updateStats={this.props.updateStats} updatenbrep={this.updatenbrep}/>);

		return (
			<div className="post">
				{supprime}
				<ResumeProfil username={this.props.auteur}/>
				
				<p className="date">{this.props.date}{this.state.estReponse && <span className="estReponse" onClick={this.voirReponse}> - est une réponse</span>}</p>
				<p className="message">{this.props.contenu}</p>
				<div className={likeStatus} onClick={() => this.likeMessage()}><p className="centre">{this.state.listeLikes.length}</p></div>
				<div className="compteurRep">
					<img className={"iconButton replyButton "+ (this.state.replies === 'visible' ? "replyButtonActiv" : "")} alt="" src="fleche.png" onClick={() => (this.state.replies === 'hidden' ? this.showReplies() : this.hideReplies())}/>
					{this.state.nbrep}
				</div>
				{replies}
			</div>
		);
	}
}
Post.contextType=ContextConnection;//abonnement au context