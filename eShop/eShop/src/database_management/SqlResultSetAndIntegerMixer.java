/**
 * <p> Title: SqlResultSetAndIntegerMixer </p>
 * <p> Description: Special Integer and ResultSet class wrapper mixer for MySQLdbManager class
 * to return result from executed mixed sql transaction MySQLdbManager methods. 
 * </p>
 * @version 1.00
 * @author (C) 30.01.2013 - 31.01.2013 zhgzhg
 */

package database_management;

import java.sql.ResultSet;

public class SqlResultSetAndIntegerMixer {

	boolean isInitialized = false;
	boolean isResultSet = false;
	ResultSet rs = null;
	Integer i = null;
	
	/**
	 * Default constructor. No value initialization done.
	 */
	
	public SqlResultSetAndIntegerMixer() {
		
	}
	
	/**
	 * Sets the contained value type of ResultSet with initialized value.
	 * After the value is initialized you can not set another value and or type.	
	 * @param rs ResultSet
	 */
	
	public SqlResultSetAndIntegerMixer(ResultSet rs) {		
		
		isResultSet = true;
		this.rs = rs;	
		
		isInitialized = true;
	}
	
	/**
	 * Sets the contained value type of Integer with initialized value.
	 * After the value is initialized you can not set another value and or type.
	 * @param i Integer
	 */
	
	public SqlResultSetAndIntegerMixer(Integer i) {
		
		isResultSet = false;
		this.i = i;
		
		isInitialized = true;
	}
	
	/**
	 * Sets the contained value type of ResultSet with initialized value.
	 * After the value is initialized you can not set another value and or type.
	 * This method intended for instance created with the default constructor.
	 * @param rs ResultSet
	 */
	
	public void Initialize(ResultSet rs) {
		
		if (isInitialized == true) {
			
			return;
		}
		
		isResultSet = true;
		this.rs = rs;	
		
		isInitialized = true;
	}
	/**
	 * Sets the contained value type of Integer with initialized value.
	 * After the value is initialized you can not set another value and or type.
	 * This method intended for instance created with the default constructor.
	 * @param i Integer
	 */
	
	public void Initialize(Integer i) {
		
		if (isInitialized == true) {
			
			return;
		}
		
		isResultSet = false;
		this.i = i;	
		
		isInitialized = true;
	}
	
	/**
	 * Returns the content of the current instance.
	 * @return Object
	 */
	
	public Object getContent() {
		
		return (isInitialized == true ? (isResultSet == true ? rs : i) : null);
	}
	
	/**
	 * Returns content type of the current instance. True if ResultSet or false if Integer type.
	 * @return boolean
	 */
	
	public boolean isResultSetInside() {
		
		return isResultSet;
	}
}
