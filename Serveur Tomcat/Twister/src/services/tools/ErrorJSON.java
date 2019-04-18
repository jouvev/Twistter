package services.tools;
//mettre dans package tools

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJSON {
	
	public static JSONObject serviceRefused(services.tools.Errors e) {
		JSONObject res = new JSONObject();
		try {
			res.put("Code", Integer.toString(e.getCode()));
			res.put("Message", e.getMessage());
		} catch (JSONException expt) {expt.printStackTrace();}
		return res;
	}

	public static JSONObject serviceAccepted() {
		JSONObject res = new JSONObject();
		return res;
	}
	
}
