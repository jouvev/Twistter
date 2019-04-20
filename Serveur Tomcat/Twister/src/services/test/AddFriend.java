package services.test;

import org.json.JSONObject;
import services.*;

public class AddFriend {

	public static void main(String[] args) {
		
		JSONObject o = Friend.addFriend("1", "test");
		JSONObject o2 = Friend.addFriend(null, "Skitry");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
