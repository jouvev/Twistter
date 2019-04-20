package services.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import bd.DBStatic;
import bd.MySQL;

/**
 * @author Vincent
 * Classe relative à tout ce qui est authentification.
 * cad : login, logout, verif de password et session(key) 
 */
public class AuthentificationTools {
	
	//limite de validité de la clé en millisecondes
	private static final long TIME_LIMIT = 1000*60*15;//(15 minutes)
	
	
	/**
	 * @param username 
	 * @param password
	 * @return true si le mot de passe correspond à l'username, false sinon
	 * @throws SQLException
	 * @throws Exception 
	 */
	public static boolean checkPassword(String username, String password) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="SELECT password FROM "+DBStatic.table_user+" WHERE username=\""+username+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		curseur.next();
		String password2 = curseur.getString("password");
		
		curseur.close();
		lecture.close();
		connexion.close();
		return password.equals(password2);
	}

	/**
	 * Créer une session pour l'utilisateur et insert les infos dans la base
	 * @param idUser
	 * @param key
	 * @param ip
	 * @return	true si l'insertion s'est bien passée, false sinon
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean insertKey(int idUser, String key, String ip) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		//recup s'il est root
		String query1 = "SELECT root FROM "+DBStatic.table_user+" WHERE id=\""+idUser+"\";";
		ResultSet curseur = lecture.executeQuery(query1);
		curseur.next();
		boolean root = curseur.getBoolean("root");
		curseur.close();
		
		//insert dans la base 
		String query2;
		if(ip!=null)
			query2 = "INSERT INTO "+DBStatic.table_session+"(keySession, idUser, root, isValid, ip) VALUES (\""+key+"\",\""+idUser+"\","+root+",true,\""+ip+"\");";
		else
			query2 = "INSERT INTO "+DBStatic.table_session+"(keySession, idUser, root, isValid) VALUES (\""+key+"\",\""+idUser+"\","+root+",true);";
		int curseur2 = lecture.executeUpdate(query2);
		lecture.close();
		connexion.close();
		
		return curseur2 >= 1;
	}
	
	
	/**
	 * @param key
	 * @return true si la clé est valide, false sinon
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean keyExists(String key) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		//recup la clé dans la base
		String query="SELECT dateCreation, root, isValid FROM "+DBStatic.table_session+" WHERE keySession=\""+key+"\";";
		ResultSet curseur = lecture.executeQuery(query);
		
		//si null return false la key n'existe pas
		if(!curseur.next()) 
			return false;
		
		boolean res = true;
		
		Date dateKey = curseur.getDate("dateCreation");//on recup la date 
		
		Calendar current = new GregorianCalendar();
		Date dateCurrent = current.getTime();
		
		boolean root = curseur.getBoolean("root");
		boolean isValid = curseur.getBoolean("isValid");
		if (root)
			return true;
		else if (!isValid || dateCurrent.getTime() - dateKey.getTime() < TIME_LIMIT) {
			res=false;
			invalidatingKey(key);//utile?
		}
		else
			res = false;
		
		if(res==true) {//on refresh la date de validité
			String query2="UPDATE "+DBStatic.table_session+" SET dateCreation=NOW() WHERE keySession='"+key+"';";
			if(lecture.executeUpdate(query2)==0) res = false;//si bug
		}
		
		curseur.close();
		lecture.close();
		connexion.close();
		
		return res;
	}
	
	/**
	 * Invalide la key dans la base
	 * @param key
	 * @return true si l'update s'est bien passée, false sinon 
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean invalidatingKey(String key) throws SQLException, Exception{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query="UPDATE "+DBStatic.table_session+" SET isValid=false WHERE keySession=\""+key+"\";";
		int curseur = lecture.executeUpdate(query);

		lecture.close();
		connexion.close();
		return curseur >= 1;
	}
}
