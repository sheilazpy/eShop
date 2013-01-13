/**
 * <p> Title: MySQLdbManager </p>
 * <p> Description: MySQL java database manager wrapper </p>
 * @version 1.00
 * @author (C) 09.01.2013 - 13.01.2013 zhgzhg
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
	private boolean useUTF8Encoding = true;
	private Connection dbConnection = null;
		
	private String lastError = null;
	
	/**
	 * Empty Constructor 
	 */
	
	public MySQLdbManager() {
		
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server ip address or hostname
	 */
	
	public MySQLdbManager(String mySqlServerAddress) {
		this.mySqlServerAddress = mySqlServerAddress;
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 * @param mySqlServerPort int MySQL server running port between 1 and 65535. The default one is 3306.
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 * @param mySqlServerPort int MySQL server running port between 1 and 65535. The default one is 3306.
	 * @param mySqlUsername String MySQL database username
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort, String mySqlUsername) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
		this.mySqlUsername = mySqlUsername;
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 * @param mySqlServerPort int MySQL server running port between 1 and 65535. The default one is 3306.
	 * @param mySqlUsername String MySQL database username
	 * @param mySqlPassword String MySQL database username password. The default one is empty string.
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName, int mySqlServerPort, String mySqlUsername, String mySqlPassword) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.setMySqlServerPort(mySqlServerPort);
		this.mySqlUsername = mySqlUsername;
		this.mySqlPassword = mySqlPassword;
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 * @param mySqlUsername String MySQL database username
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName,  String mySqlUsername) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.mySqlUsername = mySqlUsername;		
	}
	
	/**
	 * Constructor
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
	 * @param mySqlDatabaseName String MySQL database name (leave empty "" if you want to assign with no one)
	 * @param mySqlUsername String MySQL database username
	 * @param mySqlPassword String MySQL database username password. The default one is empty string.
	 */
	
	public MySQLdbManager(String mySqlServerAddress, String mySqlDatabaseName,  String mySqlUsername, String mySqlPassword) {
		this.mySqlServerAddress = mySqlServerAddress;
		this.mySqlDatabaseName = mySqlDatabaseName;
		this.mySqlUsername = mySqlUsername;
		this.mySqlPassword = mySqlPassword;
	}
	
	/**
	 * Sets MySQL server address. After done you need to call disconnect() and connect() again.
	 * @param mySqlServerAddress String MySQL server IP address ot HOSTNAME
	 */
	
	public void setMySqlServerAddress(String mySqlServerAddress) {
		this.mySqlServerAddress = mySqlServerAddress;
	}
	
	/**
	 * Returns MySQL server address.
	 * @return String
	 */
	
	public String getMySqlServerAddress() {
		return mySqlServerAddress;
	}
	
	/**
	 * Sets MySQL database [name] to use. Leave empty "" if you want to assign with no one.
	 * You need to call disconnect() and connect() after that.
	 * @param mySqlDatabaseName String
	 */
	
	public void setMySqlActiveDatabase(String mySqlDatabaseName) {
		this.mySqlDatabaseName = mySqlDatabaseName;
	}
	
	/**
	 * Returns current active MySQL database name.
	 * @return String
	 */
	
	public String getMySqlActiveDatabase() {
		return mySqlDatabaseName;
	}
	
	/**
	 * Sets MySQL server port. Must be between [1 and 65535].
	 * You need to call disconnect() and connect() after that.
	 * @param mySqlServerPort int
	 */
	
	public void setMySqlServerPort(int mySqlServerPort) {
		
		if (mySqlServerPort <= 0 || mySqlServerPort > 65535)
		{ lastError = "Invalid mysql server port range! Must be between [1 and 65535]."; return; }
		
		this.mySqlServerPort = mySqlServerPort;
	}
	
	/**
	 * Returns MySQL server port.
	 * @return int ([between 1 and 65535])
	 */
	
	public int getMySqlServerPort() {
		
		return this.mySqlServerPort;
	}
	
	/**
	 * Sets MySQL database username.
	 * You need to call disconnect() and connect() after that.
	 * @param mySqlUsername String
	 */
	
	public void setMySqlUsername(String mySqlUsername) {
		
		this.mySqlUsername = mySqlUsername;
	}
	
	/**
	 * Returns MySQL database username.
	 * @return String
	 */
	
	public String getMySqlUsername() {
		
		return mySqlUsername;
	}
	
	/**
	 * Sets MySQL database username password.
	 * You need to call disconnect() and connect() after that.
	 * @param mySqlPassword String
	 */
	
	public void setMySqlPassword(String mySqlPassword) {
		
		this.mySqlPassword = mySqlPassword;
	}
	
	/**
	 * Returns current MySQL database username password.
	 * @return String
	 */
	
	public String getMySqlPassword() {
		
		return this.mySqlPassword;
	}
	
	/**
	 * Sets database input/output UTF8 data encoding on or off.
	 * You need to call disconnect() and connect() after that.
	 * @param on boolean
	 */
	
	public void setUtf8EncodingOn(boolean on) {
		
		this.useUTF8Encoding = on;
	}
	
	/**
	 * Returns database input/output UTF8 is on or off.
	 * @return boolean
	 */
	
	public boolean isUtf8EncodingUsed() {
		
		return this.useUTF8Encoding;
	}
	
	/**
	 * Checks for active database connection and returns true if it is presented.
	 * @return boolean
	 */
	
	public boolean isConnected() {
		
		boolean result = true;
		
		lastError = null;
		
		if (dbConnection != null) {
			
			try {
				
				result = (!dbConnection.isClosed());
			}			
			catch (SQLException ex) {
				
				lastError = ex.getMessage(); 
				result = false;
			}
		}
		else {			
			lastError = "Uninitialized database connection!";
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Tries to connect to MySQL database and returns true in case of success.
	 * @return boolean
	 */
	
	public boolean connect() {
		
		boolean result = true;
		
		if (mySqlServerAddress == null || mySqlDatabaseName == null || mySqlUsername == null) { 
			lastError = "Uninitialized server address, database name or database username!"; return false; }
		else {
			lastError = null; //new connect always clears the last error
		}			
		
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
					"?user=" + mySqlUsername + (mySqlPassword != null ? "&password=" + mySqlPassword : "") + 
					(useUTF8Encoding == true ? "&useUnicode=TRUE&characterEncoding=UTF-8" : ""));
		}
		catch (Exception ex) {
			
			if (lastError == null) { 
				lastError = ex.getMessage(); 
			}
			else { 
				lastError += ex.getMessage(); 
			}
			
		    result = false;
		}
		
		return result;
	}
	
	/**
	 * Close the active database connection if presented.
	 */
	
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
	
	/**
	 * Executes query and returns the number of affected things. For INSERT, UPDATE, DELETE ...
	 * Warning! Prone to SQL injection!!!
	 * @param query String
	 * @return int the number of affected things
	 */
	
	public int executeNonQuery(String query) {
		
		Statement sqlStatement;
		int affectedCount = 0;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return affectedCount;			
		}
		else {			
			lastError = null;
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
	
	/**
	 * Executes query and returns ResultSet with data. For SELECT...
	 * Warning! Prone to SQL injection!!!
	 * @param query String
	 * @return ResultSet
	 */
	
	public ResultSet executeQuery(String query) { //executes query and returns ResultSet 
		
		Statement sqlStatement;
		ResultSet result;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
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
	
	/**
	 * Returns the text message exception error of the last called method. Returns null if no error.
	 * @return String
	 */
	
	public String getLastError() {
		
		return lastError;
	}
	
	/**
	 * Finalization method if you don't want to use specific MySQLdbManager instance object anymore.
	 */
	
	public void finalize() {
		
		disconnect();
		mySqlServerAddress = null;	
		mySqlDatabaseName = null;
		mySqlUsername = null;
		mySqlPassword = null;
		lastError = null;
	}
}
