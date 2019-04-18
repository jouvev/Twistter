package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import services.tools.*;

public class Friend {
	
	public static JSONObject addFriend(String key, String username) {
		
		if(key==null || username==null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			if (!(UserTools.userExists(username)))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
			int idUser1 = UserTools.getIdUserByKey(key);
			int idUser2 = UserTools.getIdUser(username);
			if (FriendTools.alreadyFriend(idUser1, idUser2))
				return ErrorJSON.serviceRefused(Errors.ERROR_ALREADY_FRIEND);
			if (!(FriendTools.insertFriend(idUser1, idUser2)))
				return ErrorJSON.serviceRefused(Errors.ERROR_INSERTION_SQL);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject deleteFriend(String key, String username) {
		if(key == null || username == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			if (!(UserTools.userExists(username)))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
			int idUser1 = UserTools.getIdUserByKey(key);
			int idUser2 = UserTools.getIdUser(username);
			if (!FriendTools.alreadyFriend(idUser1, idUser2))
				return ErrorJSON.serviceRefused(Errors.ERROR_NOT_FRIEND);
			if (!(FriendTools.deleteFriend(idUser1, idUser2)))
				return ErrorJSON.serviceRefused(Errors.ERROR_DELETE_SQL);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject listFriends(String username) {
		if(username == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!(UserTools.userExists(username)))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
			int idUser1 = UserTools.getIdUser(username);
			return FriendTools.listFriends(idUser1);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
	}
	
}
