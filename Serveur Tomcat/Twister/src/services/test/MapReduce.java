package services.test;

import org.json.JSONException;

import services.tools.MapReduceTools;
import services.tools.MessageTools;

public class MapReduce {

	public static void main(String[] args) {
		try {
			System.out.println(MapReduceTools.searchMessagesResults("test"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void addmessage() {
		MessageTools.insertMessage("test", "test coucou 2", null);
		MessageTools.insertMessage("test", "test bonjour", null);
		MessageTools.insertMessage("test", "coucou bisous", null);
		MessageTools.insertMessage("test", "big kiss", null);
	}
}
