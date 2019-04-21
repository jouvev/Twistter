package services.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.Part;

import bd.DBStatic;
import bd.MySQL;

public class UploadTools {
	//chemin vers img de profil
	private static final String PATH = "/home/tomcat/img_profil/";
	private static final int TAILLE_TAMPON = 10240;//10ko

	public static boolean uploadImage( Part part, String key) throws SQLException, Exception {
		if(! AuthentificationTools.keyExists(key)) {
			return false;
		}
		
		/* Prépare les flux. */
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		String format = getFormat(part);
		String username = UserTools.getUsernameByKey(key);
		if( format == null || !(format.equals("png")))
			return false;
		String nomFichier = "profil_"+username+"."+format;
		changerImageDeProfil(username, nomFichier);
		try {
			/* Ouvre les flux. */
			entree = new BufferedInputStream( part.getInputStream(), TAILLE_TAMPON );
			sortie = new BufferedOutputStream( new FileOutputStream( new File( PATH + nomFichier ) ),
					TAILLE_TAMPON );
			/*
			 * Lit le fichier reçu et écrit son contenu dans un fichier sur le
			 * disque.
			 */
			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur;
			while ( ( longueur = entree.read( tampon ) ) > 0 ) {
				sortie.write( tampon, 0, longueur );
			}
		} finally {
			try {
				sortie.close();
			} catch ( IOException ignore ) {
			}
			try {
				entree.close();
			} catch ( IOException ignore ) {
			}
		}
		return true;
	}
	
	private static String getFormat(Part part) {
		 /* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
	    for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
	    	/* Recherche de l'éventuelle présence du paramètre "filename". */
	        if ( contentDisposition.trim().startsWith("filename") ) {
	            /* Si "filename" est présent, alors renvoi de sa valeur, c'est-à-dire l'extention du fichier de fichier. */
	            return contentDisposition.substring( contentDisposition.indexOf( '.' ) + 1 ).trim().replace( "\"", "" );
	        }
	    }
	    /* Et pour terminer, si rien n'a été trouvé... */
	    return null;
	}
	
	private static void changerImageDeProfil(String username, String chemin) throws SQLException, Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connexion =  MySQL.getMySQLConnection();
		Statement lecture = connexion.createStatement();
		
		String query = "UPDATE "+DBStatic.table_user+" SET image=\""+chemin+"\" WHERE username=\""+username+"\";";
		lecture.executeUpdate(query);
		
		lecture.close();
		connexion.close();
	}
}
