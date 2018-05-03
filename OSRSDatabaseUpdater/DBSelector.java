package dbbotconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Obtains IDs, bot names, item names, item counts from database
 *
 * HOW: 	1. 	Establish connection with database (Connection)
 *			2. 	Create then execute an SQL Query (PreparedStatement)
 *			3. 	Process result from query (ResultSet)
 *			
 * WHY:		1. 	IDs of bots and items will be needed for updating and 
 *				accessing bot data files. 
 * 			2. 	Names and item counts will be needed for console print 
 * 				statements
 * ---------------------------------------------------------------------- */


public class DBSelector {
	
	private String driver, url, user, pass;				//Database connection data									
	private Connection conn;
	private PreparedStatement prepState;
	private ResultSet result;
	
	public DBSelector (String driver, String url, String user, String pass) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		conn = getConnection();
	}
	
	//Establishes connection to the database
	public Connection getConnection() {
		
		try {
			Class.forName(this.driver);
			Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);			
			return conn;
		
		} catch (Exception e) {
			System.out.println ( "No connection to database!\n"
							+ "1. Check if XAMPP server is running.\n"
							+ "2. Check if login information is correct.");
			System.exit(0);
		}
		
		return null;
	}
	
	//Returns the primary key of table REPORT of all bots stored in database
	//PRIMARY KEY: {BotId, ItemId}
	public int [][] getReportKeys () throws Exception {
		
		int numOfReportKeys = getNumOfReportKeys();
		int [][] reportKeys = new int [numOfReportKeys][2];
		
		String obtainKeys =	"SELECT BotID, ItemID FROM Report";
		
		prepState = conn.prepareStatement(obtainKeys);
		result = prepState.executeQuery();
	
		for (int i=0; i<numOfReportKeys; i++) {
			result.next();
			reportKeys[i][0] = result.getInt("BotID");
			reportKeys[i][1] = result.getInt("ItemID");
		}
		
		return reportKeys;
	}
	
	//Returns number of report keys
	public int getNumOfReportKeys () throws Exception {
		
		int numOfKeys = 0;

		
		String obtainCountOfTuples = "SELECT * FROM Report";

		prepState = conn.prepareStatement(obtainCountOfTuples);
		result = prepState.executeQuery();
		
		result.last();
		numOfKeys = result.getRow();
		
		return numOfKeys;
	}
	
	//Returns names of all bots associated with their corresponding botId's found in table REPORT
	public String[] getBotNames() throws Exception {

		int numOfKeys = getNumOfReportKeys();
		String [] botNames = new String [numOfKeys];
		
		String obtainBotName = "SELECT BotName " 
							+ "FROM Report, Bot "
							+ "WHERE Report.BotID = Bot.BotID";
		
		prepState = conn.prepareStatement(obtainBotName);
		result = prepState.executeQuery();
	
		int i=0;
		
		while (result.next()) {
			botNames[i] = result.getString("BotName");
			i++;
		}
			
		return botNames;
	}
	
	//Returns names of all items associated with their corresponding itemId's found in table REPORT
	public String[] getItemNames() throws Exception {
		
		int numOfKeys = getNumOfReportKeys();
		String [] itemNames = new String [numOfKeys];
		
		String obtainBotName =  "SELECT ItemName " 
							+ "FROM Report, Item "
							+ "WHERE Report.ItemID = Item.ItemID";
	
		prepState = conn.prepareStatement(obtainBotName);
		result = prepState.executeQuery();
	
		int i=0;
	
		while (result.next()) {
			itemNames[i] = result.getString("ItemName");
			i++;
		}
		
		return itemNames;
	}
	
	//Returns all total collected items of each active bot
	public int [] getItemCounts () throws Exception {
		
		int numOfKeys = getNumOfReportKeys();
		
		int [] totalItems = new int [numOfKeys];
		
		String obtainNumOfItems =  "SELECT NumOfItems " 
							+ "FROM Report";
		
		prepState = conn.prepareStatement(obtainNumOfItems);
		result = prepState.executeQuery();
	
		int i=0;
	
		while (result.next()) {
			totalItems[i] = result.getInt("NumOfItems");
			i++;
		}
		
		return totalItems;
	}
}
