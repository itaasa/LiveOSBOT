package scripts;

import java.io.IOException;

public class Test {

	public static void main(String args[]) throws InterruptedException, IOException {
		
		int x = 0;
		
		String inDriver = "com.mysql.jdbc.Driver";
		String inUrl = "jdbc:mysql://localhost:3306/liveosbotdb";
		String inUser = "root";
		String inPass = "";

		DBUpdater db = new DBUpdater(inDriver, inUrl, inUser, inPass, 12, 1511);
				
		while (true) {
	
			try {
				x = db.updateNumOfItems();
				db.updateGPRate(x);
				db.updateXPRate(x);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------------------------------------------");
			Thread.sleep(10000);
		}
	}

}
