package services.test;

import org.json.JSONObject;
import services.*;

public class AddFriend {

	public static void main(String[] args) {
		
		JSONObject o = Friend.addFriend("fN1w1JJYtYccaRrN6diUqg1ahUjEhOrKQ6S8ViObl67S3Soww5qN1iFTZIylkNvt", "test");
		JSONObject o2 = Friend.addFriend(null, "Skitry");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
