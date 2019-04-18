package services.tools;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Tools {
	
	public static String generateKey(int length) {
		String lettres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789";
		String key = "";
		Random r = new Random(); 
		for (int i = 0; i<length; i++){
			int n = r.nextInt(lettres.length());
			key += lettres.charAt(n);
		}
		return key;
	}
	
	public static Date getDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.getTime();
	}
	
	public static Date getDate(String date) {
		/*format DD/MM/AAAA*/
		String[] dateSep = date.split("/");
		return new Date(Integer.parseInt(dateSep[2])-1900,Integer.parseInt(dateSep[1])-1,Integer.parseInt(dateSep[0]));
	}
}
