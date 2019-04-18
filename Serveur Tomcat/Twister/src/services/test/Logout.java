package services.test;

import org.json.JSONObject;
import services.*;

public class Logout {

	public static void main(String[] args) {
		
		JSONObject o = Authentification.logout("OjpxQKfISY1WJeWcWMecr5MAMiug0bRSTue0Gwb4JH1taUSsfMTUORdrbWdNUAU2");
		JSONObject o2 = Authentification.logout(null);
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
