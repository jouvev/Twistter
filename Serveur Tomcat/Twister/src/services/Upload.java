package services;

import java.sql.SQLException;

import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import services.tools.ErrorJSON;
import services.tools.Errors;
import services.tools.UploadTools;

public class Upload {
	public static JSONObject uploadImage(Part part, String key) {
		if (key == null) 
			return ErrorJSON.serviceRefused(Errors.ERROR_ARGUMENT);
		try {
			if(UploadTools.uploadImage(part,key))
				return ErrorJSON.serviceAccepted();
			else
				return ErrorJSON.serviceRefused(new Errors("erreur"));
		}
		catch (SQLException e) {return ErrorJSON.serviceRefused(Errors.ERROR_SQL);}
		catch (JSONException e) {return ErrorJSON.serviceRefused(Errors.ERROR_JSON);}
		catch (Exception e) {return ErrorJSON.serviceRefused(new Errors(e.getMessage()));}
	}
}
