package services.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import bd.MongoDB;
import bd.MySQL;

public class RechercheTools {
	
	public static List<Document> rechercheAvance(String from, String to, Integer popularite) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		//liste qui contient les utilisateurs populaire
		List<String> usersPopulaire = new ArrayList<>();
		
		String query="SELECT u.username as user FROM twister_user u WHERE (SELECT count(*) from twister_friend f WHERE u.id=f.idUser2)>="+popularite+";";
		ResultSet curseur = lecture.executeQuery(query);
		
		while(curseur.next()) {
			usersPopulaire.add(curseur.getString("user"));
		}
		
		curseur.close();
		lecture.close();
		connexion.close();
	
		//liste des messages
		List<Document> messages = new ArrayList<>();
		MongoCollection<Document> coll = MongoDB.getCollection("posts");
		
		Document querymongo;
		
		querymongo = new Document("auteur", new Document("$in", usersPopulaire)).append("date" , new Document("$gte", Tools.getDate(from)).append("$lte", Tools.getDate(to)));
		
		System.out.println(querymongo+" "+Tools.getDate(from));
		
		MongoCursor<Document> cursor = coll.find(querymongo).iterator();
		
		while(cursor.hasNext()) {
			Document d = cursor.next();
			messages.add(d);
		}

		return messages;
	}
}
