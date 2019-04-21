import React from 'react';
import axios from 'axios';
import ResumeProfil, {ImageProfil} from './ResumeProfil';
import './App.css';

export default class PageParametre extends React.Component{
	constructor(props){
		super(props);
		this.onSubmit=this.onSubmit.bind(this);
		this.onChange=this.onChange.bind(this);
	}
	
	onSubmit(event){
		let img=this.state.file;
		const formData = new FormData();
		formData.append('image',img)
		formData.append('key',this.props.infos.key);
		const config = {
			headers: {
            'content-type': 'multipart/form-data'
			}
		}
		axios.post(window.addressServer+"/upload", formData, config).then(rep => console.log(rep.data));
		event.preventDefault();
	}
	
	onChange(event) {
		this.setState({file:event.target.files[0]})
	}

	render(){
		return (
			<div className="corps">
				<div className="styleDeBase pageParmetre">
					<form className="formImageProfil" onSubmit={this.onSubmit}>
						<fieldset>
							<legend>Image de profil</legend>
							<ImageProfil className="imageParams" username={this.props.infos.username} />
							<input className="parcourirImage" type="file" onChange={this.onChange} accept="image/png"/>
							<input className="button basDroit" type="submit" value="Envoyer"/>
						</fieldset>
					</form>
				</div>
			</div>
		);
	}
}