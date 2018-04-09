package scripts;

import java.util.Random;

public class MultipleBotTest {
	public static void main(String args[]) throws Exception {
		
		String inDriver = "com.mysql.jdbc.Driver";
		String inUrl = "jdbc:mysql://localhost:3306/liveosbotdb";
		String inUser = "root";
		String inPass = "";
		DBUpdater db = new DBUpdater(inDriver, inUrl, inUser, inPass);
		Random r =  new Random();
		
		int[][] keyArray = db.getReportKeys();
		int numOfKeys = db.getNumOfReportKeys();
		int count = 0;
		
		while (true) {
			
			for (int i=0; i<numOfKeys; i++) {
				int botId = keyArray[i][0], itemId = keyArray[i][1];
				int randomNumColl = r.nextInt(3);
				db.writeAfter(randomNumColl, botId, itemId);
				db.writeLevelData(13, 10000 + (count*10), 200000, botId, itemId);
				//System.out.println("HELLO");
			}
			count++;
			Thread.sleep(5000);
		}
		
	}
}
