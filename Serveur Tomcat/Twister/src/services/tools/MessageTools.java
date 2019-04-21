package services.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bd.MongoDB;

public class MessageTools {

	private static Document createMessage(String auteur, String message, String idMessage) {
		Document d = new Document();
		d.append("auteur", auteur);
		d.append("date", Tools.getDate());
		d.append("message", message);
		d.append("likes", new ArrayList<Integer>());
		d.append("parent", idMessage);
		return d;
	}
	
	/**************************************************************************************/
	
	public static void insertMessage(String auteur, String message, String idMessage){
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		Document d = createMessage(auteur, message, idMessage);
		coll.insertOne(d);
	}
	
	public static void deleteMessage(String idMessage) {
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		
		List<String> openList = new ArrayList<>();
		List<String> closeList = new ArrayList<>();
		openList.add(idMessage);
		
		while(openList.size()>0) {
			String idMsg = openList.remove(0);
			closeList.add(idMsg);
			MongoCursor<Document> cursor = coll.find(new Document("parent", idMsg)).iterator();
			while(cursor.hasNext()) {
				Document d = cursor.next();
				ObjectId did = (ObjectId) d.get("_id");
				openList.add(did.toString());
			}
		}
		
		Map<Integer, Integer> likesToRemove = new HashMap<>();
		Map<String, Integer> twistsToRemove = new HashMap<>();
		for (String id: closeList){
			Document query = new Document("_id", new ObjectId(id));
			MongoCursor<Document> cursor = coll.find(query).iterator();
			Document d = null;
			while(cursor.hasNext()) d = cursor.next();
			@SuppressWarnings("unchecked")
			List<Integer> likesList = (List<Integer>) d.get("likes");

			//pour chaque like d'un message qu'on va supprimer, on enleve un like aux utilisateurs qui l'avait like
			for (Integer idUser: likesList) {
				if (likesToRemove.containsKey(idUser))
					likesToRemove.put(idUser, likesToRemove.get(idUser) + 1);
				else
					likesToRemove.put(idUser, 1);
			}
			//pour chaque message qu'on va supprimer, on enleve un twist à l'utilisateur qui l'avait écrit
			String auteur = (String) d.get("auteur");
			if (twistsToRemove.containsKey(auteur))
				twistsToRemove.put(auteur, twistsToRemove.get(auteur) + 1);
			else
				twistsToRemove.put(auteur, 1);
			
			coll.deleteOne(query);
		}
		
		//après avoir les dictionnaires des valeurs à retirer pour tout le monde, on les parcours pour tout supprimer
		for (Entry<Integer, Integer> couple : likesToRemove.entrySet()){
			try {
				String username = UserTools.getUsernameById(couple.getKey());
				UserTools.updateLikeUser(username, -couple.getValue());
			} catch (Exception e) {e.printStackTrace();}
		}
		for (Entry<String, Integer> couple : twistsToRemove.entrySet())
			UserTools.updateTwistUser(couple.getKey(), -couple.getValue());
	}
	
	public static void deleteUserMessage(int idUser) {
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		MongoCursor<Document> cursor = coll.find(new Document("idUser", idUser)).iterator();
		List<String> idList = new ArrayList<>();
		while(cursor.hasNext()) {
			Document d = cursor.next();
			ObjectId did = (ObjectId) d.get("_id");
			idList.add(did.toString());
		}
		for(String s: idList) 
			deleteMessage(s);
	}
	
	public static JSONObject listMessagesUser(String username) throws JSONException, SQLException, Exception {
		JSONObject res = new JSONObject();
		List<Document> liste = new ArrayList<>();
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		
		Document query = new Document();
		
		query = new Document("auteur", username);
		
		MongoCursor<Document> cursor = coll.find(query).sort(new Document("date",-1)).iterator();
		while(cursor.hasNext()) {
			Document d = cursor.next();
			liste.add(d);
		}
		res.put("Messages", liste);
		return res;
	}
	
	
	public static JSONObject listMessagesRacines() throws JSONException, SQLException, Exception {
		JSONObject res = new JSONObject();
		List<Document> liste = new ArrayList<>();
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		
		Document query = new Document();
		
		query = new Document("parent", null);
		
		MongoCursor<Document> cursor = coll.find(query).sort(new Document("date",-1)).iterator();
		while(cursor.hasNext()) {
			Document d = cursor.next();
			liste.add(d);
		}
		res.put("Messages", liste);
		return res;
	}
	
	public static JSONObject listMessagesReponses(String idParent) throws JSONException, SQLException, Exception {
		JSONObject res = new JSONObject();
		List<Document> liste = new ArrayList<>();
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		
		Document query = new Document();
		
		query = new Document("parent", idParent);
		
		MongoCursor<Document> cursor = coll.find(query).sort(new Document("date",-1)).iterator();
		while(cursor.hasNext()) {
			Document d = cursor.next();
			liste.add(d);
		}
		res.put("Messages", liste);
		return res;
	}
	
	
	
	public static void likeMessage(int idUser, String idMessage) {
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		MongoCursor<Document> cursor = coll.find(new Document("_id", new ObjectId(idMessage))).iterator();
		Document d = null;
		while(cursor.hasNext()) d = cursor.next();
		@SuppressWarnings("unchecked")
		List<Integer> likesList = (List<Integer>) d.get("likes");
		
		if (!likesList.contains(idUser)) {
			likesList.add(idUser);
			//je dois faire un try catch pour appeler la fonction qui ajoute un like
			try {
				String username = UserTools.getUsernameById(idUser);
				UserTools.updateLikeUser(username, 1);
			} catch (Exception e) {e.printStackTrace();}
		}
		else {
			likesList.remove((Integer) idUser);
			//je dois faire un try catch pour appeler la fonction qui ajoute un like
			try {
				String username = UserTools.getUsernameById(idUser);
				UserTools.updateLikeUser(username, -1);
			} catch (Exception e) {e.printStackTrace();}
		}
		
		Document search = new Document();
		search.append("_id", new ObjectId(idMessage));
		Document query = new Document("$set", new Document("likes", likesList));
		coll.updateOne(search, query);
	}
}
