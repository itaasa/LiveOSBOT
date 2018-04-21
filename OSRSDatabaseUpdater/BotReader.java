package scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BotReader {
	
	//File access objects
	FileReader fRead;
	BufferedReader bRead;
	
	
	public BotReader () {
		
	}
	
	//Reads the inventory count written in invCountAfter_botId_itemId.txt
	public int readAfter (int botId, int itemId) {
		
		String invAfterPath = System.getProperty("user.dir") + File.separator + 
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
		
		String beforePath = System.getProperty("user.dir") + File.separator + 
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
		String path = System.getProperty("user.dir") + File.separator + 
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
		String path = System.getProperty("user.dir") + File.separator + 
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