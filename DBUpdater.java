package scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class DBUpdater {
	
	private String driver = "", url = "", user = "", pass = "";
	private FileReader frBefore, frAfter;
	private BufferedReader brBefore, brAfter;
	private FileWriter fwBefore, fwAfter;
	private PrintWriter pwBefore, pwAfter;
	private String beforePath = System.getProperty("user.dir") + File.separator + "invCountAfter.txt";
	private String afterPath = System.getProperty("user.dir") + File.separator + "invCountBefore.txt";
	private int botID, itemID;
	
	public DBUpdater(String driver, String url, String user, String pass, int botID, int itemID) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.botID = botID;
		this.itemID = itemID;
	}
	
	public int updateNumOfItems() throws Exception {
		
		PreparedStatement prepState = null;
		Connection conn = null;
		ResultSet result;
		int numOfItems = 0;
		
		int beforeValue = readInvCountBefore();
		int afterValue = readInvCountAfter();
		
		int numCollected = beforeValue - afterValue;
				
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
		
		
		prepState.setInt(1, botID);
		prepState.setInt(2, itemID);
		
		result = prepState.executeQuery();
		
		while (result.next())
			numOfItems = result.getInt("NumOfItems");
		
		if (numCollected > 0)
			numOfItems += numCollected;
	
		//Updating the current NumOfItems from the DB
		prepState = conn.prepareStatement(updateNumOfItems);
		prepState.setInt(1, numOfItems);
		prepState.setInt(2, botID);
		prepState.setInt(3, itemID);
		prepState.executeUpdate();
		
		
		//Resetting before value to after value
		writeInvCountBefore(afterValue);
		System.out.println("UPDATED REPORT(" +this.botID+", " + this.itemID + 
				", NumOfItems)\t" + numCollected);
		
		if (numCollected > 0)
			return numCollected;
		return 0;
							
	}
	
	private int readInvCountBefore() {
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
	
	private int readInvCountAfter() {
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
		
	public void writeInvCountBefore(int x) {
		try {
			fwBefore = new FileWriter (beforePath);
			pwBefore = new PrintWriter (fwBefore);
			pwBefore.println(Integer.toString(x));
			pwBefore.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeInvCountAfter(int x) {
		try {
			fwAfter = new FileWriter (afterPath);
			pwAfter = new PrintWriter (fwAfter);
			pwAfter.println(Integer.toString(x));
			pwAfter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int updateGPRate (int numCollected) throws Exception {
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
		prepState.setInt(1, this.itemID);
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
		prepState.setInt(2, this.botID);
		prepState.setInt(3, this.itemID);
		prepState.executeUpdate();
		
		System.out.println("UPDATED REPORT(" +this.botID+", " + this.itemID + 
				", GpPerHour)\t" + gpRate);
		
		return gpRate;
	}
	
	public int updateXPRate (int numCollected) throws Exception {
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
		prepState.setInt(1, this.itemID);
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
		prepState.setInt(2, this.botID);
		prepState.setInt(3, this.itemID);
		prepState.executeUpdate();
		
		System.out.println("UPDATED REPORT(" +this.botID+", " + this.itemID + 
				", XpPerHour)\t" + xpRate);
		
		return xpRate;
	}
	
	public Connection getConnection() throws Exception {
		
		try {
			Class.forName(this.driver);
			Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);			
			return conn;
		
		} catch (Exception e) {System.out.println (e);}
		
		return null;
	}
	
}
