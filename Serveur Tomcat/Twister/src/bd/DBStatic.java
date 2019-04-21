package bd;

public class DBStatic {
	//mysql
	public static final String mysql_host = "localhost:3306";
	public static final String mysql_username = "root";
	public static final String mysql_password = "q96p{+DA3";
	public static final boolean mysql_pooling = true;
	public static final String mysql_db = "twister";
		//tables
	public static final String table_user = "twister_user";
	public static final String table_session = "twister_session";
	public static final String table_friend = "twister_friend";
	
	//mongo
	public static final String mongo_host = "mongodb://localhost:27017";
	public static final String mongo_bd = "twister";
		//collections
	public static final String coll_message = "posts";
	public static final String coll_mapReduce = "postsRes";
}
