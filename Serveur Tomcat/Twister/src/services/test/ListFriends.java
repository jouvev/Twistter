package services.test;

import org.json.JSONObject;
import services.*;

public class ListFriends {

	public static void main(String[] args) {
		
		JSONObject o = Friend.listFriends("Skitry");
		JSONObject o2 = Friend.listFriends(null);
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
