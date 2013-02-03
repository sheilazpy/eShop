import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import database_management.MySQLdbManager;

public class databaseConnectWindow extends JDialog/*JFrame*/ {
	private static final long serialVersionUID = 3002L;

	private final JLabel label = new JLabel();
	private final JLabel label_1 = new JLabel();
	private final JLabel label_2 = new JLabel();
	private final JLabel label_3 = new JLabel();
	private final JLabel label_4 = new JLabel();
	private final JButton connectToDatabaseButton = new JButton();
	private final JTextField mysqlServerAddressTextField = new JTextField();
	private final JTextField mysqlPortTextField = new JTextField();
	private final JTextField mysqlDatabaseNameTextField = new JTextField();
	private final JTextField mysqlUsernameTextField = new JTextField();
	private final JPasswordField mysqlPasswordPasswordField = new JPasswordField();
	public static MySQLdbManager dbPortal = null;
	
	/**
	 * Dialog constructor
	 * @param owner Owner of the window
	 * @param modal Modal dialog
	 */
	public databaseConnectWindow(Frame owner, boolean modal) {
		super(owner, modal);
		setBounds(100, 100, 384, 203);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
	/**
	 * Launch the application - use if the class extends JFrame
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { 
			public void run() {
				try {
					databaseConnectWindow frame = new databaseConnectWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the frame - use if the class extends JFrame
	 */
	public databaseConnectWindow() {
		super();
		setBounds(100, 100, 384, 203);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//
	}
	private void jbInit() throws Exception {
		
		getRootPane().setDefaultButton(connectToDatabaseButton);
		
		getContentPane().setLayout(null);
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Свързване с базата данни");
		setName("databaseConnectWindowFrame");
		
		getContentPane().add(label);
		label.setText("Адрес на MySQL сървъра:");
		label.setBounds(10, 10, 165, 16);
		
		getContentPane().add(label_1);
		label_1.setText("Име на базата данни:");
		label_1.setBounds(10, 54, 165, 16);
		
		getContentPane().add(label_2);
		label_2.setText("Порт:");
		label_2.setBounds(10, 32, 156, 16);
		
		getContentPane().add(label_3);
		label_3.setText("Потребителско име:");
		label_3.setBounds(10, 76, 165, 16);
		
		getContentPane().add(label_4);
		label_4.setText("Парола:");
		label_4.setBounds(10, 98, 156, 16);
		
		getContentPane().add(connectToDatabaseButton);
		connectToDatabaseButton.addActionListener(new ConnectToDatabaseButtonActionListener());
		connectToDatabaseButton.setText("Свържи се!");
		connectToDatabaseButton.setBounds(132, 135, 106, 26);
		
		getContentPane().add(mysqlServerAddressTextField);
		mysqlServerAddressTextField.setText("127.0.0.1");
		mysqlServerAddressTextField.setBounds(181, 8, 185, 20);
		
		getContentPane().add(mysqlPortTextField);
		mysqlPortTextField.setText("3306");
		mysqlPortTextField.setBounds(181, 30, 91, 20);
		
		getContentPane().add(mysqlDatabaseNameTextField);
		mysqlDatabaseNameTextField.setText("shop");
		mysqlDatabaseNameTextField.setBounds(181, 52, 181, 20);
		
		getContentPane().add(mysqlUsernameTextField);
		mysqlUsernameTextField.setText("root");
		mysqlUsernameTextField.setBounds(181, 76, 180, 18);
		
		getContentPane().add(mysqlPasswordPasswordField);
		mysqlPasswordPasswordField.setBounds(181, 96, 181, 20);
	}
	private class ConnectToDatabaseButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			connectToDatabaseButton_actionPerformed(e);
		}
	}

	protected void connectToDatabaseButton_actionPerformed(ActionEvent e) {
		
		String password = "";
		
		char [] pass = mysqlPasswordPasswordField.getPassword();
		for (int i = 0; i < pass.length; i++) {
			
			password += pass[i];
		}
		
		dbPortal = new MySQLdbManager(mysqlServerAddressTextField.getText(),
				mysqlDatabaseNameTextField.getText(), Integer.parseInt(mysqlPortTextField.getText()),
				mysqlUsernameTextField.getText(), password);
		
		dbPortal.setMySQLTransactionIsolationLevel(MySQLdbManager.TRANSACTION_REPEATABLE_READ);		
		
		if (dbPortal.connect() == false) { // now check for database name that is not existing
			
			dbPortal.setMySqlActiveDatabase("");
			dbPortal.disconnect(); //just in case
			
			if (dbPortal.connect() == false) { // connection settings problem or not running MySQL server
				dbPortal.finalize();
				JOptionPane.showMessageDialog(this, "Грешка при свързването! Проверете настройките и дали MySQL сървът е стартиран!", 
						"Проблем с връзката!", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			else { // will have to create new database
				
				dbPortal.disconnect();
				
				if (JOptionPane.showConfirmDialog(this, "Ще бъде създадена нова база данни с име: " + mysqlDatabaseNameTextField.getText() +
						".\nСигурни ли сте?", "Създаване на нова база данни?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					if (createNewDatabaseAndTables() == false) { //in case of fail do not close this window
						
						return;
					}
				}
				else {
				
					dbPortal.finalize();
					return;
				}
				
				this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}			
		}
		else {
			
			// check for correct database (with all tables and columns needed)
			
			if (isConnectedDatabaseContainsAllNeededTablesAndColumns() == false) {
				
				dbPortal.finalize();
				JOptionPane.showMessageDialog(this, "Задали сте име на база данни, чието съдържание\n" + 
						"не е съвместимо с тази версия на eShop!!!", "Грешна база данни", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
		}		
		
		
		this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	private boolean createNewDatabaseAndTables() {
		
		if (dbPortal.connect() == false) {
			
			JOptionPane.showMessageDialog(this, "Грешка при свързването (1)! Проверете настройките и дали MySQL сървът е стартиран!", 
					"Проблем с връзката!", JOptionPane.ERROR_MESSAGE);			
			dbPortal.finalize();
			return false;
		}
		
		//FIXME prone??? to sql injection maybe??
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		
		if (dbPortal.executeNonQuery("CREATE DATABASE " + mysqlDatabaseNameTextField.getText() + " COLLATE utf8_general_ci") != 1) {
			
			dbPortal.freeQueryNonQueryTemporaryResults();
			dbPortal.executeNonQuery("DROP DATABASE " + mysqlDatabaseNameTextField.getText());
			dbPortal.finalize();
			JOptionPane.showMessageDialog(this, "Не може да се създаде база данни!\nПроверете имате ли права за това!", 
					"Не може да се създаде БД!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		dbPortal.setMySqlActiveDatabase(mysqlDatabaseNameTextField.getText());
		dbPortal.disconnect();
		
		if (dbPortal.connect() == false) { //false positive for database creation
			
			dbPortal.setMySqlActiveDatabase("");
			dbPortal.disconnect();
			
			if (dbPortal.connect()) {
				dbPortal.freeQueryNonQueryTemporaryResults();
				dbPortal.executeNonQuery("DROP DATABASE " + mysqlDatabaseNameTextField.getText());
				dbPortal.disconnect();
			}			
			
			JOptionPane.showMessageDialog(this, "Грешка при свързването (2)! Проверете настройките и дали MySQL сървът е стартиран!", 
					"Проблем с връзката!", JOptionPane.ERROR_MESSAGE);			
			dbPortal.finalize();
			return false;
		}
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		dbPortal.executeNonQuery("CREATE TABLE operators (operator_id int NOT NULL AUTO_INCREMENT primary key," + 
				"operator_username nvarchar(20) NOT NULL, operator_password nvarchar(64) NOT NULL," + 
				"operator_first_name nvarchar(20) NOT NULL, operator_last_name nvarchar(20) NOT NULL) ENGINE=InnoDB");
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		dbPortal.executeNonQuery("CREATE TABLE orders (order_id int NOT NULL AUTO_INCREMENT primary key," + 
				"order_time datetime NOT NULL, order_operator_id int NOT NULL," +
				"CONSTRAINT FK_OPERATORS FOREIGN KEY (order_operator_id) REFERENCES operators(operator_id) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB");

		dbPortal.freeQueryNonQueryTemporaryResults();
		dbPortal.executeNonQuery("CREATE TABLE products (product_id int NOT NULL AUTO_INCREMENT primary key," + 
				"product_name nvarchar(30) NOT NULL, product_quantity int NOT NULL,	product_price decimal(10,2) NOT NULL) ENGINE=InnoDB");

		dbPortal.freeQueryNonQueryTemporaryResults();
		dbPortal.executeNonQuery("CREATE TABLE order_details (order_detail_id int NOT NULL AUTO_INCREMENT primary key," + 
				"order_detail_order_id int NOT NULL, order_detail_product_id int NOT NULL, " +
				"order_detail_product_quantity int NOT NULL," +
				"CONSTRAINT FK_ORDERS FOREIGN KEY (order_detail_order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CONSTRAINT FK_PRODUCTS FOREIGN KEY (order_detail_product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB");
		
		/* for testing only; not needed any more
		try {
			if (dbPortal.executeNonQuery("INSERT INTO operators (operator_username, operator_password, operator_first_name, operator_last_name)" + 
					" VALUES ('operator1', '" + Md5hashcalc.calculateMD5hash("operator1")  + "', 'Оператор1', 'служебен акаунт')") != 1) {
				
				dbPortal.executeNonQuery("DROP DATABASE " + mysqlDatabaseNameTextField.getText());
				dbPortal.disconnect();
				dbPortal.finallize();
				
				JOptionPane.showMessageDialog(this, "Грешка при добавяне на запис в базата данни!\nПроверете правата на потребителя и връзката.", "Грешка при добавяне на данни в БД", JOptionPane.ERROR_MESSAGE);
				
				return false;
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Не може да се зареди криптиращ алгоритъм MD5!", "Фатална Грешка", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}*/
		
		return true;
	}
	
	private boolean isConnectedDatabaseContainsAllNeededTablesAndColumns() {
		
		ResultSet rs = null;
				
		//check for existing connection
		
		if (dbPortal == null) {			
			return false;
		}		
		if (!dbPortal.isConnected()) {
			return false;
		}
		
		// check for existing tables 
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT table_name FROM information_schema.tables WHERE " + 
				"table_schema = '" + dbPortal.getMySqlActiveDatabase() + "' AND (table_name = 'operators' OR " +
				"table_name = 'orders' OR table_name = 'products' OR table_name = 'order_details')");
		
		if (resultsetInformationCountCheckIsEqual(rs, 4) == false) { //there must be 4 tables
			return false;
		}
		
		// check for existing columns in operators table
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT * FROM information_schema.columns WHERE " + 
				"table_schema = '" + dbPortal.getMySqlActiveDatabase() + "' AND table_name = 'operators' AND (" +
				"column_name = 'operator_id' OR column_name = 'operator_username' OR column_name = 'operator_password' OR " +
				"column_name = 'operator_first_name' OR column_name = 'operator_last_name')");
		
		if (resultsetInformationCountCheckIsEqual(rs, 5) == false) { //there must be 5 columns
			return false;
		}
		
		// check for existing columns in operators table
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT table_name FROM information_schema.columns WHERE " + 
				"table_schema = '" + dbPortal.getMySqlActiveDatabase() + "' AND table_name = 'orders' AND (" +
				"column_name = 'order_id' OR column_name = 'order_time' OR column_name = 'order_operator_id')");
		
		if (resultsetInformationCountCheckIsEqual(rs, 3) == false) { //there must be 3 columns
			return false;
		}
		
		// check for existing columns in products table
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT * FROM information_schema.columns WHERE " + 
				"table_schema = '" + dbPortal.getMySqlActiveDatabase() + "' AND table_name = 'products' AND (" +
				"column_name = 'product_id' OR column_name = 'product_name' OR column_name = 'product_quantity' OR " +
				"column_name = 'product_price')");
		
		if (resultsetInformationCountCheckIsEqual(rs, 4) == false) { //there must be 4 columns
			return false;
		}
		
		// check for existing columns in order_details table
		
		dbPortal.freeQueryNonQueryTemporaryResults();
		rs = dbPortal.executeQuery("SELECT * FROM information_schema.columns WHERE " + 
				"table_schema = '" + dbPortal.getMySqlActiveDatabase() + "' AND table_name = 'order_details' AND (" +
				"column_name = 'order_detail_id' OR column_name = 'order_detail_order_id' OR " +
				"column_name = 'order_detail_product_id' OR column_name = 'order_detail_product_quantity')");
		
		if (resultsetInformationCountCheckIsEqual(rs, 4) == false) { //there must be 4 columns
			return false;
		}		
		
		return true;
	}
	
	private boolean resultsetInformationCountCheckIsEqual(ResultSet rs, int expectedCount) {
		
		if (rs == null) {
			
			return false;
		}
		else {
			
			try {
			
				if (rs.last()) {
					
					if (rs.getRow() != expectedCount) {
						
						return false;
					}
				}
				else {
					return false;
				}			
			}
			catch (SQLException ex) {
				
				return false;
			}			
		}
		
		return true;
	}
}
