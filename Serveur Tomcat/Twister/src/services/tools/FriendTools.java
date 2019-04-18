package services.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import bd.MySQL;

public class FriendTools {
	
	public static boolean alreadyFriend(int idUser1, int idUser2) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT COUNT(*) as val FROM twister_friend WHERE idUser1=\""+idUser1+"\" and idUser2=\""+idUser2+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		int nb = curseur.getInt("val");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return nb >= 1;
	}
	
	public static boolean insertFriend(int idUser1, int idUser2) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="INSERT INTO twister_friend VALUES (\""+idUser1+"\",\""+idUser2+"\");";
		int val = lecture.executeUpdate(query);
		lecture.close();
		connexion.close();
		
		return val >= 1;
	}
	
	public static boolean deleteFriend(int idUser1, int idUser2) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="DELETE FROM twister_friend WHERE idUser1=\""+idUser1+"\" AND idUser2=\""+idUser2+"\";";
		int val = lecture.executeUpdate(query);
		lecture.close();
		connexion.close();
		
		return val >= 1;
	}
	
	public static List<String> usernameListFriends(int idUser1) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT u.username FROM twister_user u, twister_friend f WHERE f.idUser1="+idUser1+" and f.idUser2=u.id;";

		ResultSet curseur = lecture.executeQuery(query);

		List<String> liste = new ArrayList<>();
		while(curseur.next())
			liste.add(curseur.getString("username"));
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return liste;
	}
	
	public static JSONObject listFriends(int idUser1) throws SQLException, Exception{
		JSONObject res = new JSONObject();
		res.put("Friends", usernameListFriends(idUser1));
		
		return res;
	}
	
	
}
