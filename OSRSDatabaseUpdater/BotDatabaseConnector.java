package dbbotconnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BotDatabaseConnector {
	
	private static DBAuthenticator dbAuth;
	private static DBUpdater dbUp;
	
	public static void main (String args[]) throws Exception{

		displayHeaderText();
		pressAnyKeyToContinue();

		connectToDB ("jdbc:mysql://localhost:3306/liveosbotdb");
		
		dbUp.preProc();
		
		while (true) {
			dbUp.executeProc();
		}
		
	}
	
	private static void connectToDB (String dbURL) {
		
		dbAuth = new DBAuthenticator (dbURL);
		
		System.out.println("Login to database...");
		
		do {
			dbAuth.setUser();
			dbAuth.setPass();
			dbUp = dbAuth.authenticate();
			
			if (dbUp == null) {
				System.out.println("Invalid login information.");
			}
		} while (dbUp == null);
	}
	
	public static void displayHeaderText () {
		FileReader fr;
		BufferedReader br;
		String buff;
		
		String path = System.getProperty("user.dir") + File.separator + "introtext" + File.separator 
				+ "header.txt";
		
		try {
			fr = new FileReader (path);
			br = new BufferedReader (fr);
			
			while((buff = br.readLine()) != null) {
				System.out.println(buff);
			}
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public static void pressAnyKeyToContinue(){ 
	        System.out.println("Press the enter to key to continue... ");
	        try { System.in.read();}  
	        catch(Exception e){}  
	 }
	
}
