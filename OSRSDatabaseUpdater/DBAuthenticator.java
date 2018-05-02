package dbbotconnector;

import java.io.Console;
import java.util.Scanner;

public class DBAuthenticator {
	
	private String user, pass, dbUrl, driver = "com.mysql.jdbc.Driver";
	private Scanner sc;
	private DBUpdater db;
	
	public DBAuthenticator (){
		this.sc = new Scanner(System.in);
	}
	
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
		
		try {
			db.getConnection();
		} catch (Exception e) {
			System.out.println("No connection has been made... Retry login!");
			return false;
		}
		
		return true;
	}
	
	public void setUser() {
		System.out.print("Enter username: ");
		this.user = sc.next();
	}
	
	public void setPass() {
		Console console = System.console();
		this.pass = String.valueOf(console.readPassword("Enter password: "));
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
}
