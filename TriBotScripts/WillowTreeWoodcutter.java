package scripts;

import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import dbbotconnector.BotWriter;

@ScriptManifest (authors = {"Polygon Window"}, category = "Woodcutting", name = "Willow Tree Woodcutter")

public class WillowTreeWoodcutter extends Script {
	
	BotWriter botWrite = new BotWriter ();
	private final int botId = 0;
	
	//ItemID's to be referenced later
	private final int WILLOW_TREE_ID [] = {1750, 1756};
	private final int WILLOW_LOG_ID = 1519;
	
	private final int treeAreaRadius = 9;
	private final RSTile treeCenter = new RSTile (2968, 3197, 0);
	private final RSArea treeArea = new RSArea (treeCenter, treeAreaRadius);
	Timer time = new Timer (3000);

	
	@Override
	public void run() {
		if(onStart())
			while (true){
				loop();
				sleep(140);
				
				if (Login.getLoginState() == Login.STATE.UNKNOWN || Login.getLoginState() == Login.STATE.LOGINSCREEN){
					break;
				}
			}
		
		println ("Now writing the bot as offline.");
		botWrite.writeStatus(botId, 0);
	}
	
	private boolean onStart() {
		
		if (Login.getLoginState() == Login.STATE.INGAME) {			
			botWrite.writeStatus(botId, 1);
			botWrite.writeBefore(Inventory.getCount(WILLOW_LOG_ID), botId, WILLOW_LOG_ID);
			botWrite.writeAfter(Inventory.getCount(WILLOW_LOG_ID), botId, WILLOW_LOG_ID);
			botWrite.writeBefore(Inventory.getCount(WILLOW_LOG_ID), botId, WILLOW_LOG_ID);
			botWrite.writeLevelData(Skills.getCurrentLevel(SKILLS.WOODCUTTING), 
									Skills.getXP(SKILLS.WOODCUTTING), 
									Skills.getXPToNextLevel(SKILLS.WOODCUTTING), 
									botId, WILLOW_LOG_ID);
			
			botWrite.writeWorldData(botId, WorldHopper.getWorld());
		}
		
		else
			botWrite.writeStatus(botId, 0);
		
		return true;
	}

	private boolean loop() {
		
		botWrite.writeAfter(Inventory.getCount(WILLOW_LOG_ID), botId, WILLOW_LOG_ID);
		botWrite.writeLevelData(Skills.getCurrentLevel(SKILLS.WOODCUTTING), 
				Skills.getXP(SKILLS.WOODCUTTING), 
				Skills.getXPToNextLevel(SKILLS.WOODCUTTING), 
				botId, WILLOW_LOG_ID);
		
		if (Inventory.isFull()) {
			println ("Bot has full inventory, now dropping logs...");
			Inventory.drop(WILLOW_LOG_ID);
		}
		
		else {
		
			if (nearTreeArea())
				cutTrees();
						
			else 
				moveToTreeArea();			
		}
		
		return true;
	}
	
	private void cutTrees(){
		if (Player.getRSPlayer().getAnimation() == -1){
			RSObject foundTree = findNearest (15, WILLOW_TREE_ID);
			
			if (foundTree != null) {
				if (foundTree.isOnScreen()) {
					foundTree.click("Chop down");
					time.reset();
					while (Player.getRSPlayer().getAnimation() == -1 && time.isRunning()){
						sleep(10);
					}
				}
			}
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
	
	private void moveToTreeArea () {
		WebWalking.walkTo(treeCenter.getPosition());
	}
	
	private boolean nearTreeArea() {
		if (treeArea.contains(Player.getRSPlayer().getPosition().getPosition()))
			return true;
	
		return false;
	}
	
}
