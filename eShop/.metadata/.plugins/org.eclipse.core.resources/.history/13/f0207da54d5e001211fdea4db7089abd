import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class mainWindow extends JFrame {
	private final JPanel productsManagementToolsPanel = new JPanel();
	private final JLabel productsManagementToolsPanelProductNameLabel = new JLabel();
	private final JLabel productsManagementToolsPanelProductQuantityLabel = new JLabel();
	private final JLabel productsManagementToolsPanelProductPriceLabel = new JLabel();
	private final JTextField productsManagementToolsPanelProductNameTextField = new JTextField();
	private final JSpinner productsManagementToolsPanelProductPriceSpinner = new JSpinner();
	private final JSpinner productsManagementToolsPanelProductQuantitySpinner = new JSpinner();
	private final JButton productsManagementToolsPanelProductAddButton = new JButton();
	private final JButton productsManagementToolsPanelProductEditButton = new JButton();	
	private final JButton productsManagementToolsPanelProductRemoveButton = new JButton();
	
	class ProductsTableTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 3005L;
		
		private final String[] COLUMNS = new String[] {
			"Име на продукт", "Количество", "Цена в лева"
		};
		
		private String[][] CELLS = new String[][] {
			{"добави", "нов", "продукт","id"},
			/*{"1 - 0", "1 - 1", "1 - 2"},
			{"2 - 0", "2 - 1", "2 - 2"},
			{"3 - 0", "3 - 1", "3 - 2"},
			{"4 - 0", "4 - 1", "4 - 2"},*/
		};
		
		public int getRowCount() {
			return CELLS.length;
		}
		public int getColumnCount() {
			return COLUMNS.length;
		}
		public String getColumnName(int column) {
			return COLUMNS[column];
		}
		public Object getValueAt(int row, int column) {
			return CELLS[row].length > column ? CELLS[row][column] : (column + " - " + row);
		}
		
		public void populateTableWithDatabaseData() {
						
			int rowsCount = 0;
			
			ResultSet rs = databaseConnectWindow.dbPortal.executeQuery("SELECT * FROM products");
			if (rs != null) {
				try {
				
					rs.last();
					rowsCount = rs.getRow();
					if (rowsCount <= 0) {					
						return;
					}
					rs.first();
					
					CELLS = new String[rowsCount][4];
					
					while (true) {
						
						CELLS[rs.getRow() - 1][0] =	rs.getString(2);			   // product_name
						CELLS[rs.getRow() - 1][1] =	new String("" + rs.getInt(3)); // product_quantity
						CELLS[rs.getRow() - 1][2] =	rs.getBigDecimal(4).toString();// product_price
						CELLS[rs.getRow() - 1][3] =	new String("" + rs.getInt(1)); // product_id
						
						if (rs.isLast()) {
							break;
						}
						
						rs.next();
					}
					
				}
				catch (Exception ex) {
					
				}				
			}		
		}
		
		public void insertNewRow(Object name, Object quantity, Object price) {
			
			if (databaseConnectWindow.dbPortal.executeParameterizedNonQuery("INSERT INTO products (product_name, product_quantity, product_price) VALUES(?,?,?)", 
					name, quantity, price) != 1) {
				
				String errorMessage = "Грешка при добавянето на продукт:\n" + databaseConnectWindow.dbPortal.getLastError();
				
				JOptionPane.showMessageDialog(null, errorMessage, "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// populate the table with the new row
			
			int lastProductId = -1;
			
			ResultSet rs = databaseConnectWindow.dbPortal.executeParameterizedQuery("SELECT product_id FROM products WHERE product_name=? AND " +
					"product_quantity=? AND product_price=? ORDER BY product_id DESC", name, quantity, price);
			
			if (rs != null) {
				
				try {
					lastProductId = rs.getInt(1);
				}
				catch (Exception ex) {
					lastProductId = -1;
				}
			}
			
			if (lastProductId == -1) {
				JOptionPane.showMessageDialog(null, "Грешка при опресняването на таблицата с продукти!", "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String[][] newCells = new String[CELLS.length + 1][4];
			for (int i = 0; i < CELLS.length; i++) {
				newCells[i][0] = CELLS[i][0];
				newCells[i][1] = CELLS[i][1];
				newCells[i][2] = CELLS[i][2];
				newCells[i][3] = CELLS[i][3];
			}
			
			rs = null;
			rs = databaseConnectWindow.dbPortal.executeQuery("SELECT * FROM products WHERE product_id=" + lastProductId);
			
			if (rs != null) {
				
				try {
					
					newCells[CELLS.length][0] =	rs.getString(2);			   // product_name
					newCells[CELLS.length][1] =	new String("" + rs.getInt(3)); // product_quantity
					newCells[CELLS.length][2] =	rs.getBigDecimal(4).toString();// product_price
					newCells[CELLS.length][3] =	new String("" + rs.getInt(1)); // product_id
				}
				catch (Exception ex) {
					
					JOptionPane.showMessageDialog(null, "Грешка при опресняването на таблицата с продукти!\nВъзможно е добавянето на нов продукт да не е " + 
							"било успешно.", "Грешка", JOptionPane.ERROR_MESSAGE);
				}
				
				CELLS = newCells;
				
				fireTableCellUpdated(CELLS.length, 0); //visual optimized refresh
				fireTableCellUpdated(CELLS.length, 1);
				fireTableCellUpdated(CELLS.length, 2);
			}
			else {
				
				JOptionPane.showMessageDialog(null, "Грешка при опресняването на таблицата с продукти!\nВъзможно е добавянето на нов продукт да не е " + 
						"било успешно.", "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}		
		}
		
		public void removeSelectedRow(int rowNumber) {
			
			if (databaseConnectWindow.dbPortal.executeNonQuery("DELETE FROM products WHERE product_id=" + CELLS[rowNumber][3]) != 1) {
				
				String errorMessage = "Грешка при изтриването на продукт:\n" + databaseConnectWindow.dbPortal.getLastError();
				
				JOptionPane.showMessageDialog(null, errorMessage, "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// remove the deleted row from the table
			
			String[][] newCells = new String[CELLS.length - 1][4];
			
			for (int i = 0, j = 0; i < CELLS.length; i++) {
				
				if (i != rowNumber) {
				
					newCells[j][0] = CELLS[i][0];
					newCells[j][1] = CELLS[i][1];
					newCells[j][2] = CELLS[i][2];
					newCells[j][3] = CELLS[i][3];
					j++;
				}
			}
			
			CELLS = newCells;
			
			//no optimized visual refresh here
		}
		
		public void updateSelectedRow(int rowNumber, Object name, Object quantity, Object price) {
			
			Integer productId = Integer.parseInt(CELLS[rowNumber][3]);
			
			if (databaseConnectWindow.dbPortal.executeParameterizedNonQuery("UPDATE products SET product_name=?, product_quantity=?, product_price=? WHERE " + 
					"product_id=?", name, quantity, price, productId) != 1) {
				
				String errorMessage = "Грешка при редактирането на продукт:\n" + databaseConnectWindow.dbPortal.getLastError();
				
				JOptionPane.showMessageDialog(null, errorMessage, "Грешка", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ResultSet rs = null;
			rs = databaseConnectWindow.dbPortal.executeQuery("SELECT product_name, product_quantity, product_price FROM products WHERE product_id=" + productId);
			
			if (rs != null) {
				
				try {
					
					CELLS[rowNumber][0] = rs.getString(1); 	   			  // product_name
					CELLS[rowNumber][1] = new String("" + rs.getInt(2));  // product_quantity
					CELLS[rowNumber][2] = rs.getBigDecimal(3).toString(); // product_price
				}
				catch (Exception ex) {
					
				}
			}
			
			fireTableCellUpdated(rowNumber, 0); //visual optimized refresh
			fireTableCellUpdated(rowNumber, 1);
			fireTableCellUpdated(rowNumber, 2);
		}
	}

	private static final long serialVersionUID = 3001L;

	private final JMenuBar mainWindowMenu = new JMenuBar();
	private final JMenu File = new JMenu();
	private final JMenuItem FileConnectToDb = new JMenuItem();
	private final JMenu Operations = new JMenu();
	private final JMenu Help = new JMenu();
	private final JMenuItem HelpAbout = new JMenuItem();
	private final JMenuItem FileExit = new JMenuItem();
	private static mainWindow mainWindowPointer;
	
	private final JMenuItem FileDisconnectFromDb = new JMenuItem();
	private final JPanel mainWindowStatusPanel = new JPanel();
	private final JLabel mainWindowStatusPanelLoggedUserLabel = new JLabel();
	private final JButton mainWindowStatusPanelLoggedUserLogin = new JButton();
	private final JButton mainWindowStatusPanelLoggedUserSettings = new JButton();

	private final JLabel mainWindowStatusPanelSeparatorLabel = new JLabel();
	private final JLabel mainWindowStatusPanelSeparatorLabel2 = new JLabel();
	private final JButton mainWindowStatusPanelLoggedUserLogout = new JButton();
	
	private final JMenuItem operationsProductsManagement = new JMenuItem();
	private final JMenuItem operationsOrdersManagement = new JMenuItem();
	private final JMenuItem operationsNewOrder = new JMenuItem();
	
	private final JPanel productsManagementPanel = new JPanel();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable productsTable = new JTable();
	
	
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow frame = new mainWindow();
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
	public mainWindow() {
		super();
		setBounds(100, 100, 742, 438);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindowPointer = this;
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//		
	}
	private void jbInit() throws Exception {
		addWindowListener(new ThisWindowListener());
		getContentPane().setLayout(new BorderLayout());
		setTitle("eShop 1.00 (C) 2013 Желян Гуглев & Пламен Генчев");
		setName("mainWindow");
		
		setJMenuBar(mainWindowMenu);
		
		mainWindowMenu.add(File);
		File.setText("Файл");
		
		File.add(FileConnectToDb);
		FileConnectToDb.addActionListener(new FileConnectToDbActionListener());
		FileConnectToDb.setText("Свържи се с БД");
		
		File.add(FileDisconnectFromDb);
		FileDisconnectFromDb.addActionListener(new FileDisconnectFromDbActionListener());
		FileDisconnectFromDb.setText("Затвори връзката с БД");
		FileDisconnectFromDb.setEnabled(false);

		File.addSeparator();
		
		File.add(FileExit);
		FileExit.addActionListener(new FileExitActionListener());
		FileExit.setText("Изход");
		
		mainWindowMenu.add(Operations);
		Operations.setText("Операции");
		Operations.setEnabled(false);
		
		Operations.add(operationsNewOrder);
		operationsNewOrder.addActionListener(new OperationsNewOrderActionListener());
		operationsNewOrder.setText("Нова поръчка");

		Operations.addSeparator();
		
		Operations.add(operationsProductsManagement);
		operationsProductsManagement.addActionListener(new OperationsProductsManagementActionListener());
		operationsProductsManagement.setText("Управление на продукти");
		
		Operations.add(operationsOrdersManagement);
		operationsOrdersManagement.addActionListener(new OperationsOrdersManagementActionListener());
		operationsOrdersManagement.setText("Управление на поръчки");
		
		mainWindowMenu.add(Help);
		Help.setText("Помощ");
		
		Help.add(HelpAbout);
		HelpAbout.addActionListener(new HelpAboutActionListener());
		HelpAbout.setText("Относно");
		
		getContentPane().add(mainWindowStatusPanel, BorderLayout.SOUTH);
		mainWindowStatusPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("149dlu"),
				ColumnSpec.decode("72px"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16px")}));
		mainWindowStatusPanel.setSize(666, 25);
		mainWindowStatusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainWindowStatusPanel.setMinimumSize(new Dimension(666, 20));
		
		mainWindowStatusPanelLoggedUserLabel.setText("Потребител:");
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLabel, new CellConstraints("1, 1, 1, 2, fill, fill"));
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLogin, new CellConstraints(2, 1, 1, 2));
		mainWindowStatusPanelLoggedUserLogin.addActionListener(new MainWindowStatusPanelLoggedUserLoginActionListener());
		mainWindowStatusPanelLoggedUserLogin.setText("Вход");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserSettings, new CellConstraints(4, 1, 1, 2, CellConstraints.CENTER, CellConstraints.DEFAULT));
		mainWindowStatusPanelLoggedUserSettings.addActionListener(new MainWindowStatusPanelLoggedUserSettingsActionListener());
		mainWindowStatusPanelLoggedUserSettings.setMargin(new Insets(2, 14, 2, 14));
		mainWindowStatusPanelLoggedUserSettings.setText("Настройки");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelSeparatorLabel, new CellConstraints(3, 1, 1, 2));
		mainWindowStatusPanelSeparatorLabel.setText("   ");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelSeparatorLabel2, new CellConstraints(5, 1, 1, 2));
		mainWindowStatusPanelSeparatorLabel2.setText("   ");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLogout, new CellConstraints(6, 1, 1, 2));
		mainWindowStatusPanelLoggedUserLogout.addActionListener(new MainWindowStatusPanelLoggedUserLogoutActionListener());
		mainWindowStatusPanelLoggedUserLogout.setText("Изход");
		mainWindowStatusPanelSetEnabled(false);
		
		getContentPane().add(productsManagementPanel, BorderLayout.CENTER);
		productsManagementPanel.setLayout(new BorderLayout());		
		productsManagementPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setPreferredSize(new Dimension(0, 0));
		scrollPane.setBorder(new TitledBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null), "Продукти в базата данни:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		productsManagementPanel.setVisible(false);
		
		scrollPane.setViewportView(productsTable);
		productsTable.addMouseListener(new ProductsTableMouseListener());
		productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		productsTable.setShowGrid(true);
		productsTable.setModel(new ProductsTableTableModel());
		
		productsManagementPanel.add(productsManagementToolsPanel, BorderLayout.EAST);
		productsManagementToolsPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("92px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("92px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("52px"),
				ColumnSpec.decode("92px")},
			new RowSpec[] {
				RowSpec.decode("12dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC}));
		productsManagementToolsPanel.setBorder(new TitledBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null), "Добавяне/редактиране на продукт:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductNameLabel, new CellConstraints("2, 2, center, fill"));
		productsManagementToolsPanelProductNameLabel.setText("Име на продукт:");
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductPriceLabel, new CellConstraints("2, 3, fill, fill"));
		productsManagementToolsPanelProductPriceLabel.setText("Цена лв.");
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductQuantityLabel, new CellConstraints("2, 4, 1, 1, fill, fill"));
		productsManagementToolsPanelProductQuantityLabel.setText("Количество:");
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductNameTextField, new CellConstraints(4, 2, 4, 1, CellConstraints.FILL, CellConstraints.FILL));
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductPriceSpinner, new CellConstraints(4, 3, 3, 1, CellConstraints.FILL, CellConstraints.FILL));
		 
		SpinnerNumberModel productsManagementToolsPanelProductPriceSpinnerNumberModel = new SpinnerNumberModel(0.00, 0.00, 1000000.00, 0.01);
		productsManagementToolsPanelProductPriceSpinner.setModel(productsManagementToolsPanelProductPriceSpinnerNumberModel);		
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductQuantitySpinner, new CellConstraints(4, 4, 3, 1));
		SpinnerNumberModel productsManagementToolsPanelProductQuantitySpinnerNumberModel = new SpinnerNumberModel(0, 0, 1000000, 1);
		productsManagementToolsPanelProductQuantitySpinner.setModel(productsManagementToolsPanelProductQuantitySpinnerNumberModel);
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductAddButton, new CellConstraints(2, 6, 3, 1, CellConstraints.FILL, CellConstraints.FILL));
		productsManagementToolsPanelProductAddButton.addActionListener(new ProductsManagementToolsPanelProductAddButtonActionListener());
		productsManagementToolsPanelProductAddButton.setText("Добави нов продукт");
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductEditButton, new CellConstraints(6, 6, 2, 1, CellConstraints.FILL, CellConstraints.FILL));
		productsManagementToolsPanelProductEditButton.addActionListener(new ProductsManagementToolsPanelProductEditButtonActionListener());
		productsManagementToolsPanelProductEditButton.setText("Редактирай");
		
		productsManagementToolsPanel.add(productsManagementToolsPanelProductRemoveButton, new CellConstraints(4, 8, 3, 1));
		productsManagementToolsPanelProductRemoveButton.addActionListener(new ProductsManagementToolsPanelProductRemoveButtonActionListener());
		productsManagementToolsPanelProductRemoveButton.setText("Изтрий продукт");
	}
	
	protected void mainWindowStatusPanelSetEnabled(boolean enable) {
		
		Component[] comps = mainWindowStatusPanel.getComponents();
		
		for (int i = 0; i < comps.length; i++) {
		
			comps[i].setEnabled(enable);	
		}
		
		mainWindowStatusPanel.setEnabled(enable);
	}
	
	private class ThisWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			this_windowClosing(e);
		}
	}
	private class FileExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileExit_actionPerformed(e);
		}
	}
	private class FileConnectToDbActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileConnectToDb_actionPerformed(e);
		}
	}
	private class FileDisconnectFromDbActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileDisconnectFromDb_actionPerformed(e);
		}
	}
	private class MainWindowStatusPanelLoggedUserLoginActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			mainWindowStatusPanelLoggedUserLogin_actionPerformed(e);
		}
	}
	private class MainWindowStatusPanelLoggedUserSettingsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			mainWindowStatusPanelLoggedUserSettings_actionPerformed(e);
		}
	}
	private class MainWindowStatusPanelLoggedUserLogoutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			mainWindowStatusPanelLoggedUserLogout_actionPerformed(e);
		}
	}
	private class HelpAboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			helpAbout_actionPerformed(e);
		}
	}
	private class OperationsProductsManagementActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			operationsProductsManagement_actionPerformed(e);
		}
	}
	private class OperationsNewOrderActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			operationsNewOrder_actionPerformed(e);
		}
	}
	private class OperationsOrdersManagementActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			operationsOrdersManagement_actionPerformed(e);
		}
	}
	private class ProductsTableMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			productsTable_mouseClicked(e);
		}
	}
	private class ProductsManagementToolsPanelProductAddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			productsManagementToolsPanelProductAddButton_actionPerformed(e);
		}
	}
	private class ProductsManagementToolsPanelProductEditButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			productsManagementToolsPanelProductEditButton_actionPerformed(e);
		}
	}
	private class ProductsManagementToolsPanelProductRemoveButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			productsManagementToolsPanelProductRemoveButton_actionPerformed(e);
		}
	}

	protected void this_windowClosing(WindowEvent e) {
		
		//JOptionPane.showMessageDialog(null, "aa", "aa", JOptionPane.INFORMATION_MESSAGE);
		
		if (databaseConnectWindow.dbPortal != null) {
			
			if (databaseConnectWindow.dbPortal.isConnected()) {
				
				databaseConnectWindow.dbPortal.finalize();
			}
		}
		this.dispose();
		//System.exit(0);
	}
	
	protected void fileExit_actionPerformed(ActionEvent e) {

		this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	////////////////////////////////////////////////////////////////////////
	
	protected void fileConnectToDb_actionPerformed(ActionEvent e) {
		
		databaseConnectWindow dcw = new databaseConnectWindow();
		dcw.setVisible(true);
		dcw.addWindowListener(databaseConnectWindowClosing);
	}
	
	private static WindowListener databaseConnectWindowClosing = new WindowAdapter() {
		
        public void windowClosing(WindowEvent e) {
            
        	e.getWindow().dispose(); 
        	
        	if (databaseConnectWindow.dbPortal != null) {
    			
    			if (databaseConnectWindow.dbPortal.isConnected()) {
    				
    				mainWindowPointer.mainWindowStatusPanelSetEnabled(true);
    				mainWindowPointer.FileConnectToDb.setEnabled(false);
    				mainWindowPointer.FileDisconnectFromDb.setEnabled(true);
    			}
    			else {    				
    				mainWindowPointer.mainWindowStatusPanelSetEnabled(false);
    			}
    		}            
        }
    };
    
    ////////////////////////////////////////////////////////////////////////
    
	protected void fileDisconnectFromDb_actionPerformed(ActionEvent e) {
		
		if (databaseConnectWindow.dbPortal != null) {
			//TODO...
			databaseConnectWindow.dbPortal.finalize();
			
			this.FileConnectToDb.setEnabled(true);
			this.FileDisconnectFromDb.setEnabled(false);			
			this.Operations.setEnabled(false);
			this.mainWindowStatusPanelSetEnabled(false);
			this.productsManagementPanel.setVisible(false);
			this.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: ");
			operatorUserLoginWindow.loggedUserId = -1;
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	
	protected void mainWindowStatusPanelLoggedUserLogin_actionPerformed(ActionEvent e) {
		
		operatorUserLoginWindow oul = new operatorUserLoginWindow();
		oul.setVisible(true);
		oul.addWindowListener(operatorUserLoginWindowClosing);
	}
	private static WindowListener operatorUserLoginWindowClosing = new WindowAdapter() {

		public void windowClosing(WindowEvent e) {
			if (operatorUserLoginWindow.loggedUserId != -1) {
				
				mainWindowPointer.Operations.setEnabled(true);
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: " + operatorUserLoginWindow.loggedUserNames);
			}
			else {
				//TODO...
				mainWindowPointer.Operations.setEnabled(false);
				mainWindowPointer.productsManagementPanel.setVisible(false);
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: ");
			}
		}
	};
		
	////////////////////////////////////////////////////////////////////////
	
	protected void mainWindowStatusPanelLoggedUserSettings_actionPerformed(ActionEvent e) {
		
		if (operatorUserLoginWindow.loggedUserId == -1) {
			return;
		}
		
		operatorUserSettingsWindow ousw = new operatorUserSettingsWindow();
		ousw.setVisible(true);
		ousw.addWindowListener(operatorUserSettingsWindowClosing);
	}
	
	private static WindowListener operatorUserSettingsWindowClosing = new WindowAdapter() {
		
		public void windowClosing(WindowEvent e) {
			if (operatorUserLoginWindow.loggedUserId != -1) {
				
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: " + operatorUserSettingsWindow.operatorFirstNameLastNameCombination);
			}
			else { //if operator was deleted
				//TODO...
				mainWindowPointer.Operations.setEnabled(false);
				mainWindowPointer.productsManagementPanel.setVisible(false);
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: ");
			}
		}
	};
	
	////////////////////////////////////////////////////////////////////////
	
	protected void mainWindowStatusPanelLoggedUserLogout_actionPerformed(ActionEvent e) {
		//TODO...
		this.Operations.setEnabled(false);
		this.productsManagementPanel.setVisible(false);
		this.mainWindowStatusPanelLoggedUserLabel.setText("Потребител: ");
		operatorUserLoginWindow.loggedUserId = -1;
	}
	
	protected void helpAbout_actionPerformed(ActionEvent e) {
		
		JOptionPane.showMessageDialog(this, "eShop ver.-1.00\n(C) 2013 Желян Гуглев & Пламен Генчев", "Относно", JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void operationsProductsManagement_actionPerformed(ActionEvent e) {
		//TODO...
	    ((ProductsTableTableModel)productsTable.getModel()).populateTableWithDatabaseData();
		productsManagementPanel.setVisible(true);
	}
	protected void operationsNewOrder_actionPerformed(ActionEvent e) {
		//TODO...
		productsManagementPanel.setVisible(false);
	}
	protected void operationsOrdersManagement_actionPerformed(ActionEvent e) {
		//TODO...
		productsManagementPanel.setVisible(false);
	}
	
	protected void productsTable_mouseClicked(MouseEvent e) {
		
		productsManagementToolsPanelProductNameTextField.setText(productsTable.getValueAt(productsTable.getSelectedRow(), 0).toString());
		productsManagementToolsPanelProductQuantitySpinner.setValue(
				Integer.parseInt(productsTable.getValueAt(productsTable.getSelectedRow(), 1).toString())
				);
		productsManagementToolsPanelProductPriceSpinner.setValue(
				Double.parseDouble(productsTable.getValueAt(productsTable.getSelectedRow(), 2).toString())
				);		
	}
	
	////////////////////////////////////////////////////////////////////////
	
	protected void productsManagementToolsPanelProductAddButton_actionPerformed(ActionEvent e) {
		
		if (productsManagementToolsPanelProductNameTextField.getText().length() > 0) {
			
			((ProductsTableTableModel)productsTable.getModel()).insertNewRow(productsManagementToolsPanelProductNameTextField.getText(),
					Integer.parseInt(productsManagementToolsPanelProductQuantitySpinner.getValue().toString()),
					Double.parseDouble(productsManagementToolsPanelProductPriceSpinner.getValue().toString())
					);
			
			((ProductsTableTableModel)productsTable.getModel()).fireTableDataChanged();
		}
		else {
			JOptionPane.showMessageDialog(this, "Новият продукт задължително трябва да притежава име!", "Грешка", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	protected void productsManagementToolsPanelProductEditButton_actionPerformed(ActionEvent e) {
		
		if (productsManagementToolsPanelProductNameTextField.getText().length() > 0) {
			
			int selectedTableRow = productsTable.getSelectedRow();
			
			if (selectedTableRow == -1) {
				
				JOptionPane.showMessageDialog(this, "Изберете продукт!", "Редактиране", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			((ProductsTableTableModel)productsTable.getModel()).updateSelectedRow(selectedTableRow,
					productsManagementToolsPanelProductNameTextField.getText(),
					Integer.parseInt(productsManagementToolsPanelProductQuantitySpinner.getValue().toString()),
					Double.parseDouble(productsManagementToolsPanelProductPriceSpinner.getValue().toString())
					);
			((ProductsTableTableModel)productsTable.getModel()).fireTableDataChanged();
			
			productsTable.changeSelection(selectedTableRow, 0, true, false);
		}
		else {
			JOptionPane.showMessageDialog(this, "Редактираният продукт задължително трябва да притежава име!", "Грешка", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void productsManagementToolsPanelProductRemoveButton_actionPerformed(ActionEvent e) {
		
		if (productsTable.getSelectedRow() == -1) {
			
			JOptionPane.showMessageDialog(this, "Изберете продукт!", "Изтриване", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (JOptionPane.showConfirmDialog(this, "Сигурни ли сте?", "Изтриване на продукт", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			((ProductsTableTableModel)productsTable.getModel()).removeSelectedRow(productsTable.getSelectedRow());
			((ProductsTableTableModel)productsTable.getModel()).fireTableDataChanged();
		}
	}
	
	////////////////////////////////////////////////////////////////////////	
	
	
}
