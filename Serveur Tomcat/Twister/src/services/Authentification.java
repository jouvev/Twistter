package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import services.tools.*;

public class Authentification {
	
	public static JSONObject login(String username, String password) {
		if (username == null || password == null) 
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!UserTools.userExists(username))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
			if (!(AuthentificationTools.checkPassword(username, password)))
				return ErrorJSON.serviceRefused(Errors.ERROR_WRONG_PASSWORD);
			int idUser = UserTools.getIdUser(username);
			String key = Tools.generateKey(64);
			if (! AuthentificationTools.insertKey(idUser, key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INSERTION_SQL);
			JSONObject ret = new JSONObject();
			ret.put("id", idUser);
			ret.put("username", username);
			ret.put("key", key);
			return ret;
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
	}
	
	
	
	public static JSONObject logout(String key) {
		if (key == null) 
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			//s'occuper de verifier la validite de la date et du boolean
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			if (!(AuthentificationTools.invalidatingKey(key)))
				return ErrorJSON.serviceRefused(Errors.ERROR_UPDATE_SQL);
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
		return ErrorJSON.serviceAccepted();
	}
}
