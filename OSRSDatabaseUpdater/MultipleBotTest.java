package scripts;

import java.util.Random;

public class MultipleBotTest {
	public static void main(String args[]) throws Exception {
		
		String inDriver = "com.mysql.jdbc.Driver";
		String inUrl = "jdbc:mysql://localhost:3306/liveosbotdb";
		String inUser = "root";
		String inPass = "";
		DBUpdater db = new DBUpdater(inDriver, inUrl, inUser, inPass);
		BotWriter botWrite = new BotWriter();
		Random r =  new Random();
		
		int[][] keyArray = db.getReportKeys();
		int numOfKeys = db.getNumOfReportKeys();
		int count = 0;
		
		while (true) {
			
			for (int i=0; i<numOfKeys; i++) {
				int botId = keyArray[i][0], itemId = keyArray[i][1];
				int randomNumColl = r.nextInt(3);
				botWrite.writeAfter(randomNumColl, botId, itemId);
				botWrite.writeLevelData(13, 10000 + (count*10), 200000, botId, itemId);
				botWrite.writeStatus(botId, r.nextInt(1));
			}
			count++;
			Thread.sleep(5000);
		}
		
	}
}
