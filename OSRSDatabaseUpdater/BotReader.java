package dbbotconnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Reads live bot data from data text files which are 
 * 				updated by the bot scripts
 *
 * HOW: 	1. 	Reads from text file and returns values found (FileReader,
 * 				BufferedReader)
 *			
 * WHY:		1. 	Information sent by the bot scripts need to be processed 
 * 				and or sent to the database
 * ---------------------------------------------------------------------- */

public class BotReader {
	
	//File access objects
	private FileReader fRead;
	private BufferedReader bRead;
	
	//Path to count, status and level data files
	private String botDataPath;
	
	public BotReader () {
		botDataPath =  System.getProperty("user.dir");
	}
	
	public BotReader (String inputPath) {
		botDataPath = inputPath;
	}
	
	//Reads the inventory count written in invCountAfter_botId_itemId.txt
	public int readAfter (int botId, int itemId) {
		
		String invAfterPath = botDataPath + File.separator + 
				"itemdata" + File.separator + "invCountAfter" + "_" + botId + "_"
				+ itemId + ".txt";
		
		int result = 0;
		String buff;
		
		try {
			fRead = new FileReader (invAfterPath);
			bRead = new BufferedReader (fRead);
			
			while((buff = bRead.readLine()) != null) {
				result = Integer.parseInt(buff);
			}
			
			bRead.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	//Reads the inventory count written in invCountBefore_botId_itemId.txt
	public int readBefore (int botId, int itemId) {
		
		String beforePath = botDataPath + File.separator + 
				"itemdata" + File.separator + "invCountBefore" + "_" + botId + "_"
				+ itemId + ".txt";
		
		int result = 0;
		String buff;
		
		try {
			fRead = new FileReader (beforePath);
			bRead = new BufferedReader (fRead);
			
			while((buff = bRead.readLine()) != null) {
				result = Integer.parseInt(buff);
			}
			
			bRead.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	//Returns the xp data set consisting of the current level of botSkill and xp till next level
	public int [] readLevelData(int botId, int itemId) {
		int [] levelData = new int [3];
		String path = botDataPath + File.separator + 
				"leveldata" + File.separator + "levelCount" + "_" + botId + "_"
				+ itemId + ".txt";
		
		String buff;
		int i=0;
		
		try {
			fRead = new FileReader (path);
			bRead = new BufferedReader (fRead);
			
			while((buff = bRead.readLine()) != null) {
				levelData[i]= Integer.parseInt(buff);
				i++;
			}
			
			bRead.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return levelData;
	}
	
	//Returns the status of the bot with id = botId
	public int readStatus(int botId) {
		String path = botDataPath + File.separator + 
				"statusdata" + File.separator + "onlineStatus" + "_" + botId + ".txt";
		

		int result = 0;
		String buff;
		
		try {
			fRead = new FileReader (path);
			bRead = new BufferedReader (fRead);
			
			while((buff = bRead.readLine()) != null) {
				result = Integer.parseInt(buff);
			}
			
			bRead.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
