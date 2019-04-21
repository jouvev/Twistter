import React from 'react';
import './App.css';

export default class ResumeProfil extends React.Component{
	constructor(props){
		super(props);
		this.goToProfil= this.goToProfil.bind(this);
	}

	goToProfil(){
		this.props.setProfil(this.props.username);
	}

	render(){
		return(
			<div className="resumeProfil" onClick={this.goToProfil}>
				<ImageProfil className="imgProfil" username={this.props.username}/>
				<span>{this.props.username}</span>
			</div>
		);
	}
}

export class ImageProfil extends React.Component{
	constructor(props){
		super(props);
		
		const url = new URLSearchParams();
		url.append('username',this.props.username);
		this.state={img:window.addressServer+"/user/image?"+url};
	}
	
	componentWillReceiveProps(nextProps){
		if(nextProps.username !== this.props.username){
			const url = new URLSearchParams();
			url.append('username',nextProps.username);
			this.setState({img:window.addressServer+"/user/image?"+url});
		}
	}
	
	render(){
		return(
			<img className={this.props.className} src={this.state.img} alt="" />
		);
	}
}
