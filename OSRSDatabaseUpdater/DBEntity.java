package dbbotconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBEntity {
	
	private String driver, url, user, pass;				//Database connection data									
	private Connection conn;
	private PreparedStatement prepState;
	private ResultSet result;
	
	public DBEntity (String driver, String url, String user, String pass) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		
		conn = getConnection();
		prepState = null;
		result = null;
	}
	
	
	//Establishes connection to the database
	public Connection getConnection() {
		
		try {
			Class.forName(this.driver);
			Connection conn = DriverManager.getConnection(this.url, this.user, this.pass);			
			return conn;
		
		} catch (Exception e) {
			System.out.println ("No connection to database!\n"
					+ "1. Check if XAMPP server is running.\n"
					+ "2. Check if login information is correct.");
		}
		
		return null;
	}
	
	//If an SQLException is thrown, this function will be called to handle it
	private void close() {
		
	}
}
