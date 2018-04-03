package scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class DBUpdater {
	
	//Information needed to connect to database
	private String driver, url, user, pass;
	
	//For reading and writing live updating inventory counts
	private FileReader frBefore, frAfter;
	private BufferedReader brBefore, brAfter;
	private FileWriter fwBefore, fwAfter;
	private PrintWriter pwBefore, pwAfter;

	//Constructor class
	public DBUpdater(String driver, String url, String user, String pass) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;	
	}
	
	//Updates the database with all the values passed by ALL bots running
	public void executeProc() throws Exception{
		
		int [][] reportKeys = getReportKeys();
		String [] botNames = getBotNames();
		String [] itemNames = getItemNames();
		String botName, itemName;
		
		int numOfItems, gpRate, xpRate, numOfKeys = getNumOfReportKeys(), botId, itemId;
		System.out.println("||------------------------------------------------------------------------------------------------------------------------------||");
		System.out.println("||\tBot ID\t||"
							+ "\tItem ID\t||"
							+ "\t" + String.format("%-20s", "BotName") + "||"
							+ "\t" + String.format("%-20s", "ItemName") + "||"
							+ "\tCount\t||"
							+ "\tGP/Hour\t||"
							+ "\tXP/Hour\t||");
		System.out.println("||------------------------------------------------------------------------------------------------------------------------------||");

		for (int i=0; i<numOfKeys; i++) {
			
			botId = reportKeys[i][0];
			itemId = reportKeys[i][1];
			botName = botNames[i];
			itemName = itemNames[i];
			
			numOfItems = updateNumOfItems(botId, itemId);
			gpRate = updateGPRate(numOfItems, botId, itemId);
			xpRate = updateXPRate(numOfItems, botId, itemId);
			
			System.out.println(	"||\t" + botId + "\t|" + 
								"|\t" + itemId + "\t|" + 
								"|\t" + String.format("%-20s", botName) + "|" +
								"|\t" + String.format("%-20s", itemName) + "|" +
								"|\t" + numOfItems + "\t|" + 
								"|\t" + gpRate + "\t|" +
								"|\t" + xpRate + "\t||");			
		}
		
		System.out.println("||------------------------------------------------------------------------------------------------------------------------------||");
		Thread.sleep(5000);
	}
	
	//Writes to "invCountAfter_botId_itemId.txt" the current inventory count of the bot with id=botId 
	//and collecting item with id=itemId when called
	public void writeAfter (int invCount, int botId, int itemId) {
		
		String afterPath = System.getProperty("user.dir") + File.separator + 
				"tempdata" + File.separator + "invCountAfter" + "_" + botId + "_"
				+ itemId + ".txt";
		
		try {
			fwAfter = new FileWriter (afterPath);
			pwAfter = new PrintWriter (fwAfter);
			pwAfter.println(Integer.toString(invCount));
			pwAfter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Writes to "invCountBefore_botId_itemId.txt" the current inventory count of the bot with id=botId 
	//and collecting item with id=itemId when called
	public void writeBefore (int invCount, int botId, int itemId) {
		
		String beforePath = System.getProperty("user.dir") + File.separator + 
				"tempdata" + File.separator + "invCountBefore" + "_" + botId + "_"
				+ itemId + ".txt";
		
		try {
			fwBefore = new FileWriter (beforePath);
			pwBefore = new PrintWriter (fwBefore);
			pwBefore.println(Integer.toString(invCount));
			pwBefore.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Reads the inventory count written in invCountAfter_botId_itemId.txt
	private int readAfter (int botId, int itemId) {
		
		String afterPath = System.getProperty("user.dir") + File.separator + 
				"tempdata" + File.separator + "invCountAfter" + "_" + botId + "_"
				+ itemId + ".txt";
		
		int result = 0;
		String buff;
		
		try {
			frAfter = new FileReader (afterPath);
			brAfter = new BufferedReader (frAfter);
			
			while((buff = brAfter.readLine()) != null) {
				result = Integer.parseInt(buff);
			}
			
			brAfter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	//Reads the inventory count written in invCountBefore_botId_itemId.txt
	private int readBefore (int botId, int itemId) {
		
		String beforePath = System.getProperty("user.dir") + File.separator + 
				"tempdata" + File.separator + "invCountBefore" + "_" + botId + "_"
				+ itemId + ".txt";
		
		int result = 0;
		String buff;
		
		try {
			frBefore = new FileReader (beforePath);
			brBefore = new BufferedReader (frBefore);
			
			while((buff = brBefore.readLine()) != null) {
				result = Integer.parseInt(buff);
			}
			
			brBefore.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
		
	//Updates the numOfItems collected by the bot given the botId and the itemId of the item they are collecting
	public int updateNumOfItems(int botId, int itemId) throws Exception {
		
		PreparedStatement prepState = null;
		Connection conn = null;
		ResultSet result;
		int numOfItems = 0;
		int numCollected;
		
		int beforeValue = readBefore(botId, itemId);
		int afterValue = readAfter(botId, itemId);
		
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
		writeBefore(afterValue, botId, itemId);

		return numCollected;
							
	}

	//Updates the gpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateGPRate (int numCollected, int botId, int itemId) throws Exception {
		int gpRate = 0, gpPerItem = 0;
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		//Obtaining the price per item with given itemID
		String obtainGPPerItem = "SELECT ItemPrice "
								+ "FROM Item "
								+ "WHERE ItemID = ?";
			
		conn = getConnection();
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
				
		return gpRate;
	}
	
	//Updates the xpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateXPRate (int numCollected, int botId, int itemId) throws Exception {
		int xpRate = 0, xpPerItem = 0;
		
		Connection conn = null;
		PreparedStatement prepState = null;
		ResultSet result;
		
		//Obtaining the price per item with given itemID
		String obtainXPPerItem = "SELECT ItemXp "
								+ "FROM Item "
								+ "WHERE ItemID = ?";
			
		conn = getConnection();
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
				
		return xpRate;
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
	
	//Returns the number of tuples in the relation REPORT. In otherwords the number of active bots
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
	
	//Connects to the database
	public Connection getConnection() throws Exception {
		
		try {
			Class.forName(this.driver);
			Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);			
			return conn;
		
		} catch (Exception e) {System.out.println (e);}
		
		return null;
	}
	
}
