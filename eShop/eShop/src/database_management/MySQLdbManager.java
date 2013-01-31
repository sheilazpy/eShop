/**
 * <p> Title: MySQLdbManager </p>
 * <p> Description: MySQL java database manager wrapper </p>
 * <p> For multi-threaded applications different instances should be used </p>
 * @version 1.01
 * @author (C) 30.01.2013 - 31.01.2013 zhgzhg
 */

package database_management;

import java.sql.*;
import java.util.ArrayList;

public class MySQLdbManager {
	
	public final static int TRANSACTION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED, 
	TRANSACTION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED,
	TRANSACTION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ,
	TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
	
	private final String JDBCDRIVER = "com.mysql.jdbc.Driver";
	private int mySqlServerPort = 3306;
	private String mySqlServerAddress = null;	
	private String mySqlDatabaseName = null;
	private String mySqlUsername = null;
	private String mySqlPassword = null;
	private boolean useUTF8Encoding = true;
	private Connection dbConnection = null;
	
	private PreparedStatement pSqlStatement = null;
	private Statement sqlStatement = null;
	
	private PreparedStatement[] tpSqlStatement = null;
	private Statement[] tsqlStatement = null;
		
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
	 * @param mySqlServerAddress String MySQL server IP address or HOSTNAME
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
	 * Attempts to change the transaction isolation level for the current instance. 
	 * @param level int Possible values: MySQLdbManager.TRANSACTION_READ_UNCOMMITTED, MySQLdbManager.TRANSACTION_READ_COMMITTED, 
	 * MySQLdbManager.TRANSACTION_REPEATABLE_READ or MySQLdbManager.TRANSACTION_SERIALIZABLE.
	 */
	
	public void setMySQLTransactionIsolationLevel(int level) {
		
		lastError = null;
		
		if ((level != MySQLdbManager.TRANSACTION_READ_COMMITTED) && (level != TRANSACTION_READ_UNCOMMITTED) && 
		(level != MySQLdbManager.TRANSACTION_REPEATABLE_READ) && (level != MySQLdbManager.TRANSACTION_SERIALIZABLE)) {
			
			lastError = "Invalid level value!";
			return;
		}
		
		if (dbConnection != null) {
			
			try {
				
				dbConnection.setTransactionIsolation(level);
			}
			catch (SQLException ex) {
				
				lastError = ex.getMessage();
			}
		}
		else {
			
			lastError = "Uninitialized connection!";
		} 
	}
	
	/**
	 * Returns the transaction isolation level for the current instance.
	 * @return int
	 */
	
	public int getMySQLTransactionIsolationLevel() {
		
		lastError = null;
		
		if (dbConnection != null) {
		
			try {
			
				return dbConnection.getTransactionIsolation();
			}
			catch (SQLException ex) {
				
				lastError = ex.getMessage();
			}
		}
		else {
			
			lastError = "Uninitialized connection!";
		}
		
		return -1;
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
					
					freeQueryNonQueryTemporaryResults();
					freeParameterizedQueryNonQueryTemporaryResults();
					
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
		
		int affectedCount = 0;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return affectedCount;			
		}
		else {			
			lastError = null;
		}
		
		try {
			
			dbConnection.setAutoCommit(true); // now we are not working with transactions
			
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
	 * Executes transaction queries and returns the number of affected things. For INSERT, UPDATE, DELETE ...
	 * Warning! Prone to SQL injection!!!
	 * @param query String... contains each query
	 * @return int[] the number of affected things for each query
	 */
	
	public int[] executeNonQueryTransaction(String... query) {
		
		int[] affectedCount = null;
		
		if (query == null) {
			
			lastError = "Uninitialized query!";
			return affectedCount;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return affectedCount;			
		}
		else {			
			lastError = null;
		}
		
		int i = 0;
		tsqlStatement = new Statement [query.length];
		
		try {
			
			dbConnection.setAutoCommit(false); // now we are not working with transactions
						 			
			for (i = 0; i < query.length; i++) {
			
				tsqlStatement[i] = dbConnection.createStatement();
				tsqlStatement[i].execute(query[i]);
			}
			
			dbConnection.commit();
			
			affectedCount = new int [query.length];
			
			for (i = 0; i < query.length; i++) {
				
				affectedCount[i] = 0;
			}
			
			for (i = 0; i < query.length; i++) {
			
				affectedCount[i] = tsqlStatement[i].getUpdateCount();
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();
			
			try {
				dbConnection.rollback();
			}
			catch (SQLException ex2) {
				
			}
		}		
		
		return affectedCount;
	}
	
	/**
	 * Executes parameterized query and returns the number of affected things. For INSERT, UPDATE, DELETE ...
	 * @param query String
	 * @param parameters Object (for class variable wrappers like String, Integer, etc. only from java.lang !!!!)
	 * @return int the number of affected things
	 */
	
	public int executeParameterizedNonQuery(String query, Object... parameters) {
		
		int affectedCount = 0;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return affectedCount;			
		}
		else {			
			lastError = null;
		}
		
		try {
			
			dbConnection.setAutoCommit(true); // now we are not working with transactions
			
			pSqlStatement = dbConnection.prepareStatement(query);
			
			for (int i = 0; i < parameters.length; i++) {
				
				pSqlStatement.setObject(i + 1, parameters[i]);
			}
			
			pSqlStatement.execute();
			affectedCount = pSqlStatement.getUpdateCount();
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();			
		}
		
		return affectedCount;
	}
	
	/**
	 * Executes parameterized query transaction and returns the number of affected things. For INSERT, UPDATE, DELETE ...
	 * @param queriesAndParameters SqlQueryAndParametersMixer
	 * @return int[] the number of affected things
	 */
	
	public int[] executeParameterizedNonQueryTransaction(SqlQueryAndParametersMixer... queriesAndParameters) {
		
		int[] affectedCount = null;
		
		if (queriesAndParameters == null) {
			
			lastError = "Uninitialized queriesAndParameters!";
			return affectedCount;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return affectedCount;			
		}
		else {			
			lastError = null;
		}
		
		int i = 0, j = 0;
		tpSqlStatement = new PreparedStatement[queriesAndParameters.length];
		
		try {
			
			dbConnection.setAutoCommit(false); // now we are working with transactions
			
			for (i = 0; i < queriesAndParameters.length; i++) {

				tpSqlStatement[i] = dbConnection.prepareStatement(queriesAndParameters[i].getQuery());
				
				if (queriesAndParameters[i].getParameters() != null) {
				
					for (j = 0; j < queriesAndParameters[i].getParameters().length; j++) {
						
						tpSqlStatement[i].setObject(j + 1, queriesAndParameters[i].getParameters()[j]);
					}
				}
				
				tpSqlStatement[i].execute();
			}
			
			dbConnection.commit();	
			
			affectedCount = new int[queriesAndParameters.length];
			
			for (i = 0; i < queriesAndParameters.length; i++) {
				
				affectedCount[i] = 0;
			}
			
			for (i = 0; i < queriesAndParameters.length; i++) {
			
				affectedCount[i] = tpSqlStatement[i].getUpdateCount();
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();	
			
			try {
				dbConnection.rollback();
			}
			catch (SQLException ex2) {
				
			}
		}
		
		return affectedCount;
	}
	
	/**
	 * Executes query and returns ResultSet with data. For SELECT...
	 * Warning! Prone to SQL injection!!!
	 * @param query String
	 * @return ResultSet
	 */
	
	public ResultSet executeQuery(String query) { 
		
		ResultSet result;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
		}
		
		try {
			
			dbConnection.setAutoCommit(true); // now we are not working with transactions
			
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
	 * Executes query transaction and returns ResultSet with data. For SELECT...
	 * Warning! Prone to SQL injection!!!
	 * 
	 * @param expectedQueriesWithoutResultSet int[] Index of the special query with null or without ResultSet.
	 * Used not to rise exception for query which is not SELECT (may fail because of that) or SELECT which returns nothing. Can be null.
	 * Attention!!! NO EXCEPTIONS AT ALL will be raised for the selected query.
	 * 
	 * @param query String...
	 * @return ResultSet[]
	 */
	
	public ResultSet[] executeQueryTransaction(int[] expectedQueriesWithoutResultSet, String... query) { 
		
		ResultSet[] result = null;
		
		if (query == null) {
			
			lastError = "Uninitialized query!";
			return result;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return result;
		}
		else {
			lastError = null;
		}
		
		boolean notInTheList = true;
		int i = 0, j = 0;		
		tsqlStatement = new Statement[query.length];
		result = new ResultSet[query.length];
		
		try {
			
			dbConnection.setAutoCommit(false); // now we are working with transactions
			
			for (i = 0; i < query.length; i++) {
				
				tsqlStatement[i] = dbConnection.createStatement();
				
				if (expectedQueriesWithoutResultSet == null) {
					
					result[i] = sqlStatement.executeQuery(query[i]);
				}
				else {
					
					//check if the query in in the expectedQueriesWithoutResultSet list
					
					for (j = 0, notInTheList = true; j < expectedQueriesWithoutResultSet.length; j++) {
						
						if (expectedQueriesWithoutResultSet[j] == i) {
							
							notInTheList = false;
							
							try {
								
								result[i] = sqlStatement.executeQuery(query[i]);
							}
							catch (Exception specialCase) {
								
							}
							
							break;
						}
					}
					
					if (notInTheList == true) {
						
						result[i] = sqlStatement.executeQuery(query[i]);
					}
				}
					
			}
			
			dbConnection.commit();
			
			for (i = 0; i < query.length; i++) {
				
				try {
					
					result[i].first();
				}
				catch (Exception ex) {
					
				}
			}
			
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();
			
			try {
			
				dbConnection.rollback();
			}
			catch (SQLException ex2) {
				
			}
			
			return null;
		}
		
		return result;		
	}
	
	/**
	 * Executes parameterized query and returns ResultSet with data. For SELECT...
	 * @param query String
	 * @param parameters Object (for class variable wrappers like String, Integer, etc. only from java.lang !!!!)
	 * @return ResultSet
	 */
	
	public ResultSet executeParameterizedQuery(String query, Object... parameters) { 
		
		ResultSet result;
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
		}
		
		try {
			
			dbConnection.setAutoCommit(true); // now we are not working with transactions
			
			pSqlStatement = dbConnection.prepareStatement(query);
			
			for (int i = 0; i < parameters.length; i++) {
				
				pSqlStatement.setObject(i + 1, parameters[i]);
			}
			
			result = pSqlStatement.executeQuery();
			
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
	 * Executes parameterized query transaction and returns ResultSet with data. For SELECT...
	 * 
	 * @param expectedQueriesWithoutResultSet int[] Index of the special query with null or without ResultSet.
	 * Used not to rise exception for query which is not SELECT or SELECT which returns nothing. Can be null.
	 * Attention!!! NO EXCEPTIONS AT ALL will be raised for the selected query.
	 * 
	 * @param queriesAndParameters SqlQueryAndParametersMixer
	 * 
	 * @return ResultSet[]
	 */
	
	public ResultSet[] executeParameterizedQueryTransaction(int[] expectedQueriesWithoutResultSet, SqlQueryAndParametersMixer... queriesAndParameters) {
		
		ResultSet[] result = null;
		
		if (queriesAndParameters == null) {
			
			lastError = "Uninitialized queriesAndParameters!";
			return null;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
		}
		
		int i = 0, j = 0;
		boolean notInTheList = true;
		result = new ResultSet[queriesAndParameters.length];
		tpSqlStatement = new PreparedStatement[queriesAndParameters.length];
		
		try {
			
			dbConnection.setAutoCommit(false); // now we are working with transactions
			
			for (i = 0; i < queriesAndParameters.length; i++) {

				tpSqlStatement[i] = dbConnection.prepareStatement(queriesAndParameters[i].getQuery());
				
				if (queriesAndParameters[i].getParameters() != null) {
					
					for (j = 0; j < queriesAndParameters[i].getParameters().length; j++) {
						
						tpSqlStatement[i].setObject(j + 1, queriesAndParameters[i].getParameters()[j]);
					}
				}
				
				if (expectedQueriesWithoutResultSet == null) {
					
					result[i] = tpSqlStatement[i].executeQuery();
				}
				else {
					
					//check if the query in in the expectedQueriesWithoutResultSet list
					
					for (j = 0, notInTheList = true; j < expectedQueriesWithoutResultSet.length; j++) {
						
						if (expectedQueriesWithoutResultSet[j] == i) {
							
							notInTheList = false;
							
							try {
								
								result[i] = tpSqlStatement[i].executeQuery();
							}
							catch (Exception specialCase) {
								
							}
							
							break;
						}
					}
					
					if (notInTheList == true) {
						
						result[i] = tpSqlStatement[i].executeQuery();
					}
				}
			}
			
			dbConnection.commit();
			
			for (i = 0; i < queriesAndParameters.length; i++) {
	
				try {
					
					result[i].first();
				}
				catch (Exception ex) {
					
				}
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();			
			try {
				dbConnection.rollback();
			}
			catch (Exception ex2) {
				
			}
			
			return null;
		}
		
		return result;		
	}
	
	/**
	 * Executes parameterized SQL transaction, returning mixed result of Integers and ResultSets. 
	 * @param queries String All the queries to execute in one transaction.
	 * @return ArrayList<SqlResultSetAndIntegerMixer> combination result of Integers (number of affected things) and ResultSets
	 * In case of total fail (wrong SQL syntax, etc.) it returns null.
	 */
	
	public ArrayList<SqlResultSetAndIntegerMixer> executeMixedTransaction(String... queries) {
		
		if (queries == null) {

			lastError = "Uninitialized queriesAndParameters!";
			return null;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
		}
		
		int i = 0;
		ArrayList<SqlResultSetAndIntegerMixer> result = new ArrayList<SqlResultSetAndIntegerMixer>(queries.length);		
		tsqlStatement = new Statement[queries.length];
				
		try {
			
			dbConnection.setAutoCommit(false); // now we are working with transactions
			
			for (i = 0; i < queries.length; i++) {
				
				tsqlStatement[i] = dbConnection.createStatement();
				
				if (tsqlStatement[i].execute(queries[i]) == true) {
				
					result.add(i, new SqlResultSetAndIntegerMixer(tsqlStatement[i].getResultSet()));
				} 
				else { // add the the number of affected things is get after the commit call
					
					result.add(i, new SqlResultSetAndIntegerMixer());
				}					
			}
			
			dbConnection.commit();
			
			for (i = 0; i < queries.length; i++) { // get affected things count where possible
				
				if (result.get(i).isInitialized == false) {
					
					result.get(i).Initialize(tsqlStatement[i].getUpdateCount());
				}
				else {
					
					try {
						
						if (result.get(i).isResultSetInside() == true) {
							
							((ResultSet)result.get(i).getContent()).first(); 
						}
					}
					catch (SQLException se) {
						
					}
				}
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();
			
			try {
				dbConnection.rollback();
			}
			catch (Exception ex2) {
				
			}
			
			return null;
		}
		
		return result;		
	}
	
	/**
	 * Executes parameterized SQL transaction, returning mixed result of Integers and ResultSets. 
	 * @param queriesAndParameters SqlQueryAndParametersMixer
	 * @return ArrayList<SqlResultSetAndIntegerMixer> combination result of Integers (number of affected things) and ResultSets
	 * In case of total fail (wrong SQL syntax, etc.) it returns null.
	 */
	
	public ArrayList<SqlResultSetAndIntegerMixer> executeMixedParameterizedTransaction(SqlQueryAndParametersMixer... queriesAndParameters) {
		
		if (queriesAndParameters == null) {

			lastError = "Uninitialized queriesAndParameters!";
			return null;
		}
		
		if (!isConnected()) {
			
			lastError = "No connection!";
			return null;
		}
		else {
			lastError = null;
		}
		
		int i = 0, j = 0;
		ArrayList<SqlResultSetAndIntegerMixer> result = new ArrayList<SqlResultSetAndIntegerMixer>(queriesAndParameters.length);		
		tpSqlStatement = new PreparedStatement[queriesAndParameters.length];
				
		try {
			
			dbConnection.setAutoCommit(false); // now we are working with transactions
			
			for (i = 0, j = 0; i < queriesAndParameters.length; i++) {
				
				tpSqlStatement[i] = dbConnection.prepareStatement(queriesAndParameters[i].getQuery());
				
				if (queriesAndParameters[i].getParameters() != null) {
				
					for (j = 0; j < queriesAndParameters[i].getParameters().length; j++) {
							
						tpSqlStatement[i].setObject(j + 1, queriesAndParameters[i].getParameters()[j]);
					}				
				}
				if (tpSqlStatement[i].execute() == true) {
				
					result.add(i, new SqlResultSetAndIntegerMixer(tpSqlStatement[i].getResultSet()));
				} 
				else { // add the the number of affected things is get after the commit call
					
					result.add(i, new SqlResultSetAndIntegerMixer());
				}
					
			}
			
			dbConnection.commit();
			
			for (i = 0; i < queriesAndParameters.length; i++) { // get affected things count where possible
				
				if (result.get(i).isInitialized == false) {
					
					result.get(i).Initialize(tpSqlStatement[i].getUpdateCount());
				}
				else {
					
					try {
						
						if (result.get(i).isResultSetInside() == true) {
							
							((ResultSet)result.get(i).getContent()).first(); 
						}
					}
					catch (SQLException se) {
						
					}
				}
			}
		}
		catch (SQLException ex) {
			
			lastError = ex.getMessage();
			
			try {
				dbConnection.rollback();
			}
			catch (Exception ex2) {
				
			}
			
			return null;
		}
		
		return result;		
	}
	
	/**
	 * Tries manually to roll back the last committed SQL transaction.
	 */
	
	public void tryRollbackLastTransaction() {
		
		if (dbConnection != null) {
		
			try {
				
				if (dbConnection.getAutoCommit() == false) {
					
					dbConnection.rollback();					
				}
			}
			catch (Exception ex) {
				
			}
		}
	}
	
	/**
	 * Frees memory resources allocated by executeQuery() and executeNonQuery().
	 * Recommended to use before the upper functions.
	 */
	
	public void freeQueryNonQueryTemporaryResults() {
		
		lastError = null;
		
		if (sqlStatement != null) {
			
			try {
				
				sqlStatement.close();
			}
			catch (Exception ex) {
				lastError = ex.getMessage();
			}
		}
	}
	
	/**
	 * Frees memory resources allocated by executeQueryTransaction() and executeNonQueryTransaction().
	 * Recommended to use before the upper functions.
	 */
	
	public void freeQueryNonQueryTransactionTemporaryResults() {
		
		lastError = null;
		
		if (tsqlStatement != null) {
			
			for (int i = 0; i < tsqlStatement.length; i++) {
			
				try {
					
					tsqlStatement[i].close();
				}
				catch (Exception ex) {
					lastError = ex.getMessage();
				}
			}
		}
	}
	
	/**
	 * Frees memory resources allocated by executeParameterizedQuery() and executeParameterizedNonQuery().
	 * Recommended to use before the upper functions.
	 */
	
	public void freeParameterizedQueryNonQueryTemporaryResults() {
		
		lastError = null;
		
		if (pSqlStatement != null) {
			
			try {
				
				pSqlStatement.close();
			}
			catch (Exception ex) {
				lastError = ex.getMessage();
			}
		}
	}
	
	/**
	 * Frees memory resources allocated by executeParameterizedQueryTransaction(), executeParameterizedNonQueryTransaction() and
	 * executeMixedParameterizedTransaction().
	 * Recommended to use before the upper functions.
	 */
	
	public void freeParameterizedQueryNonQueryTransactionTemporaryResults() {
		
		lastError = null;
		
		if (tpSqlStatement != null) {
			
			for (int i = 0; i < tpSqlStatement.length; i++) {
				
				try {
					
					tpSqlStatement[i].close();
				}
				catch (Exception ex) {
					lastError = ex.getMessage();
				}
			}
		}
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
