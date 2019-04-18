package services.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import bd.DBStatic;
import bd.MongoDB;


public class MapReduceTools {
	
	private static String map = "function(){\r\n" + 
			"	var text = this.message;\r\n" + 
			"	var words = text.match(/\\w+/g);\r\n" + 
			"	\r\n" + 
			"	if (words === null) {\r\n" + 
			"		return;\r\n" + 
			"	}\r\n" + 
			"	\r\n" + 
			"	var tf = {};\r\n" + 
			"	for (var i=0; i<words.length; i++){\r\n" + 
			"		if (tf[words[i]] == null){\r\n" + 
			"			tf[words[i]] = 1;\r\n" + 
			"		}\r\n" + 
			"		else {\r\n" + 
			"			tf[words[i]] += 1;\r\n" + 
			"		}\r\n" + 
			"	}\r\n" + 
			"	for (var w in tf) {\r\n" + 
			"		var ret = {};\r\n" + 
			"		ret[this._id] = tf[w];\r\n" + 
			"		emit(w,ret);\r\n" + 
			"	}\r\n" + 
			"}";
	
	private static String reduce = "function(key, values){\r\n" + 
			"	var ret = {};\r\n" + 
			"	for(var i=0;i<values.length;i++){\r\n" + 
			"		for(var d in values[i]){\r\n" + 
			"			ret[d]=values[i][d]\r\n" + 
			"		}\r\n" + 
			"	}\r\n" + 
			"	return ret;\r\n" + 
			"}";
	
	private static String f = "function(key, values){\r\n" + 
			"	var df = Object.keys(values).length;\r\n" + 
			"	for (var d in values){\r\n" + 
			"		values[d]=values[d]*Math.log(N/df);\r\n" + 
			"	}\r\n" + 
			"	return values;\r\n" + 
			"}";
	
	@SuppressWarnings("deprecation")
	public static void runMapReduce() {
		MongoClient mongo = MongoClients.create(DBStatic.mongo_host);
		MongoDatabase database = mongo.getDatabase(DBStatic.mongo_bd);
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		Document d = new Document();
		d.put("mapreduce", "posts");
		d.put("map", map);
		d.put("reduce", reduce);
		d.put("out", "postsRes");
		d.put("finalize", f);
		d.put("scope", new Document("N", coll.count()));
		database.runCommand(d);
	}
	
	public static ArrayList<Document> getMessageByQuery(MongoCollection<Document> posts,
			MongoCollection<Document> postsRes, String query){
		
		String[] mots = query.split(" ");
		HashSet<String> w = new HashSet<>();
		for (String mot: mots) 
			if (!w.contains(mot))
				w.add(mot);
		
		HashMap<ObjectId, Double> scores = new HashMap<>();
		for(String mot: w) {
			Document d = new Document();
			d.put("_id", mot);
			MongoCursor<Document> cursor = postsRes.find(d).iterator();
			
			if (cursor.hasNext()) {
				Document res = cursor.next();
				Document messages = (Document) res.get("value");
				
				for(Entry<String, Object> msg: messages.entrySet()) {
					String docObjectId = msg.getKey();
					System.out.println(docObjectId);
					String[] docId = docObjectId.split("\"");
					String idMessage = docId[1];
					
					ObjectId idDoc = new ObjectId(idMessage);
					
					double val = (double)msg.getValue();
					Double s = scores.get(idDoc);
					s = (s==null)?val:(s+val);
					scores.put(idDoc, s);
				}
			}
		}
		
		List<Map.Entry<ObjectId, Double>> entries = new ArrayList<>(scores.entrySet());
		
		Collections.sort(entries, (e1,e2) -> { 
									return -Double.compare(e1.getValue(),e2.getValue());
								});
		
		ArrayList<Document> ret = new ArrayList<>();
		for (Map.Entry<ObjectId, Double> entry: entries) {
			Document request = new Document("_id", entry.getKey());
			MongoCursor<Document> cursor = posts.find(request).iterator();
			
			if (cursor.hasNext()) {
				Document res = cursor.next();
				ret.add(res);
			}
		}
		return ret;
	}
	
	
	public static JSONObject searchMessagesResults(String query) throws JSONException {
		MongoCollection<Document> posts = MongoDB.getCollection("posts");
		MongoCollection<Document> postsRes = MongoDB.getCollection("postsRes");
		
		ArrayList<Document> liste = getMessageByQuery(posts, postsRes, query);
		
		JSONObject res = new JSONObject();
		res.put("Messages", liste);
		
		return res;
	}
}
