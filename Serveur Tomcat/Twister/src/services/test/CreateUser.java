package services.test;

import org.json.JSONObject;
import services.*;

public class CreateUser {

	public static void main(String[] args) {
		
		JSONObject o = User.createUser("Cadiou", "Antoine", "ToLluvo", "azerty123", "gpasdmail@gmail.com");
		JSONObject o1 = User.createUser("Jouve", "Vincent", "Skitry", "azerty123", "gpasdmail2@gmail.com");
		JSONObject o2 = User.createUser(null, "Antoine", "Skitry", "azerty123", "gpasdmail@gmail.com");
		
		System.out.println(o.toString());
		System.out.println(o1.toString());
		System.out.println(o2.toString());
	}

}
