package serveur;

import services.tools.MapReduceTools;

public class MapReduce implements Runnable {
	private final static long TIME = 20*60*1000;//en ms (20minutes)
	
	@Override
	public void run() {
		while(true) {
			System.out.println("debut");
			MapReduceTools.runMapReduce();
			
			//attente
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
