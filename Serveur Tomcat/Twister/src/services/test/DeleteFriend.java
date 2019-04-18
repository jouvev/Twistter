package services.test;

import org.json.JSONObject;
import services.*;

public class DeleteFriend {

	public static void main(String[] args) {
		
		JSONObject o = Friend.deleteFriend("Re6KnNTFAIa0NEavrOFAOSOqDt6QRbmK6SziWFZEhVFrvsE9cGLDedAkVhnQC47z", "ToLluvo");
		JSONObject o2 = Friend.deleteFriend(null, "Skitry");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
