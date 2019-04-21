package services.tools;

public class Errors {
	/*
	 * erreurs classiques
	 */
	public static final Errors ERROR_ARGUMENT 	= new Errors("Arguments manquants ou invalides", -1);
	public static final Errors ERROR_JSON 		= new Errors("Erreur JSON", 100);
	public static final Errors ERROR_SQL 		= new Errors("Erreur SQL", 	1000);
	public static final Errors ERROR_JAVA 		= new Errors("Erreur JAVA", 10000);
	public static final Errors ERROR_USER_EXISTING = new Errors("Erreur utilisateur existant", 2);
	public static final Errors ERROR_INSERTION_SQL = new Errors("Erreur insertion sql", 3);
	public static final Errors ERROR_USER_NOT_EXISTING = new Errors("Erreur utilisateur inexistant", 4);
	public static final Errors ERROR_WRONG_PASSWORD = new Errors("Erreur mauvais mdp", 5);
	public static final Errors ERROR_INCORRECT_KEY = new Errors("Erreur mauvaise cle", 6);
	public static final Errors ERROR_UPDATE_SQL = new Errors("Erreur update sql", 7);
	public static final Errors ERROR_ALREADY_FRIEND = new Errors("Erreur deja amis", 8);
	public static final Errors ERROR_NOT_FRIEND = new Errors("Erreur PAS amis", 9);
	public static final Errors ERROR_DELETE_SQL = new Errors("Erreur update sql", 10);


	private String message;
	private int code;
	
	
	/**
	 * Créer une erreur avec le code 1
	 * @param message de l'erreur
	 */
	public Errors(String message){
		this.message = message;
		this.code = 1;
	}
	
	/**
	 * Sert à créer une variable Errors static
	 * @param message de l'erreur
	 * @param code de l'erreur
	 */
	private Errors(String message, int code){
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}
