package dbbotconnector;

import java.io.Console;
import java.util.Scanner;

/* ----------------------------------------------------------------------
 * WHAT: 	1.	Allows user to enter login information such as user name 
 * 				and password
 * 			2.  If user name and password are correct, connection will
 * 				be established
 *
 * HOW: 	1. 	Prompt user to enter login data (setUser, setPass)
 * 			2. 	If login information is correct, the testConnection
 * 				method will return a DBUpdater object, else it will
 * 				return null (which will be handled)
 *				
 * WHY:		1. 	Aids in console login prompt compatibility
 * ---------------------------------------------------------------------- */

public class DBAuthenticator {
	
	private String user, pass, dbUrl;
	private final String driver = "com.mysql.jdbc.Driver";
	private Scanner sc;
	private DBUpdater db;
		
	public DBAuthenticator (String dbUrl) {
		this.dbUrl = dbUrl;
		this.sc = new Scanner(System.in);
	}
	
	public DBUpdater authenticate () {
		if (testConnection()) {
			sc.close();
			return db;
		}
		else
			return null;
	}
	
	private boolean testConnection () {
		
		db = new DBUpdater (driver, dbUrl, user, pass);	
		db.getConnection();
	
		return true;
	}
	
	public void setUser() {
		System.out.print("Enter username: ");
		this.user = sc.next();
	}
	
	public void setPass() {
		
		Console console = System.console();
		this.pass = String.valueOf(console.readPassword("Enter password: "));
		
		//System.out.print("Enter password: ");
		//this.pass = sc.next();
	}
	
	public void setDBUrl (String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public String getDriver (){
		return driver;
	}
}
