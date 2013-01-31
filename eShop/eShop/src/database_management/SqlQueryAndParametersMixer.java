/**
 * <p> Title: SqlQueryAndParametersMixer </p>
 * <p> Description: Special query and parameters class wrapper mixer for MySQLdbManager class
 * to pass arguments when needed to call parameterized non/query methods 
 * </p>
 * @version 1.00
 * @author (C) 30.01.2013 - 31.01.2013 zhgzhg
 */

package database_management;

public class SqlQueryAndParametersMixer {
	
	String query = null;
	Object[] parameters = null;
		
	/**
	 * Default constructor.
	 */
	
	public SqlQueryAndParametersMixer() {
		
	}
	
	/**
	 * Constructor which sets one query and it's parameters. Query or parameters can also be left null.
	 * @param query String
	 * @param parameters Object (for class variable wrappers like String, Integer, etc. only from java.lang !!!!)
	 */
	
	public SqlQueryAndParametersMixer(String query, Object... parameters) {
		
		if (query != null) {		
			
			setQuery(query);
		}
		
		if (parameters != null) {
			
			setParameters(parameters);
		}		
	}
		
	/**
	 * Sets the query string.
	 * @param query String
	 * @return void
	 */
	
	public void setQuery(String query) {
		
		this.query = query;		
	}
	
	/**
	 * Returns the current query string.
	 * @return String - the query string
	 */
	
	public String getQuery() {
		
		return query;
	}
	
	/**
	 * Sets the parameters for the query.
	 * @param parameters Object (for class variable wrappers like String, Integer, etc. only from java.lang !!!!)
	 * @return void
	 */
	
	public void setParameters(Object... parameters) {
		
		this.parameters = parameters;
	}
	
	/**
	 * Returns the current query parameters.
	 * @return Object[] - the query parameters
	 */
	
	public Object[] getParameters() {
		
		return parameters;
	}
	
	/**
	 * Finalization method. Call in case specific instance of this class would not be used anymore. 
	 */
	
	public void finalize() {
		
		query = null;
		parameters = null;
	}
}
