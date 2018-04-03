package scripts;

import java.io.IOException;

public class Test {

	public static void main(String args[]) throws Exception {

		String inDriver = "com.mysql.jdbc.Driver";
		String inUrl = "jdbc:mysql://localhost:3306/liveosbotdb";
		String inUser = "root";
		String inPass = "";
		DBUpdater db = new DBUpdater(inDriver, inUrl, inUser, inPass);
		
		while (true){
			db.executeProc();
		}
	}

}
