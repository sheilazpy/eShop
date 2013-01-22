/*
 * Easy JTable population and management with data from database.
 * (C) 14.01.2013 - 19.01.2013 zhgzhg
 */

package jtables_database_management;

import database_management.MySQLdbManager;
import java.sql.ResultSet;

import javax.swing.table.AbstractTableModel;

public class Tables_db_manager {
	
	private AbstractTableModel currentModel;
	private int rowsCount, columnsCount;
	private String populateQuery = null, insertQuery = null, updateQuery = null, deleteQuery = null;
	private Object[] populateQueryParams = null, insertQueryParams = null, updateQueryParams = null, deleteQueryParams = null;
	
	private boolean noDataInTheCells = true;
	private String[][] noDataInTheCellsMessage = null;
	
	private MySQLdbManager dbPortal;
	private String errorMessage = null;
	
	private String[][] CELLS;
	
	public Tables_db_manager(AbstractTableModel tableModelPointer, int rowsCount, int columnsCount, 
			MySQLdbManager openedDatabasePointer, String[][] CELLS) {
		
		this.currentModel = tableModelPointer;
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
		this.dbPortal = openedDatabasePointer;
		this.CELLS = CELLS;
	}
	
	public void setPopulateQuery(String query) {
		
		populateQuery = query;
		populateQueryParams = null;
	}
	
	public void setPopulateQuery(String query, Object... params) {
		
		populateQuery = query;
		populateQueryParams = new Object[params.length];
		
		for (int i = 0; i < params.length; i++) {
			populateQueryParams[i] = params[i];
		}
	}
	
	public void setInsertQuery(String query) {
		
		insertQuery = query;
		insertQueryParams = null;
	}
	
	public void setInsertQuery(String query, Object... params) {
		
		insertQuery = query;
		insertQueryParams = new Object[params.length];
		
		for (int i = 0; i < params.length; i++) {
			insertQueryParams[i] = params[i];
		}
	}
	
	public void setUpdateQuery(String query) {
		
		updateQuery = query;
		updateQueryParams = null;
	}
	
	public void setUpdateQuery(String query, Object... params) {
		
		updateQuery = query;
		updateQueryParams = new Object[params.length];
		
		for (int i = 0; i < params.length; i++) {
			updateQueryParams[i] = params[i];
		}
	}
	
	public void setDeleteQuery(String query) {
		
		deleteQuery = query;
		deleteQueryParams = null;
	}
	
	public void setDeleteQuery(String query, Object... params) {
		
		deleteQuery = query;
		deleteQueryParams = new Object[params.length];
		
		for (int i = 0; i < params.length; i++) {
			deleteQueryParams[i] = params[i];
		}
	}

	public void setNoDataInTheCellsMessage(String[] wordsToPutInTheCells) {
		
		noDataInTheCellsMessage = new String[1][wordsToPutInTheCells.length];
		
		for (int i = 0; i < wordsToPutInTheCells.length; i++) {
			noDataInTheCellsMessage[0][i] = wordsToPutInTheCells[i];
		}
	}
	
	public boolean areCellsEmpty() {
		return noDataInTheCells;
	}
	
	public String getLastError() {
		return errorMessage;
	}
	
	/**
	 * Requires setPopulateQuery() to be set!
	 */
	
	public String[][] performPopulate() {
		
		ResultSet rs = null;
				
		if (populateQueryParams == null) { 
			
			dbPortal.freeQueryNonQueryTemporaryResults();
			rs = dbPortal.executeQuery(populateQuery); 
		}
		else {
			
			dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
			rs = dbPortal.executeParameterizedQuery(populateQuery, populateQueryParams);
		}
		
		if (rs != null) {
			
			try {
			
				rs.last();
				rowsCount = rs.getRow();
				if (rowsCount <= 0) {					
					return noDataInTheCellsMessage;
				}
				rs.first();
				
				CELLS = new String[rowsCount][columnsCount];
				
				while (true) {
					
					Object o = null;
					
					for (int j = 0; j < columnsCount; j++) {						
					
						o = rs.getObject(j + 1); 
						CELLS[rs.getRow() - 1][j] = (o == null ? "" : o.toString());
					}
					
					if (rs.isLast()) {
						break;
					}
					
					rs.next();
				}
				
				noDataInTheCells = false;					
			}
			catch (Exception ex) {
				
			}				
		}
		
		return CELLS;
	}
	
	/**
	 * Requires setInsertQuery() and SPECIAL setPopulateQuery() to be set!
	 */
	
	public String[][] performRowInsert() {
		
		errorMessage = null;
		
		ResultSet rs = null;
		
		if (insertQueryParams == null) {
		
			dbPortal.freeQueryNonQueryTemporaryResults();
			
			if (dbPortal.executeNonQuery(insertQuery) < 1) {
				
				errorMessage = "������ ��� ����������:\n" + dbPortal.getLastError();				
				return CELLS;
			}		
		}
		else {
			
			dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
			
			if (dbPortal.executeParameterizedNonQuery(insertQuery, insertQueryParams) < 1) {
				
				errorMessage = "������ ��� ����������:\n" + dbPortal.getLastError();				
				return CELLS;
			}			
		}
		
		// populate the table with the new row
		
		int lastProductId = -1;
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT LAST_INSERT_ID()");
		
		if (rs != null) {
			
			try {
				lastProductId = rs.getInt(1);
			}
			catch (Exception ex) {
				lastProductId = -1;
			}
		}
		
		if (lastProductId == -1) {
			
			errorMessage = "������ ��� ������������� �� ���������!";
			return CELLS;
		}
		
		String[][] newCells = null;
		
		if (noDataInTheCells == false) {
		
			newCells = new String[rowsCount + 1][columnsCount];
			for (int i = 0; i < rowsCount; i++) {
				for (int j = 0; j < columnsCount; j++) {
					newCells[i][j] = CELLS[i][j];
				}
			}			
		}
		else {				
			newCells = new String[1][columnsCount];
			rowsCount++;
		}
		
		rs = null;
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery(populateQuery + lastProductId); //special query
		
		if (rs != null) {
			
			try {
				
				for (int k = 0; k < columnsCount; k++) {
					newCells[(noDataInTheCells == false ? rowsCount : 0)][k] = rs.getObject(k + 1).toString();
				}				
			}
			catch (Exception ex) {
				
				errorMessage = "������ ��� ������������� �� ���������!\n�������� � ���������� �� ����� �� �� � ���� �������.";
			}
			
			CELLS = newCells;
						
			for (int l = 0; l < columnsCount; l++) {
				currentModel.fireTableCellUpdated((noDataInTheCells == false ? rowsCount : 0), l); //visual optimized refresh
			}
			
			if (noDataInTheCells == false) {			
			
				rowsCount++;
			}
			
			noDataInTheCells = false;
		}
		else {
			
			errorMessage = "������ ��� ������������� �� ���������!\n�������� � ���������� �� ��� ����� �� �� � ���� �������.";			
		}
		
		return CELLS;
	}
	
	/**
	 * Requires setDeleteQuery() to be set!
	 */
	
	public String[][] performRowDelete(int tableRowNumber) {
		
		errorMessage = null;
		
		if (noDataInTheCells == true) {
			return noDataInTheCellsMessage;
		}
		
		if (deleteQueryParams == null) {
		
			dbPortal.freeQueryNonQueryTemporaryResults();
			if (dbPortal.executeNonQuery(deleteQuery) < 1) {
				
				errorMessage = "������ ��� �����������:\n" + dbPortal.getLastError();				
				return CELLS;
			}
		}
		else {
			
			dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
			if (dbPortal.executeParameterizedNonQuery(deleteQuery, deleteQueryParams) < 1) {
				
				errorMessage = "������ ��� �����������:\n" + dbPortal.getLastError();				
				return CELLS;
			}			
		}
		
		// remove the deleted row from the table
		
		String[][] newCells = null;
		
		if (rowsCount > 1) {
		
			newCells = new String[rowsCount - 1][columnsCount];
			
			for (int i = 0, j = 0; i < rowsCount; i++) {
				
				for (int k = 0; k < columnsCount; k++) {
				
					if (i != tableRowNumber) {
					
						newCells[j][k] = CELLS[i][k];						
					}
					else {
						break;
					}
				}
				
				if (i != tableRowNumber) {
					j++;
				}
			}
			
			rowsCount--;
		}
		else {
			
			newCells = noDataInTheCellsMessage;
			noDataInTheCells = true;
		}
		
		CELLS = newCells;
		
		//no optimized visual refresh here
		
		return CELLS;
	}
	
	/**
	 * Requires setUpdateQuery() and setPopulateQuery() to be set!
	 */
	
	public String[][] performRowUpdate(int tableRowNumber) {
		
		errorMessage = null;
		
		if (noDataInTheCells == true) {
			return noDataInTheCellsMessage;
		}
		
		if (updateQueryParams == null) {		
			
			dbPortal.freeQueryNonQueryTemporaryResults();
			if (dbPortal.executeNonQuery(updateQuery) < 1) {
				
				errorMessage = "������ ��� �������������:\n" + dbPortal.getLastError();							
				return CELLS;
			}
		}
		else {
			
			dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
			if (dbPortal.executeParameterizedNonQuery(updateQuery, updateQueryParams) < 1) {
				
				errorMessage = "������ ��� �������������:\n" + dbPortal.getLastError();							
				return CELLS;
			}
		}
		
		ResultSet rs = null;
		
		if (populateQueryParams == null) {
		
			dbPortal.freeQueryNonQueryTemporaryResults();
			rs = dbPortal.executeQuery(populateQuery);
		}
		else {
			
			dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
			rs = dbPortal.executeParameterizedQuery(populateQuery, populateQueryParams);
		}
			
		if (rs != null) {
			
			try {
				
				Object o = null;
				 
				for (int i = 0; i < columnsCount; i++) {
					
					o = rs.getObject(i + 1);
					CELLS[tableRowNumber][i] = (o == null ? "" : o.toString());
				}
				
				//CELLS[rowNumber][0] = rs.getString(1); 	   			  // product_name
				//CELLS[rowNumber][1] = new String("" + rs.getInt(2));  // product_quantity
				//CELLS[rowNumber][2] = rs.getBigDecimal(3).toString(); // product_price
			}
			catch (Exception ex) {
				errorMessage = ex.getMessage();
				return CELLS;
			}
		}
		else {
			errorMessage = dbPortal.getLastError();
		}
		
		for (int j = 0; j < columnsCount; j++) { //visual optimized refresh
			
			currentModel.fireTableCellUpdated(tableRowNumber, j); 
		}
		
		return CELLS;
	}
	
	public void finalize() {
		
		currentModel = null;
		populateQuery = null;
		insertQuery = null;
		updateQuery = null; 
		deleteQuery = null;
		
		populateQueryParams = null;
		insertQueryParams = null;
		updateQueryParams = null;
		deleteQueryParams = null;
		
		noDataInTheCellsMessage = null;
		
		dbPortal = null;
		errorMessage = null;
		CELLS = null;
	}
}
