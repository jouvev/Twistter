package services.test;

import org.json.JSONObject;

import services.Message;

public class LikeMessage {
	public static void main(String[] args) {
		
		JSONObject o = Message.likeMessage("Re6KnNTFAIa0NEavrOFAOSOqDt6QRbmK6SziWFZEhVFrvsE9cGLDedAkVhnQC47z", "5c74211fa001ae2ee2cf02d4");
		JSONObject o2 = Message.likeMessage(null, "Skitry");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}
}
