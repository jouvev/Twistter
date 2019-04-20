package services.tools;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJSON {
	
	/**
	 * Construit un json à partir de l'erreur rencontrer pendant un service
	 * @param erreur
	 * @return le json de l'erreur
	 */
	public static JSONObject serviceRefused(services.tools.Errors erreur) {
		JSONObject res = new JSONObject();
		try {
			res.put("Code", Integer.toString(erreur.getCode()));
			res.put("Message", erreur.getMessage());
		} catch (JSONException expt) {expt.printStackTrace();}
		return res;
	}

	/**
	 * retour d'un service lorsque qu'il ne produit pas d'erreur
	 * @return un json vide 
	 */
	public static JSONObject serviceAccepted() {
		JSONObject res = new JSONObject();
		return res;
	}
	
}
