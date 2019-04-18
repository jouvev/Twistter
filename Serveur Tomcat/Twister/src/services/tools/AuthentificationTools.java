package services.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import bd.MySQL;

public class AuthentificationTools {
	
	//limite de validit� de la cl� en millisecondes (15 minutes)
	private static final long TIME_LIMIT = 1000*60*15;
	
	public static boolean checkPassword(String username, String password) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT password FROM twister_user WHERE username=\""+username+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		String password2 = curseur.getString("password");
		
		curseur.close();
		lecture.close();
		connexion.close();
		return password.equals(password2);
	}

	public static boolean insertKey(int idUser, String key) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query1 = "SELECT root FROM twister_user WHERE id=\""+idUser+"\";";
		ResultSet curseur = lecture.executeQuery(query1);
		curseur.next();
		boolean root = curseur.getBoolean("root");
		
		curseur.close();
		String query2 = "INSERT INTO twister_session(keySession, idUser, root, isValid) VALUES (\""+key+"\",\""+idUser+"\","+root+",true);";
		int curseur2 = lecture.executeUpdate(query2);
		lecture.close();
		connexion.close();
		return curseur2 >= 1;
	}
	
	public static boolean keyExists(String key) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT dateCreation, root, isValid, COUNT(*) as nb FROM twister_session WHERE keySession=\""+key+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		curseur.next();
		int nb = curseur.getInt("nb");
		boolean res = true;
		
		if (nb >= 1){
			Date dateKey = curseur.getDate("dateCreation");
			
			Calendar current = new GregorianCalendar();
			Date dateCurrent = current.getTime();
			
			boolean root = curseur.getBoolean("root");
			boolean isValid = curseur.getBoolean("isValid");
			if (root)
				res = true;
			else if (!isValid || dateCurrent.getTime() - dateKey.getTime() < TIME_LIMIT) {
				res=false;
				invalidatingKey(key);
			}
		}
		else
			res = false;
		
		if(res==true) {
			String query2="UPDATE twister_session SET dateCreation=NOW() WHERE keySession='"+key+"';";
			if(lecture.executeUpdate(query2)==0) res = false;
		}
		
		curseur.close();
		lecture.close();
		connexion.close();
		return res;
	}
	
	public static boolean invalidatingKey(String key) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="UPDATE twister_session SET isValid=false WHERE keySession=\""+key+"\";";
		int curseur = lecture.executeUpdate(query);

		lecture.close();
		connexion.close();
		return curseur >= 1;
	}
}
