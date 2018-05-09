package dbbotconnector;

import java.sql.SQLException;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Updates the following database information: 
 *				Items Collected, XP/GP Rates, Levels
 *
 * HOW: 	1. 	Establish connection with database (Connection)
 * 			2. 	Read information sent from bots (BotReader)
 *			3. 	Create then execute an SQL Update Query 
 *				with the values found in 2. (PreparedStatement)
 *				
 * WHY:		1. 	Allows for live information to be sent to database
 * 				as bots are collecting items, gaining experience/gold
 * ---------------------------------------------------------------------- */

public class DBUpdater extends DBEntity {
	
	private BotReader botRead;		
	private BotWriter botWrite;					///Read and write to bot data text files	
	private DBSelector dbSelect; 				//Obtains data from bot database	
		
	public DBUpdater(String driver, String url, String user, String pass) {
		
		super(driver, url, user, pass);

		this.botRead = new BotReader();
		this.botWrite = new BotWriter();	
		
		dbSelect = new DBSelector (driver, url, user, pass);
	}
	
	//Updates the database with all the values passed by ALL bots currently running (this uses all methods)
	public void executeProc() throws Exception{
		
		int [][] reportKeys = dbSelect.getReportKeys();
		
		int [] totalItemsCollected = dbSelect.getItemCounts();
		int totalNumOfItems, numOfItems, gpRate, xpRate, numOfKeys = dbSelect.getNumOfReportKeys(), 
				botId, itemId;
		
		String [] botNames = dbSelect.getBotNames();
		String [] itemNames = dbSelect.getItemNames();
		String botName, itemName;
		
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
	public int updateNumOfItems(int botId, int itemId) {
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
		
		//Obtaining the current NumOfItems from the DB
		try {
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
			
		} catch (SQLException e) {
			handler();
			
		}
		//Resetting before value to after value
		botWrite.writeBefore(afterValue, botId, itemId);

		return numCollected;
							
	}

	//Updates the gpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateGPRate (int numCollected, int botId, int itemId) {
		int gpRate = 0, gpPerItem = 0;
			
		if (numCollected != 0) {
			
			//Obtaining the price per item with given itemID
			String obtainGPPerItem = "SELECT ItemPrice "
									+ "FROM Item "
									+ "WHERE ItemID = ?";
			
			try {
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
			catch (SQLException e) {
				handler();
			}
		} 
		
		else {
			
			//Gets the past rate when numCollected was non-zero integer (used to avoid 0 data values for rates)
			String obtainGPRate = "SELECT GpPerHour "
								+ "FROM Report "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			conn = getConnection();
			try {
				prepState = conn.prepareStatement(obtainGPRate);
			
				prepState.setInt(1, botId);
				prepState.setInt(2, itemId);
				result = prepState.executeQuery();
				
				while(result.next())
					gpRate = result.getInt("GpPerHour");
			
			} catch (SQLException e) {
				handler();
			}
			
		}
				
		return gpRate;
	}
	
	//Updates the xpPerHour collected by the bot given the botId and the itemId of the item they are collecting
	private int updateXPRate (int numCollected, int botId, int itemId){
		int xpRate = 0, xpPerItem = 0;
		
		if (numCollected != 0) {
		//Obtaining the price per item with given itemID
			String obtainXPPerItem = "SELECT ItemXp "
									+ "FROM Item "
									+ "WHERE ItemID = ?";
				
			try {
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
			
			} catch (SQLException e) {
				handler();
			}
			
		}
		
		else {
			//Gets the past rate when numCollected was non-zero integer
			String obtainGPRate = "SELECT XpPerHour "
								+ "FROM Report "
								+ "WHERE BotID = ? "
								+ "AND ItemID = ?";
			
			try {
				prepState = conn.prepareStatement(obtainGPRate);
		
				prepState.setInt(1, botId);
				prepState.setInt(2, itemId);
				result = prepState.executeQuery();
				
				while(result.next())
					xpRate = result.getInt("XpPerHour");
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return xpRate;
	}
	
	//Updates the current level, xpNextLevel and timeNextLevel for corresponding botId and itemId 
	private int [] updateLevelData(int xpRate, int botId, int itemId) {
		
		int [] levelData = botRead.readLevelData(botId, itemId);
		int curLvl = levelData[0];
		int totalXp = levelData[1];
		int xpNextLvl = levelData[2];
		float timeNextLevel = (float) xpNextLvl / xpRate;
		
		String updateQuery = "UPDATE Report "
							+ "SET CurrentLevel = ?, TotalXp = ?, XpNextLevel  = ?, TimeNextLevel = ? "
							+ "WHERE BotID = ? AND ItemID = ?";
			
		try {
			prepState = conn.prepareStatement(updateQuery);

			prepState.setInt(1, curLvl);
			prepState.setInt(2, totalXp);
			prepState.setInt(3, xpNextLvl);
			prepState.setString(4, floatToTimeString(timeNextLevel));
			prepState.setInt(5, botId);
			prepState.setInt(6, itemId);
			prepState.executeUpdate();
			
		} catch (SQLException e) {
			handler();
		}
		
		return levelData;
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
	
	public void updateStatus (int botId){
		
		int status = botRead.readStatus(botId);
		
		String updateQuery = "UPDATE Bot "
							+ "SET IsOnline = ? "
							+ "WHERE BotID = ?";

		try {
			prepState = conn.prepareStatement(updateQuery);
	
			prepState.setInt(1, status);
			prepState.setInt(2, botId);
			prepState.executeUpdate();
			
		} catch (SQLException e) {
			handler();
		}
	}
}
