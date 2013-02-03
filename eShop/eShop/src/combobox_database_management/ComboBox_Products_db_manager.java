/*
 * Easy ComboBox population with products/operators data from the database and data accuracy control.
 * (C) 31.01.2013 - 03.02.2013 zhgzhg
 */

package combobox_database_management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database_management.MySQLdbManager;
import database_management.SqlResultSetAndIntegerMixer;

public class ComboBox_Products_db_manager {	
	
	Integer[] productsIds = null;
	String[] productsNames = null;
	Integer[] productsQuantities = null;
	
	MySQLdbManager dbPortal = null;	
	
	public ComboBox_Products_db_manager(MySQLdbManager dbPortalPointer) {
		
		dbPortal = dbPortalPointer;
		productsIds = null;
		productsNames = null;
		productsQuantities = null;
	}
	
	public void loadAllProducts() {
		
		if (dbPortal == null) {			
			return;
		}
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		ResultSet rs = dbPortal.executeQuery("SELECT product_id, product_name, product_quantity FROM products");
		
		if (rs == null) {
			
			productsIds = null;
			productsNames = null;
			productsQuantities = null;
			return;
		}
		
		int productsQuantity = 0;
		
		try {
			rs.last();		
			productsQuantity = rs.getRow();
			rs.first();
		}
		catch (SQLException ex) {
			
			productsIds = null;
			productsNames = null;
			productsQuantities = null;
			
			JOptionPane.showMessageDialog(null, "Грешка: " + ex.getMessage(), "Грешка", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (productsQuantity == 0) {
			
			productsIds = null;
			productsNames = null;
			productsQuantities = null;
			return;
		}
		
		//fill the arrays with data
		
		productsIds = new Integer[productsQuantity];
		productsNames = new String[productsQuantity];
		productsQuantities = new Integer[productsQuantity];
		
		int i = 0;
		
		while (true) {
			
			try {
				productsIds[i] = new Integer(rs.getInt(1));
				productsNames[i] = new String(rs.getString(2));
				productsQuantities[i] = new Integer(rs.getInt(3));
				
				if (rs.isLast() == true) {
					break;
				}
				
				i++;
				
				rs.next();
			}
			catch (SQLException ex) {
				
				productsIds = null;
				productsNames = null;
				productsQuantities = null;
				JOptionPane.showMessageDialog(null, "Грешка: " + ex.getMessage(), "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	public String[] getAllLoadedProductsNames() {
		
		if (dbPortal == null) {
			return null;
		}
		
		return productsNames;
	}
	
	public int getProductStringArrayIdByProductDbId(int productDbId) {
		
		int fail = -1;
		
		if ((dbPortal == null) || (productsIds == null) || (productDbId < 0) || (productsIds.length <= 0)) {
			return fail;
		}
		
		for (int i = 0; i < productsIds.length; i++) {
			
			if (productsIds[i].intValue() == productDbId) {
				
				return i;
			}
		}
		
		return fail;
	}
	
	public Integer getProductIdByProductStringArrayId(int inStringArrayId) {		

		if ((dbPortal == null) || (productsIds == null) || (productsIds.length < (inStringArrayId + 1)) || 
				(inStringArrayId < 0)) {
			return null;
		}
		
		return productsIds[inStringArrayId];
	}
	
	public Integer getProductQuantityByProductStringArrayId(int inStringArrayId) {
		
		if ((dbPortal == null) || (productsQuantities == null) || (productsQuantities.length < (inStringArrayId + 1)) ||
				(inStringArrayId < 0)) {
			return null;
		}

		return productsQuantities[inStringArrayId];
	}
	
	public boolean decreaseProductQuantityFromProductStringArrayId(int inStringArrayId, int byHowMuch) {
		
		boolean result = false;
		
		if ((byHowMuch < 0) || (productsQuantities.length < inStringArrayId + 1) || (inStringArrayId < 0)) {
			return result;
		}
		
		if (byHowMuch > productsQuantities[inStringArrayId]) {
			
			JOptionPane.showMessageDialog(null, "Надвишен лимит!", "Грешка", JOptionPane.ERROR_MESSAGE);
			return result;
		}
		
		dbPortal.freeQueryNonQueryTransactionTemporaryResults();
		
		ArrayList<SqlResultSetAndIntegerMixer> rs =
		dbPortal.executeMixedTransaction("UPDATE products SET product_quantity=product_quantity-" + byHowMuch + 
				" WHERE product_id=" + productsIds[inStringArrayId].intValue(), "SELECT product_quantity FROM products WHERE product_id=" +
				productsIds[inStringArrayId].intValue());
		
		if (((Integer)rs.get(0).getContent()).intValue() < 1) {
			
			JOptionPane.showMessageDialog(null, "Грешка при обновяване на наличното количество на продукт в базата данни:\n" + 
					dbPortal.getLastError(), "Грешка при редактиране на налично продуктово количество", JOptionPane.ERROR_MESSAGE);
			
			result = false;
		}
		else {			
		
			if (((ResultSet)rs.get(1).getContent()) == null) {
				
				JOptionPane.showMessageDialog(null, "Грешка при обновяване на наличното количество на продукт в базата данни:\n" + 
						dbPortal.getLastError(), "Грешка при редактиране на налично продуктово количество", JOptionPane.ERROR_MESSAGE);
				result = false;
			}
			else {
				
				try {
					
					int restQuantity = 0;
					
					if ((restQuantity = ((ResultSet)rs.get(1).getContent()).getInt(1)) < 0) { // someone has exhausted the quantity
						
						result = false;
						
						JOptionPane.showMessageDialog(null, "Зададеното от вас количество е било намалено от някой друг.\nПробвайте пак!",
								"Грешка при обновяване на продукта", JOptionPane.ERROR_MESSAGE);
						
						if (dbPortal.executeNonQuery("UPDATE products SET product_quantity=product_quantity+" + byHowMuch + 
								" WHERE product_id=" + productsIds[inStringArrayId].intValue()) != 1) {							
							
							JOptionPane.showMessageDialog(null, "Възникна проблем при обновяването на наличното количество на продукта!\n" + 
									"Възможно възникване на неконсистентни данни! Моля, проверете проблема!", "Грешка при промяна на данните за продукт", 
									JOptionPane.ERROR_MESSAGE);
						}
						else {
							productsQuantities[inStringArrayId] = new Integer(restQuantity);
						}
					}
					else {
					
						//update the quantity in the list
						result = true;
						
						productsQuantities[inStringArrayId] = new Integer(productsQuantities[inStringArrayId].intValue() - byHowMuch);
					}
				}
				catch (SQLException ex) {
					
					result = false;
					
					JOptionPane.showMessageDialog(null, "Възникна проблем при обновяването на наличното количество на продукта!\n" + 
							"Възможно възникване на неконсистентни данни! Моля, проверете проблема!", "Грешка при промяна на данните за продукт", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		return result;
	}	
	
	public boolean increaseProductQuantityFromProductStringArrayId(int inStringArrayId, int byHowMuch) {
		
		boolean result = false;
		
		if ((byHowMuch < 0) || (productsQuantities.length < inStringArrayId + 1) || (inStringArrayId < 0)) {
			return result;
		}
		
		dbPortal.freeQueryNonQueryTransactionTemporaryResults();
		
		ArrayList<SqlResultSetAndIntegerMixer> rs =
		dbPortal.executeMixedTransaction("UPDATE products SET product_quantity=product_quantity+" + byHowMuch + 
				" WHERE product_id=" + productsIds[inStringArrayId].intValue(), "SELECT product_quantity FROM products WHERE product_id=" +
				productsIds[inStringArrayId].intValue());
		
		if (((Integer)rs.get(0).getContent()).intValue() < 1) {
			
			JOptionPane.showMessageDialog(null, "Грешка при обновяване на наличното количество на продукт в базата данни:\n" + 
					dbPortal.getLastError(), "Грешка при редактиране на налично продуктово количество", JOptionPane.ERROR_MESSAGE);
			
			result = false;
		}
		else {			
		
			if (((ResultSet)rs.get(1).getContent()) == null) {
				
				JOptionPane.showMessageDialog(null, "Грешка при обновяване на наличното количество на продукт в базата данни:\n" + 
						dbPortal.getLastError(), "Грешка при редактиране на налично продуктово количество", JOptionPane.ERROR_MESSAGE);
				result = false;
			}
			else {
				
				try {
					if (((ResultSet)rs.get(1).getContent()).getInt(1) < 0) { // someone has exhausted the quantity more than allowed
						
						result = false;
						
						JOptionPane.showMessageDialog(null, "Зададеното от вас количество е било намалено от някой друг.\nПробвайте пак!",
								"Грешка при обновяване на продукта", JOptionPane.ERROR_MESSAGE);
						
						if (dbPortal.executeNonQuery("UPDATE products SET product_quantity=0 WHERE product_id=" + 
								productsIds[inStringArrayId].intValue()) != 1) {							
							
							JOptionPane.showMessageDialog(null, "Възникна проблем при обновяването на наличното количество на продукта!\n" + 
									"Възможно възникване на неконсистентни данни! Моля, проверете проблема!", "Грешка при промяна на данните за продукт", 
									JOptionPane.ERROR_MESSAGE);
						}
						else {
							productsQuantities[inStringArrayId] = new Integer(0);
						}
					}
					else {
					
						//update the quantity in the list
						result = true;
						
						productsQuantities[inStringArrayId] = new Integer(productsQuantities[inStringArrayId].intValue() + byHowMuch);
					}
				}
				catch (SQLException ex) {
					
					result = false;
					
					JOptionPane.showMessageDialog(null, "Възникна проблем при обновяването на наличното количество на продукта!\n" + 
							"Възможно възникване на неконсистентни данни! Моля, проверете проблема!", "Грешка при промяна на данните за продукт", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		return result;
	}
	
	public void finalize() {
		
		dbPortal = null;
		productsIds = null;
		productsNames = null;
		productsQuantities = null;		
	}
}
