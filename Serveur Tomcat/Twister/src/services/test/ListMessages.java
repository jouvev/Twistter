package services.test;

import org.json.JSONObject;
import services.*;

public class ListMessages {

	public static void main(String[] args) {
		
		JSONObject o = Message.listMessages("Skitry", null);
		JSONObject o2 = Message.listMessages(null, null);
		JSONObject o3 = Message.listMessages(null, "1");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
		System.out.println(o3.toString());
	}

}
