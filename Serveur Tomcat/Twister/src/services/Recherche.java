package services;

import java.sql.SQLException;
import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import services.tools.ErrorJSON;
import services.tools.Errors;
import services.tools.RechercheTools;

public class Recherche {
	
	public static JSONObject rechercheAvance(String from, String to, Integer popularite) {
		if (from == null || to == null || popularite == null) 
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			List<Document> messages = RechercheTools.rechercheAvance(from,to,popularite);
			JSONObject ret = new JSONObject();
			ret.put("messages", messages);
			return ret;
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
	}
}
