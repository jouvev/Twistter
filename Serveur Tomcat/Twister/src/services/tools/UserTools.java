package services.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import bd.MySQL;

public class UserTools {
	
	public static boolean userExists(String username) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT COUNT(*) as val FROM twister_user WHERE username=\""+username+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		int nb = curseur.getInt("val");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return nb>=1;
	}
	
	public static boolean insertUser(String name, String firstName, String username, String password, String email) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="INSERT INTO twister_user(username,password,name,firstName,email,root,nbTwists,nbLikes) VALUES (\""+username+"\",\""+password+"\",\""+name+"\",\""+firstName+"\",\""+email+"\", FALSE, 0, 0);";
		int val = lecture.executeUpdate(query);
		lecture.close();
		connexion.close();
		return val >= 1;
	}
	
	public static int getIdUser(String username) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT id FROM twister_user WHERE username=\""+username+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		int id = curseur.getInt("id");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return id;
	}
	
	public static int getIdUserByKey(String key) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		
		String query="SELECT idUser FROM twister_session WHERE keySession=\""+key+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		int id = curseur.getInt("idUser");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return id;
	}
	
	public static String getUsernameByKey(String key) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		
		String query="SELECT user.username FROM twister_session session, twister_user user WHERE session.keySession=\""+key+"\" and session.idUser=user.id;";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		String username = curseur.getString("user.username");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return username;
	}
	
	public static String getUsernameById(int idUser) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		
		String query="SELECT username FROM twister_user WHERE id=\""+idUser+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		String username = curseur.getString("username");
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return username;
	}
	
	public static JSONObject getUserById(int idUser) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT name,firstName,username,email FROM twister_user WHERE id=\""+idUser+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		String name = curseur.getString("name");
		String firstName = curseur.getString("firstName");
		String username = curseur.getString("username");
		String email = curseur.getString("email");
		
		JSONObject res = new JSONObject();
		res.put("name", name);
		res.put("firstName", firstName);
		res.put("username", username);
		res.put("email", email);
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return res;
	}
	
	public static boolean deleteUser(int idUser) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="DELETE FROM twister_user WHERE id=\""+idUser+"\";";
		int val = lecture.executeUpdate(query);
		lecture.close();
		connexion.close();
		
		return val >= 1;
	}

	public static JSONObject searchUser(String pattern) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT username FROM twister_user WHERE UPPER(username) LIKE UPPER(\"%"+pattern+"%\");";
		ResultSet curseur = lecture.executeQuery(query);
		
		List<String> listeUsers = new ArrayList<>();
		
		while(curseur.next()) {
			listeUsers.add(curseur.getString("username"));
		}
	
		JSONObject res = new JSONObject();
		res.put("Users", listeUsers);
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return res;
	}
	
	public static JSONObject statsUser(String username) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		
		String query="SELECT nbTwists,nbLikes FROM twister_user WHERE username=\""+username+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		int nbTwists = curseur.getInt("nbTwists");
		int nbLikes = curseur.getInt("nbLikes");
		
		JSONObject res = new JSONObject();
		res.put("nbTwists", nbTwists);
		res.put("nbLikes", nbLikes);
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return res;
	}
	
	
	public static void updateLikeUser(String username, int nb){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connexion =  MySQL.getMySQLConnection();
			Statement lecture = connexion.createStatement();
			
			String query="UPDATE twister_user SET nbLikes = nbLikes + "+nb+" WHERE username=\""+username+"\";";
			lecture.executeUpdate(query);
			lecture.close();
			connexion.close();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void updateTwistUser(String username, int nb){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connexion =  MySQL.getMySQLConnection();
			Statement lecture = connexion.createStatement();
			String query="UPDATE twister_user SET nbTwists = nbTwists + "+nb+" WHERE username=\""+username+"\";";
			lecture.executeUpdate(query);
			lecture.close();
			connexion.close();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
