package dbbotconnector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Methods called by bot scripts will write to bot data 
 * 				text files
 *
 * HOW: 	1. 	Data is passed by parameters of bots and is then written 
 * 				to text file (FileWriter, PrintWriter)
 *			
 * WHY:		1. 	Data needs to be written by the bots in a readable and more
 * 				manageable way 
 * ---------------------------------------------------------------------- */

public class BotWriter {

	//File access objects
	private FileWriter fWrite;
	private PrintWriter pWrite;
	
	//Path to count, status and level data files
	private String botDataPath;
	
	public BotWriter () {
		botDataPath = System.getProperty("user.dir");
	}
	
	public BotWriter (String inputPath) {
		botDataPath = inputPath;
	}
	
	//Writes to "invCountAfter_botId_itemId.txt" the current inventory count of the bot with id=botId 
	//and collecting item with id=itemId when called
	public void writeAfter (int invCount, int botId, int itemId) {
		
		String invAfterPath = botDataPath + File.separator + 
				"itemdata" + File.separator + "invCountAfter" + "_" + botId + "_"
				+ itemId + ".txt";
		
		try {
			fWrite = new FileWriter (invAfterPath);
			pWrite = new PrintWriter (fWrite);
			pWrite.println(Integer.toString(invCount));
			pWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//Writes to "invCountBefore_botId_itemId.txt" the current inventory count of the bot with id=botId 
	//and collecting item with id=itemId when called
	public void writeBefore (int invCount, int botId, int itemId) {
		
		String invBeforePath = botDataPath + File.separator + 
				"itemdata" + File.separator + "invCountBefore" + "_" + botId + "_"
				+ itemId + ".txt";
		
		try {
			fWrite = new FileWriter (invBeforePath);
			pWrite = new PrintWriter (fWrite);
			pWrite.println(Integer.toString(invCount));
			pWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//Write online status to "onlineStatus_botId.txt" for bot with id=botId
	public void writeStatus (int botId, int status) {
		
		String statusPath = botDataPath + File.separator + 
				"statusdata" + File.separator + "onlineStatus" + "_" + botId + ".txt";
		
		try {
			fWrite = new FileWriter (statusPath);
			pWrite = new PrintWriter (fWrite);
			pWrite.println(Integer.toString(status));
			pWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Writes to file the currentLevel of botSkill and the xpNextLevel
	public void writeLevelData (int currentLevel, int currentXp, int xpNextLevel, int botId, int itemId) {
		String levelPath = botDataPath + File.separator + 
				"leveldata" + File.separator + "levelCount" + "_" + botId + "_"
				+ itemId + ".txt";
		
		try {
			fWrite = new FileWriter (levelPath);
			pWrite = new PrintWriter (fWrite);
			pWrite.println(Integer.toString(currentLevel));
			pWrite.println(Integer.toString(currentXp));
			pWrite.println(Integer.toString(xpNextLevel));
			pWrite.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Writes to bot text file which world they are in
	public void writeWorldData (int botId, int world) {
		String worldPath = botDataPath + File.separator + 
				"worlddata" + File.separator + "world" + "_" + botId + ".txt";
		
		try {
			fWrite = new FileWriter (worldPath);
			pWrite = new PrintWriter (fWrite);
			pWrite.println(Integer.toString(world));
			pWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
