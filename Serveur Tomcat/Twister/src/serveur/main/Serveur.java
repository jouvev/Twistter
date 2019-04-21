package serveur.main;

import serveur.MapReduce;

public class Serveur {
	public static void main(String[] args) {
		//mapReduce
		(new Thread(new MapReduce())).start();
	}
}
