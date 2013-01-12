import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import database_management.MySQLdbManager;

public class databaseConnectWindow extends JFrame {

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
	 * Launch the application
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
	 * Create the frame
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
		getContentPane().setLayout(null);
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("��������� � ������ �����");
		setName("databaseConnectWindowFrame");
		
		getContentPane().add(label);
		label.setText("����� �� MySQL �������:");
		label.setBounds(10, 10, 165, 16);
		
		getContentPane().add(label_1);
		label_1.setText("��� �� ������ �����:");
		label_1.setBounds(10, 54, 165, 16);
		
		getContentPane().add(label_2);
		label_2.setText("����:");
		label_2.setBounds(10, 32, 156, 16);
		
		getContentPane().add(label_3);
		label_3.setText("������������� ���:");
		label_3.setBounds(10, 76, 165, 16);
		
		getContentPane().add(label_4);
		label_4.setText("������:");
		label_4.setBounds(10, 98, 156, 16);
		
		getContentPane().add(connectToDatabaseButton);
		connectToDatabaseButton.addActionListener(new ConnectToDatabaseButtonActionListener());
		connectToDatabaseButton.setText("������ ��!");
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
		
		
		if (dbPortal.connect() == false) { //now check for database name that is not existing
			
			dbPortal.setMySqlActiveDatabase("");
			dbPortal.disconnect(); //just in case
			
			if (dbPortal.connect() == false) { //connection settings problem or not running MySQL server
				dbPortal.finallize();
				JOptionPane.showMessageDialog(this, "������ ��� �����������! ��������� ����������� � ���� MySQL ������ � ���������!", 
						"������� � ��������!", JOptionPane.ERROR_MESSAGE);
				
				return;
			}
			else { //will have to create new database
				
				dbPortal.disconnect();
				
				if (JOptionPane.showConfirmDialog(this, "�� ���� ��������� ���� ���� ����� � ���: " + mysqlDatabaseNameTextField.getText() +
						".\n������� �� ���?", "��������� �� ���� ���� �����?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					if (createNewDatabaseAndTables() == false) { //in case of fail do not close this window
						
						return;
					}					
				}
				else {
				
					dbPortal.finallize();
					return;
				}
				
				this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}			
		}
		
		this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	private boolean createNewDatabaseAndTables() {
		
		if (dbPortal.connect() == false) {
			
			JOptionPane.showMessageDialog(this, "������ ��� ����������� (1)! ��������� ����������� � ���� MySQL ������ � ���������!", 
					"������� � ��������!", JOptionPane.ERROR_MESSAGE);			
			dbPortal.finallize();
			return false;
		}
		
		if (dbPortal.executeNonQuery("CREATE DATABASE " + mysqlDatabaseNameTextField.getText()) != 1) {
			
			dbPortal.executeNonQuery("DROP DATABASE " + mysqlDatabaseNameTextField.getText());
			dbPortal.disconnect();
			dbPortal.finallize();
			JOptionPane.showMessageDialog(this, "�� ���� �� �� ������� ���� �����!\n��������� ����� �� ����� �� ����!", 
					"�� ���� �� �� ������� ��!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		dbPortal.setMySqlActiveDatabase(mysqlDatabaseNameTextField.getText());
		dbPortal.disconnect();
		
		if (dbPortal.connect() == false) { //false positive for database creation
			
			dbPortal.setMySqlActiveDatabase("");
			dbPortal.disconnect();
			
			if (dbPortal.connect()) {
				dbPortal.executeNonQuery("DROP DATABASE " + mysqlDatabaseNameTextField.getText());
				dbPortal.disconnect();
			}			
			
			JOptionPane.showMessageDialog(this, "������ ��� ����������� (2)! ��������� ����������� � ���� MySQL ������ � ���������!", 
					"������� � ��������!", JOptionPane.ERROR_MESSAGE);			
			dbPortal.finallize();
			return false;
		}
		
		dbPortal.executeNonQuery("CREATE TABLE operators (operator_id int NOT NULL AUTO_INCREMENT primary key," + 
				"operator_username nvarchar(20) NOT NULL, operator_password nvarchar(20) NOT NULL," + 
				"operator_first_name nvarchar(20) NOT NULL, operator_last_name nvarchar(20) NOT NULL)");
		
		dbPortal.executeNonQuery("CREATE TABLE orders (order_id int NOT NULL AUTO_INCREMENT primary key," + 
				"order_time datetime NOT NULL, order_operator_id int NOT NULL," +
				"CONSTRAINT FK_OPERATORS FOREIGN KEY (order_operator_id) REFERENCES operators(operator_id) ON DELETE CASCADE ON UPDATE CASCADE)");

		dbPortal.executeNonQuery("CREATE TABLE products (product_id int NOT NULL AUTO_INCREMENT primary key," + 
				"product_name nvarchar(30) NOT NULL, product_quantity int NOT NULL,	product_price decimal(10,2) NOT NULL)");

		dbPortal.executeNonQuery("CREATE TABLE order_details (order_detail_id int NOT NULL AUTO_INCREMENT primary key," + 
				"order_detail_order_id int NOT NULL, order_detail_product_id int NOT NULL, " +
				"order_detail_product_quantity int NOT NULL," +
				"CONSTRAINT FK_ORDERS FOREIGN KEY (order_detail_order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CONSTRAINT FK_PRODUCTS FOREIGN KEY (order_detail_product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE)");
		
		return true;
	}
}
