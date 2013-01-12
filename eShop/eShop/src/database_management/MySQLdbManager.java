/*
 * MySQL java database manager wrapper
 * ver. - 1.00
 * (C) 09.01.2013 - 12.01.2013 zhgzhg
 */

package database_management;

import java.sql.*;

public class MySQLdbManager {
	
	private final String JDBCDRIVER = "com.mysql.jdbc.Driver";
	private int mySqlServerPort = 3306;
	private String mySqlServerAddress = null;	
	private String mySqlDatabaseName = null;
	private String mySqlUsername = null;
	private String mySqlPassword = null;
	private Connection dbConnection = null;
	
	private String lastError = null;
	
	public MySQLdbManager() {
		
	}
	
	public MySQLdbManager(String mySqlServerAddress) {
		this.mySqlServerAddress = mySqlServerAddress;
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort, String mySqlUsername) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
		this.mySqlUsername = mySqlUsername;
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort, String mySqlUsername, String mySqlPassword) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
		this.mySqlUsername = mySqlUsername;
		this.mySqlPassword = mySqlPassword;
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName,  String mySqlUsername) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.mySqlUsername = mySqlUsername;		
	}
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName,  String mySqlUsername, String mySqlPassword) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.mySqlUsername = mySqlUsername;
		this.mySqlPassword = mySqlPassword;
	}
	
	public void setMySqlServerAddress(String mySqlServerAddress) { //needs to disconnect and connect to db again after that
		this.mySqlServerAddress = mySqlServerAddress;
	}
	
	public String getMySqlServerAddress() {
		return mySqlServerAddress;
	}
	
	public void setMySqlActiveDatabase(String mySqlDatabaseName) { //needs to disconnect and connect to db again after that
		this.mySqlDatabaseName = mySqlDatabaseName;
	}
	
	public String getMySqlActiveDatabase() {
		return mySqlDatabaseName;
	}
	
	public void setMySqlServerPort(int mySqlServerPort) { //needs to disconnect and connect to db again after that
		
		if (mySqlServerPort < 0 || mySqlServerPort > 65535)
		{ lastError = "Invalid port!"; return; }
		
		this.mySqlServerPort = mySqlServerPort;
	}
	
	public int getMySqlServerPort() {
		
		return this.mySqlServerPort;
	}
	
	public void setMySqlUsername(String mySqlUsername) { //needs to disconnect and connect to db again after that
		
		this.mySqlUsername = mySqlUsername;
	}
	
	public String getMySqlUsername() {
		
		return mySqlUsername;
	}
	
	public void setMySqlPassword(String mySqlPassword) { //needs to disconnect and connect to db again after that
		
		this.mySqlPassword = mySqlPassword;
	}
	
	public String getMySqlPassword() {
		
		return this.mySqlPassword;
	}
	
	public boolean isConnected() {
		
		boolean result = true;
		
		if (dbConnection != null) {
			
			try {
				
				result = (!dbConnection.isClosed());
			}			
			catch (SQLException ex) {
				
				lastError = ex.getMessage(); 
				result = false;
			}
		}		
		
		return result;
	}
	
	public boolean connect() {
		
		boolean result = true;
		
		if (mySqlServerAddress == null || mySqlDatabaseName == null || mySqlUsername == null)
		{ lastError = "Uninitialized server address, database name or database username!"; return false; }	
		
		if (dbConnection != null) {
			
			try {
				
				if (!dbConnection.isClosed()) {
					dbConnection.close();
				}
			}
			catch (SQLException ex) {
				lastError = ex.getMessage();
			}
		}			
		
		try {
			Class.forName(JDBCDRIVER).newInstance();
			dbConnection = DriverManager.getConnection("jdbc:mysql://" + mySqlServerAddress + ":" + mySqlServerPort + "/" + mySqlDatabaseName + 
					"?user=" + mySqlUsername + (mySqlPassword != null ? "&password=" + mySqlPassword : "") + "&useUnicode=TRUE&characterEncoding=UTF-8");
		}
		catch (Exception ex) {
			
			if (lastError == "null") { 
				lastError = ex.getMessage(); 
			}
			else { 
				lastError += ex.getMessage(); 
			}
			
		    result = false;
		}
		
		return result;
	}
	
	public void disconnect() {
		
			if (dbConnection != null) {
			
				try {
					
					if (!dbConnection.isClosed()) {
						
						dbConnection.close();
					}
				}
				catch (SQLException ex) {
					
					lastError = ex.getMessage();
				}
				
				dbConnection = null;
		}		
	}
	
	public int executeNonQuery(String query) { //executes query and returns the number of affected things
		
		Statement sqlStatement;
		int affectedCount = 0;
		
		if (!isConnected()) {
			return affectedCount;
		}
		
		try {
			
			sqlStatement = dbConnection.createStatement();
			sqlStatement.execute(query);
			affectedCount = sqlStatement.getUpdateCount();
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();			
		}
		
		return affectedCount;
	}
	
	public ResultSet executeQuery(String query) { //executes query and returns ResultSet 
		
		Statement sqlStatement;
		ResultSet result;
		
		if (!isConnected()) {
			return null;
		}
		
		try {
			
			sqlStatement = dbConnection.createStatement();
			result = sqlStatement.executeQuery(query);
			try {
				
				result.first();
			}
			catch (Exception ex) {
				
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();
			return null;
		}
		
		return result;		
	}
	
	public String getLastError() {
		
		return lastError;
	}
	
	public void finallize() {
		
		disconnect();
		mySqlServerAddress = null;	
		mySqlDatabaseName = null;
		mySqlUsername = null;
		mySqlPassword = null;
		lastError = null;
	}
}
