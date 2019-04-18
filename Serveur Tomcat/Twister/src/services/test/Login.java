package services.test;

import org.json.JSONObject;
import services.*;

public class Login {

	public static void main(String[] args) {
		
		JSONObject o = Authentification.login("ToLluvo", "12345");
		//JSONObject o1 = Authentification.login("Skitry", "mauvaismdp");
		JSONObject o2 = Authentification.login(null, "azerty123");
		
		System.out.println(o.toString());
		//System.out.println(o1.toString());
		System.out.println(o2.toString());
	}

}
