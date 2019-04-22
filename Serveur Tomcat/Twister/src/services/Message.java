package services;

import java.sql.SQLException;
import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import services.tools.AuthentificationTools;
import services.tools.ErrorJSON;
import services.tools.Errors;
import services.tools.MapReduceTools;
import services.tools.MessageTools;
import services.tools.UserTools;

public class Message {
	
	public static JSONObject addMessage(String key, String message, String idMessage) {
		//idMessage peut etre null si on ajoute un poste (il vaut l'id d'un message parent si c'est un commentaire)
		if(key==null || message==null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			String username = UserTools.getUsernameByKey(key);
			MessageTools.insertMessage(username, message, idMessage);
			UserTools.updateTwistUser(username, 1);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject deleteMessage(String key, String idMessage) {
		if(key == null || idMessage == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			MessageTools.deleteMessage(idMessage);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject listMessages(String username, String idParent) {
		try {
			//afficher le mur général de l'application (juste les messages qui n'ont pas de parents... donc les messages racines)
			if(username==null && idParent==null)
				return MessageTools.listMessagesRacines();
			
			//afficher le mur d'une personne (ses posts)
			else if(username!=null && idParent==null) {
				if (!UserTools.userExists(username))
					return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
				return MessageTools.listMessagesUser(username);
			}
			
			//afficher la liste des réponses à un message (ceux qui ont pour parent: idMessage)
			else if (username==null && idParent!=null)
				return MessageTools.listMessagesReponses(idParent);
			
			else
				return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
	}
	
	public static JSONObject likeMessage(String key, String idMessage) {
		if(key == null || idMessage == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			int idUser = UserTools.getIdUserByKey(key);
			MessageTools.likeMessage(idUser, idMessage);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject searchMessage(String pattern) {
		if (pattern == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			return MapReduceTools.searchMessagesResults(pattern);
		}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
	}
	
	
	/**
	 * @return un json avec tous les message de la conversation au dessus de idMessage
	 */
	public static JSONObject getConversation(String idMessage) {
		if (idMessage == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			List<Document> messages = MessageTools.getConversation(idMessage);
			JSONObject res = new JSONObject();
			res.put("messages", messages);
			return res;
		}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
	}
}
