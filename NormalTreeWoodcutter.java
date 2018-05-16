package scripts;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import dbbotconnector.BotWriter;

@ScriptManifest (authors = {"Polygon Window"}, category = "Woodcutting", name = "Normal Tree Woodcutter")

public class NormalTreeWoodcutter extends Script {

	BotWriter botWrite = new BotWriter();
	
	//The following are ID's of in-game object needed to be referenced in the script
	private final int BRONZE_AXE_ID = 1351;
	private final int NORMAL_TREE_ID [] = {1276};
	private final int LOG_ID = 1511;
	private final int botId = 1;
	
	//The following data for tiles (coordinates in game) used to describe the tree area the bot will obtain trees
	private final int areaRadius = 8;
	private final RSTile treeCenter = new RSTile (3276, 3450, 0);;
	private final RSArea treeArea = new RSArea (treeCenter, areaRadius);
	
	//Timer will be used to ensure bot is more human like
	Timer time = new Timer(3000);
	
	public boolean onStart() {
		
		
		if (Login.getLoginState() == Login.STATE.INGAME) {
			
			println ("Bot is online, now writing start up data...");
			botWrite.writeStatus(botId, 1);
			botWrite.writeAfter(Inventory.getCount(LOG_ID), botId, LOG_ID);
			botWrite.writeBefore(Inventory.getCount(LOG_ID), botId, LOG_ID);
			botWrite.writeLevelData(Skills.getCurrentLevel(SKILLS.WOODCUTTING), 
									Skills.getXP(SKILLS.WOODCUTTING), 
									Skills.getXPToNextLevel(SKILLS.WOODCUTTING), 
									botId, LOG_ID);
			
			botWrite.writeWorldData(botId, WorldHopper.getWorld());
		}
		
		else
			botWrite.writeStatus(botId, 0);
		
		return true;
	}
	
	@Override
	public void run() {
		if (onStart())
			while(true){
				loop();
				sleep (150);
				
				if (Login.getLoginState() == Login.STATE.UNKNOWN || Login.getLoginState() == Login.STATE.LOGINSCREEN)
					break;
			}
		
		println ("Now writing the bot as offline.");
		botWrite.writeStatus(botId, 0);
		
	}
	
	private boolean loop ()
	{
		botWrite.writeAfter(Inventory.getCount(LOG_ID), botId, LOG_ID);	
		botWrite.writeLevelData(Skills.getCurrentLevel(SKILLS.WOODCUTTING), 
				Skills.getXP(SKILLS.WOODCUTTING), 
				Skills.getXPToNextLevel(SKILLS.WOODCUTTING), 
				botId, LOG_ID);
				
		//First we check if bot has a full inventory, if so we must deposit excess items to the nearest bank
		if (Inventory.isFull()){
			println ("Inventory is full, now going to bank...");
			bankTrip();
		}
		
		//If the inventory is not full, bot will move to tree area
		else {
			
			if (nearTreeArea()) {
				cutTrees();
			}
			
			else {
				moveToTreeArea();
			}
		}
		
		return true;
	}
	
	//Locates and cuts trees in a 15-tile radius from bot
	private void cutTrees(){
		
		if (Player.getRSPlayer().getAnimation() == -1)
		{
			RSObject foundTree = findNearest(15, NORMAL_TREE_ID);
			
			if (foundTree != null) {
				if (foundTree.isOnScreen()) {
					println (foundTree.getID());
					if (foundTree.isClickable()) {
						foundTree.click("Chop down");
						time.reset();
					}
					while (Player.getRSPlayer().getAnimation() == -1 && time.isRunning()){
						sleep(10);
					}
				}
			}
			else
				println ("No tree was found...");
		}
		
	}
	
	//Returns the nearest RSObject one of following IDs, a certain distance from bot
	private RSObject findNearest (int distance, int...ids){
		RSObject[] trees = Objects.findNearest(distance, ids);
		
		for (RSObject foundTree : trees) {
			if (foundTree != null)
				return foundTree;
		}
		
		return null;
	}
	
	//Moves bot to tree area
	private void moveToTreeArea () {
		WebWalking.walkTo(treeCenter.getPosition());
	}
	
	//Checks if bot is in the tree area
	private boolean nearTreeArea (){
		if (treeArea.contains(Player.getRSPlayer().getPosition().getPosition())){
			return true;
		}
		
		return false;
	}
	
	//Will move bot to the bank to deposit logs
	private void bankTrip(){
		if(WebWalking.walkToBank()){
			sleep(500);
			
			if(Banking.openBank()){
				if(Banking.isBankScreenOpen()){
					Banking.depositAllExcept(BRONZE_AXE_ID);
					Banking.close();
				}
			}
			sleep(500);
		}
		
	}

}
