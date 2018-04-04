package scripts;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest (authors = {"Polygon Window"}, category = "Woodcutting", name = "Normal Tree Woodcutter")

public class NormalTreeWoodcutter extends Script {

	
	//The following are ID's of in-game object needed to be referenced in the script
	private final int BRONZE_AXE_ID = 1351;
	private final int NORMAL_TREE_ID [] = {1276, 1278};
	private final int LOG_ID = 1511;
	
	//The following data for tiles (coordinates in game) used to describe the tree area the bot will obtain trees
	private final int areaRadius = 8;
	private final RSTile treeCenter = new RSTile (3276, 3450, 0);;
	private final RSArea treeArea = new RSArea (treeCenter, areaRadius);
	
	//Timer will be used to ensure bot is more human like
	Timer time = new Timer(3000);
	
	//The following object will write to the database data that bot collects overtime
	static DBUpdater db = new DBUpdater (
			"com.mysql.jdbc.Driver",
			"jdbc:mysql://localhost:3306/liveosbotdb",
			"root", "");
	
	public boolean onStart() {
		
		//Setting the inventory text files counts to the current inventory count of the bot
		db.writeAfter(Inventory.getCount(LOG_ID), 0, 1511);
		db.writeBefore(Inventory.getCount(LOG_ID), 0, 1511);
		return true;
	}
	
	@Override
	public void run() {
		if (onStart())
			while(true){
				loop();
				sleep (150);
			}
	}
	
	private boolean loop ()
	{
		db.writeAfter(Inventory.getCount(LOG_ID), 0, 1511);
		
		//First we check if bot has a full inventory, if so we must deposit excess items to the nearest bank
		if (Inventory.isFull()){
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
