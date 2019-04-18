package services.test;

import org.json.JSONObject;

import services.Recherche;

public class RechercheAvance {

	public static void main(String[] args) {
		JSONObject o = Recherche.rechercheAvance("16/04/2019", "17/04/2019",1);
		System.out.println(o);
	}

}
