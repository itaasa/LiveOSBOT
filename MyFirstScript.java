package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import org.tribot.api2007.Banking;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;


@ScriptManifest(authors = { "Polygon Window" }, category = "Woodcutting", name = "Normal tree woodcutter")
public class MyFirstScript extends Script implements Painting {

	private final int[] NORMAL_TREE_ID = {1276, 1278};
	private final int LOGS = 1511, AXE = 1351;
	private final int[] BANKER = {7478, 7409};
	private final RSTile rectPosX = new RSTile (3282, 3455, 0);
	private final RSTile rectPosY = new RSTile (3272, 3446, 0);
	private final RSArea treeArea = new RSArea (rectPosX, rectPosY);
	Timer time = new Timer (3000);
	
	static String inDriver = "com.mysql.jdbc.Driver";
	static String inUrl = "jdbc:mysql://localhost:3306/liveosbotdb";
	static String inUser = "root";
	static String inPass = "";
	static int botID = 12, itemID = 1511;
	static DBUpdater db = new DBUpdater (inDriver, inUrl, inUser, inPass, botID, itemID);
	
	// code that needs to run before it runs the script
	private boolean onStart() {
		
		db.writeInvCountAfter(Inventory.getCount(LOGS));
		db.writeInvCountBefore(Inventory.getCount(LOGS));
		
		return true;
	}

	@Override
	public void run() {
				
		if (onStart()) {
			while (true) {
				sleep(loop());
			}
		}
	} 

	

	public RSObject findNearest(int distance, int... ids) {
		RSObject[] trees = Objects.findNearest(distance, ids);

		for (RSObject inputTree : trees) {
			if (inputTree != null) {
				return inputTree;
			}
		}

		return null;
	}

	public int loop() {
		
		if (Inventory.isFull()) {
			if(bankTrip()) 
				println ("Smoke");

		}

		else {
			if (Player.getRSPlayer().getAnimation() == -1) {
				
				db.writeInvCountAfter(Inventory.getCount(LOGS));
				
				if (!isNearTrees()) {
					WebWalking.walkTo(treeArea.getRandomTile());
				}
				
				else {
					RSObject tree = findNearest(15, NORMAL_TREE_ID);
	
					if (tree != null) {
						if (tree.isOnScreen()) {
							if (tree.click("Chop down")) {

							}						
							time.reset();
							while (Player.getRSPlayer().getAnimation() == -1 && time.isRunning()) {
								sleep(10);
							}
							
						}
					}
				}
			}
		}
		return 150;
	}
	
	public boolean bankTrip() {
	
		if (WebWalking.walkToBank()) {
			RSObject banker = findNearest (15, BANKER);
			
			if (banker.isOnScreen()) {
				banker.click("Bank");
				Banking.depositAllExcept(AXE);
				Banking.close();
				
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean isNearTrees () {
		if (treeArea.contains(Player.getRSPlayer().getPosition()))
			return true;
		
		return false;
	}
	
	@Override
	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.GREEN);
		g.drawString("Script running: Woodcutter", 380, 330);
	}

}
