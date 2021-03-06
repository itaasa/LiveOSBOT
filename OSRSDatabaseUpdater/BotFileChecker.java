package dbbotconnector;

import java.io.File;
import java.io.IOException;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Will check if any necessary files have been created to store
 *				bot data, otherwise, this class creates them
 *
 * HOW: 	1. 	Finds all active botId's and itemId's in the database
 *			2. 	Checks if invCount, level, status text files exist with 
 * 				the corresponding botId's and itemId's found in 1.
 * 			3. 	Any non existing files will be created
 *			
 * WHY:		1. 	If bot data files are not created, there is no where to
 * 				store live information from each bot
 * ---------------------------------------------------------------------- */

public class BotFileChecker {
	
	private DBSelector dbSelect;
	private DBUpdater dbUpdate;
	
	public BotFileChecker (String driver, String url, String user, String pass) {
		dbSelect = new DBSelector (driver, url, user, pass);
		dbUpdate = new DBUpdater (driver, url, user, pass);
	}
	
	//Creates all necessary files not already created
	//Calls on invCountExists for all existing botId and itemId in database
	public void checkAllFiles() throws Exception {
		
		int [][] reportKeys = dbSelect.getReportKeys();
		
		int tempCount = 0;
		dbUpdate.updateAllBotStatus();
		
		while (reportKeys.length == 0) {
			System.out.println("No bots are online... Now waiting for bot(s) to be active.");
			dbUpdate.updateAllBotStatus();
			Thread.sleep(5000);
			tempCount++;
			System.out.println("Time elapsed: " + tempCount * 5 + " seconds");
			
			reportKeys = dbSelect.getReportKeys();
		} 
		
		System.out.println("Now checking if necessary files exist...");
		Thread.sleep(1000);
		
		for (int i=0; i<reportKeys.length; i++){
			invCountExists(reportKeys[i][0], reportKeys[i][1]);
			levelCountExists(reportKeys[i][0], reportKeys[i][1]);
			statusExists(reportKeys[i][0]);
			worldExists(reportKeys[i][0]);
		}
		
		System.out.println("All necessary files exists! Now starting application...");
		Thread.sleep(1000);
		System.out.println();
		System.out.println();
		
	}
	
	//Checks if any invCount text files with corresponding botId and itemId are missing
	//if so, this method will create them
	private void invCountExists (int botId, int itemId) throws IOException {
		String afterPathString = System.getProperty("user.dir") + File.separator + 
				"itemdata" + File.separator + "invCountAfter" + "_" + botId + "_"
				+ itemId + ".txt";
		
		String beforePathString =  System.getProperty("user.dir") + File.separator + 
				"itemdata" + File.separator + "invCountBefore" + "_" + botId + "_"
				+ itemId + ".txt";
		
		File afterFile = new File (afterPathString);
		File beforeFile = new File (beforePathString);
		
		if (!afterFile.isFile()){
			System.out.println("Missing invCountAFTER textfile for bot: " + botId + ", collecting item: " + itemId);
			System.out.println("Now creating that file...\n");
			afterFile.createNewFile();
		}
		
		
		if (!beforeFile.isFile()){
			System.out.println("Missing invCountBEFORE textfile for bot: " + botId + ", collecting item: " + itemId);
			System.out.println("Now creating that file...\n");
			beforeFile.createNewFile();
		}
		
	}
	
	//Same as invCountExists, but we check if necessary levelCount text files have been made
	private void levelCountExists (int botId, int itemId) throws IOException {
		String pathString = System.getProperty("user.dir") + File.separator + 
				"leveldata" + File.separator + "levelCount" + "_" + botId + "_"
				+ itemId + ".txt";
			
		File file = new File (pathString);
	
		if (!file.isFile()){
			System.out.println("Missing levelCount textfile for bot: " + botId + ", collecting item: " + itemId);
			System.out.println("Now creating that file...\n");
			file.createNewFile();
		}		
	}
	
	//Same as invCountExists, but we check if necessary onlineStatus text files have been made
	private void statusExists (int botId) throws IOException {
		String pathString = System.getProperty("user.dir") + File.separator + 
				"statusdata" + File.separator + "onlineStatus" + "_" + botId + ".txt";
		
		File file = new File (pathString);
		
		if (!file.isFile()) {
			System.out.println("Missing onlineStatus textfile for bot: " + botId);
			System.out.println("Now creating that file...\n");
			file.createNewFile();
		}
	}
	
	private void worldExists (int botId) throws IOException {
		String pathString = System.getProperty("user.dir") + File.separator + 
				"worlddata" + File.separator + "world" + "_" + botId + ".txt";
		
		File file = new File (pathString);
		
		if (!file.isFile()) {
			System.out.println("Missing world textfile for bot: " + botId);
			System.out.println("Now creating that file...\n");
			file.createNewFile();
		}
	}
	
}
