/*
 * Easy ComboBox population with products/operators data from the database and data accuracy control.
 * (C) 19.01.2013 - 21.01.2013 zhgzhg
 */

package combobox_database_management;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import database_management.MySQLdbManager;

public class ComboBox_Operators_db_manager {	
	
	Integer[] operatorsIds = null;
	String[] operatorsNames = null;
		
	MySQLdbManager dbPortal = null;	
	
	public ComboBox_Operators_db_manager(MySQLdbManager dbPortalPointer) {
		
		dbPortal = dbPortalPointer;
		operatorsIds = null;
		operatorsNames = null;		
	}
	
	public void loadAllOperators() {
		
		if (dbPortal == null) {			
			return;
		}
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		ResultSet rs = dbPortal.executeQuery("SELECT operator_id, CONCAT_WS(SPACE(1), operator_first_name, operator_last_name)" + 
				" FROM operators");
		
		if (rs == null) {
			
			operatorsIds = null;
			operatorsNames = null;
			return;
		}
		
		int operatorsQuantity = 0;
		
		try {
			rs.last();		
			operatorsQuantity = rs.getRow();
			rs.first();
		}
		catch (SQLException ex) {
			
			operatorsIds = null;
			operatorsNames = null;
						
			JOptionPane.showMessageDialog(null, "������: " + ex.getMessage(), "������", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (operatorsQuantity == 0) {
			
			operatorsIds = null;
			operatorsNames = null;
			return;
		}
		
		//fill the arrays with data
		
		operatorsIds = new Integer[operatorsQuantity];
		operatorsNames = new String[operatorsQuantity];
		
		int i = 0;
		
		while (true) {
			
			try {
				operatorsIds[i] = new Integer(rs.getInt(1));
				operatorsNames[i] = new String(rs.getString(2));
								
				if (rs.isLast() == true) {
					break;
				}
				
				i++;
				
				rs.next();
			}
			catch (SQLException ex) {
				
				operatorsIds = null;
				operatorsNames = null;
				JOptionPane.showMessageDialog(null, "������: " + ex.getMessage(), "������", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	public String[] getAllLoadedOperatorsNames() {
		
		if (dbPortal == null) {
			return null;
		}
		
		return operatorsNames;
	}
	
	public int getOperatorStringArrayIdByOperatorDbId(int operatorDbId) {
		
		int fail = -1;
		
		if ((dbPortal == null) || (operatorsIds == null) || (operatorDbId < 0) || (operatorsIds.length <= 0)) {
			return fail;
		}
		
		for (int i = 0; i < operatorsIds.length; i++) {
			
			if (operatorsIds[i].intValue() == operatorDbId) {
				
				return i;
			}
		}
		
		return fail;
	}
	
	public Integer getOperatorIdByOperatorStringArrayId(int inStringArrayId) {		

		if ((dbPortal == null) || (operatorsIds == null) || (operatorsIds.length < (inStringArrayId + 1)) || 
				(inStringArrayId < 0)) {
			return null;
		}
		
		return operatorsIds[inStringArrayId];
	}
	
	public void finalize() {
		
		dbPortal = null;
		operatorsIds = null;
		operatorsNames = null;				
	}

}
