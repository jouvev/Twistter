package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MySQL {
	
	private DataSource dataSource;
	private static MySQL database = null;
	
	public MySQL(String jndiname) throws SQLException {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
		} catch (NamingException e) {
			// Handle error that it's not configured in JNDI.
			throw new SQLException(jndiname + " is missing in JNDI! : "+e.getMessage());
		}
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public static Connection getMySQLConnection() throws SQLException {
		if (DBStatic.mysql_pooling == false) {
			return(DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" +
					DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password));
		}
		
		else {
			if (database==null) {
				database=new MySQL("jdbc/db");
			}
			return(database.getConnection());
		}
	}
}