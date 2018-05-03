package dbbotconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBEntity {
	
	private String driver, url, user, pass;				//Database connection data									
	protected Connection conn;
	protected PreparedStatement prepState;
	protected ResultSet result;
	
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
			handler();
		}
		
		return null;
	}
	
	//If an SQLException is thrown, this function will be called to handle it
	protected void handler() {
		System.out.println("Database connection error!\n"
					+ "Now closing connection, prepared statement and result sets...\n"
					+ "Stopping application...");
		
		try {
			if (result!= null)
				result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			if (prepState != null)
				prepState.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
