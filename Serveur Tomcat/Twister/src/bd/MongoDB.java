package bd;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	private final MongoClient mongo;
	private final MongoDatabase mDB;
	private static MongoDB myDB=null;
	
	private MongoDB(){
		mongo=MongoClients.create(DBStatic.mongo_host);
		mDB= mongo.getDatabase(DBStatic.mongo_bd);
	}
	
	public static MongoCollection<Document> getCollection(String name){
		if(myDB==null)
			myDB=new MongoDB();
		return myDB.mDB.getCollection(name);
	}
}
