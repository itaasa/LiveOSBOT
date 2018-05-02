package dbbotconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.io.IOException;


public class DBUpdater {
	
	//Allows for class to read and write to bot data text files
	private BotReader botRead;
	private BotWriter botWrite;
	
	//Database connection information
	private String driver, url, user, pass;
	
	public DBUpdater(String driver, String url, String user, String pass) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.botRead = new BotReader();
		this.botWrite = new BotWriter();
	}
	
	//Updates the database with all the values passed by ALL bots currently running (this uses all methods)
	public void executeProc() throws Exception{
		
		int [][] reportKeys = getReportKeys();
		int [] totalItemsCollected = getTotalNumOfItems();
		String [] botNames = getBotNames();
		String [] itemNames = getItemNames();
		String botName, itemName;
		
		int totalNumOfItems, numOfItems, gpRate, xpRate, numOfKeys = getNumOfReportKeys(), 
				botId, itemId;
		
		System.out.println("||--------------------------------------------------------------------------------------------------------------------------------------------------------------||");
		System.out.println("||\tBot ID\t||"
							+ "\tItem ID\t||"
							+ "\t" + String.format("%-20s", "BotName") + "||"
							+ "\t" + String.format("%-20s", "ItemName") + "||"
							+ "\t\t" + String.format("%-20s", "Count") + "\t\t||"
							+ "\tGP/Hour\t||"
							+ "\tXP/Hour\t||");
		System.out.println("||--------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		for (int i=0; i<numOfKeys; i++) {
			
			botId = reportKeys[i][0];
			itemId = reportKeys[i][1];
			botName = botNames[i];
			itemName = itemNames[i];	
			
			numOfItems = updateNumOfItems(botId, itemId);
			totalNumOfItems = totalItemsCollected[i];
			gpRate = updateGPRate(numOfItems, botId, itemId);
			xpRate = updateXPRate(numOfItems, botId, itemId);
			
			updateLevelData(xpRate, botId, itemId);
			updateStatus(botId);
			
			System.out.println(	"||\t" + botId + "\t|" + 
								"|\t" + itemId + "\t|" + 
								"|\t" + String.format("%-20s", botName) + "|" +
								"|\t" + String.format("%-20s", itemName) + "|" +
								"|\t\t" + String.format("%-20s",totalNumOfItems + " (+" + numOfItems + ")") + "\t\t|" + 
								"|\t" + gpRate + "\t|" +
								"|\t" + xpRate + "\t||");			
		}
		
		System.out.println("||--------------------------------------------------------------------------------------------------------------------------------------------------------------||");
		Thread.sleep(7000);
	}
	
	//Updates the numOfItems collected by the bot given the botId and the itemId of the item they are collecting
	public int updateNumOfItems(int botId, int itemId) throws Exception {
		
		PreparedStatement prepState = null;
		Connection conn = null;
		ResultSet result;
		int numOfItems = 0;
		int numCollected;
		
		int beforeValue = botRead.readBefore(botId, itemId);
		int afterValue = botRead.readAfter(botId, itemId);
		
		if (afterValue >= beforeValue)
			numCollected = afterValue - beforeValue;
		else
			numCollected = 0;
				
		String obtainNumOfItems = "SELECT NumOfItems "
								+ "FROM Report "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
		
		String updateNumOfItems = "UPDATE Report "
								+ "SET NumOfItems = ? "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
		
		

		conn = getConnection();

		//Obtaining the current NumOfItems from the DB
		prepState = conn.prepareStatement(obtainNumOfItems);
		
		
		prepState.setInt(1, botId);
		prepState.setInt(2, itemId);
		
		result = prepState.executeQuery();
		
		while (result.next())
			numOfItems = result.getInt("NumOfItems");
		
		numOfItems += numCollected;
	
		//Updating the current NumOfItems from the DB
		prepState = conn.prepareStatement(updateNumOfItems);
		prepState.setInt(1, numOfItems);
		prepState.setInt(2, botId);
		prepState.setInt(3, itemId);
		prepState.executeUpdate();
		
		
		//Resetting before value to after value
		botWrite.writeBefore(afterValue, botId, itemId);

		return numCollected;
							
	}

	//Updates the gpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateGPRate (int numCollected, int botId, int itemId) throws Exception {
		int gpRate = 0, gpPerItem = 0;
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		conn = getConnection();
		
		if (numCollected != 0) {
			
			//Obtaining the price per item with given itemID
			String obtainGPPerItem = "SELECT ItemPrice "
									+ "FROM Item "
									+ "WHERE ItemID = ?";
				
			prepState = conn.prepareStatement(obtainGPPerItem);
			prepState.setInt(1, itemId);
			result = prepState.executeQuery();
			
			while (result.next())
				gpPerItem = result.getInt("ItemPrice");
		
			gpRate = (numCollected * 360) * gpPerItem;
			
			
			//Now we update the database with the calculated GPRate
			String updateGPRate = "UPDATE Report "
								+ "SET GpPerHour = ? "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			prepState = conn.prepareStatement(updateGPRate);
			prepState.setInt(1, gpRate);
			prepState.setInt(2, botId);
			prepState.setInt(3, itemId);
			prepState.executeUpdate();		
		} 
		
		else {
			
			//Gets the past rate when numCollected was non-zero integer
			String obtainGPRate = "SELECT GpPerHour "
								+ "FROM Report "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			conn = getConnection();
			prepState = conn.prepareStatement(obtainGPRate);
			prepState.setInt(1, botId);
			prepState.setInt(2, itemId);
			result = prepState.executeQuery();
			
			while(result.next())
				gpRate = result.getInt("GpPerHour");
			
		}
				
		return gpRate;
	}
	
	//Updates the xpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateXPRate (int numCollected, int botId, int itemId) throws Exception {
		int xpRate = 0, xpPerItem = 0;
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		conn = getConnection();
		
		if (numCollected != 0) {
		//Obtaining the price per item with given itemID
			String obtainXPPerItem = "SELECT ItemXp "
									+ "FROM Item "
									+ "WHERE ItemID = ?";
				
			prepState = conn.prepareStatement(obtainXPPerItem);
			prepState.setInt(1, itemId);
			result = prepState.executeQuery();
			
			while (result.next())
				xpPerItem = result.getInt("ItemXp");
			
			xpRate = (numCollected * 360) * xpPerItem;
			
			//Now we update the database with the calculated GPRate
			String updateXPRate = "UPDATE Report "
								+ "SET XpPerHour = ? "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			prepState = conn.prepareStatement(updateXPRate);
			prepState.setInt(1, xpRate);
			prepState.setInt(2,botId);
			prepState.setInt(3, itemId);
			prepState.executeUpdate();
			
		}
		
		else {
			//Gets the past rate when numCollected was non-zero integer
			String obtainGPRate = "SELECT XpPerHour "
								+ "FROM Report "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			prepState = conn.prepareStatement(obtainGPRate);
			prepState.setInt(1, botId);
			prepState.setInt(2, itemId);
			result = prepState.executeQuery();
			
			while(result.next())
				xpRate = result.getInt("XpPerHour");
			
		}
		return xpRate;
	}
	
	//Updates the current level, xpNextLevel and timeNextLevel for corresponding botId and itemId 
	private int [] updateLevelData(int xpRate, int botId, int itemId) throws Exception {
		
		int [] levelData = botRead.readLevelData(botId, itemId);
		int curLvl = levelData[0];
		int totalXp = levelData[1];
		int xpNextLvl = levelData[2];
		float timeNextLevel = (float) xpNextLvl / xpRate;
		
		String updateQuery = "UPDATE Report "
							+ "SET CurrentLevel = ?, TotalXp = ?, XpNextLevel  = ?, TimeNextLevel = ? "
							+ "WHERE BotID = ? AND ItemID = ?";
		
		Connection conn = null;
		PreparedStatement prepState = null;	
		conn = getConnection();
		
		prepState = conn.prepareStatement(updateQuery);
		prepState.setInt(1, curLvl);
		prepState.setInt(2, totalXp);
		prepState.setInt(3, xpNextLvl);
		prepState.setString(4, floatToTimeString(timeNextLevel));
		prepState.setInt(5, botId);
		prepState.setInt(6, itemId);
		prepState.executeUpdate();
		
		return levelData;
	}
	
	private void updateStatus (int botId) throws Exception{
		
		int status = botRead.readStatus(botId);
		
		String updateQuery = "UPDATE Bot "
							+ "SET IsOnline = ? "
							+ "WHERE BotID = ?";
		
		Connection conn = null;
		PreparedStatement prepState = null;	
		conn = getConnection();
		prepState = conn.prepareStatement(updateQuery);
		prepState.setInt(1, status);
		prepState.setInt(2, botId);
		prepState.executeUpdate();
	}
	
	//Calculates number of seconds to numbers of day, hours, minutes and seconds and displays it in a string
	private String floatToTimeString(float x) {
		int days, hours, minutes, seconds;
		double decimal = x - Math.floor(x);
		days = (int) (x/24);
		hours = (int) (Math.floor(x) % 24);
		minutes = (int) Math.floor(decimal * 60);
		seconds = (int) Math.floor((decimal * 60 - Math.floor(decimal * 60)) * 60);
		
		return days + "d, " + hours + "h, " + minutes + "m, " + seconds + "s";
	}
	
	//Returns names of bot, given the corresponding botIDs in the parameter array
	public String[] getBotNames() throws Exception {
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		int numOfKeys = getNumOfReportKeys();
		String [] botNames = new String [numOfKeys];
		
		String obtainBotName =  "SELECT BotName " 
								+ "FROM Report, Bot "
								+ "WHERE Report.BotID = Bot.BotID";
		
		conn = getConnection();
		prepState = conn.prepareStatement(obtainBotName);
		result = prepState.executeQuery();
		
		int i=0;
		
		while (result.next()) {
			botNames[i] = result.getString("BotName");
			i++;
		}
		return botNames;
	}
	
	public String[] getItemNames() throws Exception {
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		int numOfKeys = getNumOfReportKeys();
		String [] itemNames = new String [numOfKeys];
		
		String obtainBotName =  "SELECT ItemName " 
								+ "FROM Report, Item "
								+ "WHERE Report.ItemID = Item.ItemID";
		
		conn = getConnection();
		prepState = conn.prepareStatement(obtainBotName);
		result = prepState.executeQuery();
		
		int i=0;
		
		while (result.next()) {
			itemNames[i] = result.getString("ItemName");
			i++;
		}
		return itemNames;
	}
	
	//Gets all total collected items given the botId and the itemId of all active bots
	public int [] getTotalNumOfItems () throws Exception {
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		int numOfKeys = getNumOfReportKeys();
		
		int [] totalItems = new int [numOfKeys];
		
		String obtainNumOfItems =  "SELECT NumOfItems " 
								+ "FROM Report";
		
		conn = getConnection();
		prepState = conn.prepareStatement(obtainNumOfItems);
		result = prepState.executeQuery();
		
		int i=0;
		
		while (result.next()) {
			totalItems[i] = result.getInt("NumOfItems");
			i++;
		}
		
		return totalItems;
		
	}
	
	//Returns the number of tuples in the relation REPORT. In other words the number of active bots
	public int getNumOfReportKeys () throws Exception {
		int numOfKeys = 0;
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		String obtainCountOfTuples = "SELECT * FROM Report";
		
		conn = getConnection();
		prepState = conn.prepareStatement(obtainCountOfTuples);
		result = prepState.executeQuery();
		
		result.last();
		numOfKeys = result.getRow();
		
		return numOfKeys;
	}
	
	//Return a 2-D array consisting of all the current active botIds in the first row, and the corresponding itemIds of the
	//items they are collecting. 
	public int [][] getReportKeys () throws Exception {
		
		int numOfReportKeys = getNumOfReportKeys();
		
		int [][] reportKeys = new int [numOfReportKeys][2];
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		String obtainKeys = "SELECT BotID, ItemID "
							+ "FROM Report";
		
		conn = getConnection();
		prepState = conn.prepareStatement(obtainKeys);
		result = prepState.executeQuery();
		
		for (int i=0; i<numOfReportKeys; i++) {
			result.next();
			reportKeys[i][0] = result.getInt("BotID");
			reportKeys[i][1] = result.getInt("ItemID");
		}
		
		return reportKeys;
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
	
	//same as invCountExists, but we check if necessary levelCount text files have been made
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
	
	//same as invCountExists, but we check if necessary onlineStatus text files have been made
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
	
	
	//Creates all necessary files not already created
	//Calls on invCountExists for all existing botId and itemId in database
	public void preProc() throws Exception {
		
		int [][] reportKeys = getReportKeys();
		
		System.out.println("Now checking if necessary files exist...");
		Thread.sleep(1000);
		
		for (int i=0; i<getNumOfReportKeys(); i++){
			invCountExists(reportKeys[i][0], reportKeys[i][1]);
			levelCountExists(reportKeys[i][0], reportKeys[i][1]);
			statusExists(reportKeys[i][0]);
		}
		
		System.out.println("All necessary files exists! Now starting application...");
		Thread.sleep(1000);
		System.out.println();
		System.out.println();
		
	}
	
	//Establishes connection to the database
	public Connection getConnection() throws Exception {
		
		try {
			Class.forName(this.driver);
			Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);			
			return conn;
		
		} catch (Exception e) {System.out.println (e);}
		
		return null;
	}
	
	
}
