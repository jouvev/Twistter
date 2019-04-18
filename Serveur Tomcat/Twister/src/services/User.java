package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import services.tools.*;

public class User {
	
	public static JSONObject createUser(String name, String firstName, String username, String password, String email) {
		if(name==null || firstName==null || username == null || password==null || email==null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (UserTools.userExists(username))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_EXISTING);
			if (!(UserTools.insertUser(name, firstName, username, password, email)))
				return ErrorJSON.serviceRefused(Errors.ERROR_INSERTION_SQL);
		} 
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);} 
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject deleteUser(String key) {
		if(key == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!AuthentificationTools.keyExists(key))
				return ErrorJSON.serviceRefused(Errors.ERROR_INCORRECT_KEY);
			int idUser = UserTools.getIdUserByKey(key);
			if (!(UserTools.deleteUser(idUser)))
				return ErrorJSON.serviceRefused(Errors.ERROR_DELETE_SQL);
			MessageTools.deleteUserMessage(idUser);
		} 
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);	}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
		return ErrorJSON.serviceAccepted();
	}
	
	public static JSONObject getUser(String username) {
		if (username == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!UserTools.userExists(username))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);
			int idUser = UserTools.getIdUser(username);
			return UserTools.getUserById(idUser);

		} 
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);	}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
	}
	
	public static JSONObject searchUser(String pattern) {
		if (pattern == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			return UserTools.searchUser(pattern);
		} 
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);	}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
	}
	
	public static JSONObject statsUser(String username) {
		if (username == null)
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if (!UserTools.userExists(username))
				return ErrorJSON.serviceRefused(Errors.ERROR_USER_NOT_EXISTING);

			return UserTools.statsUser(username);

		} 
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);	}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(Errors.ERROR);}
	}
}
