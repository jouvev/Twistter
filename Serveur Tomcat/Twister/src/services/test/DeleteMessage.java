package services.test;

import org.json.JSONObject;
import services.*;

public class DeleteMessage {

	public static void main(String[] args) {
		
		JSONObject o = Message.deleteMessage("Re6KnNTFAIa0NEavrOFAOSOqDt6QRbmK6SziWFZEhVFrvsE9cGLDedAkVhnQC47z", "5c7420c2589ec96621232a25");
		JSONObject o2 = Message.deleteMessage(null, "Skitry");
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
