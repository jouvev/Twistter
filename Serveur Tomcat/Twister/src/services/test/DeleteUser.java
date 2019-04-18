package services.test;

import org.json.JSONObject;
import services.*;

public class DeleteUser {

	public static void main(String[] args) {
		
		JSONObject o = User.deleteUser("Re6KnNTFAIa0NEavrOFAOSOqDt6QRbmK6SziWFZEhVFrvsE9cGLDedAkVhnQC47z");
		JSONObject o2 = User.deleteUser(null);
		
		System.out.println(o.toString());
		System.out.println(o2.toString());
	}

}
