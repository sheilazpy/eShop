import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import database_management.MySQLdbManager;

public class mainWindow extends JFrame {

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
	private final JPanel panel = new JPanel();
	private final JLabel mainWindowStatusPanelLoggedUserLabel = new JLabel();
	private final JButton mainWindowStatusPanelLoggedUserLogin = new JButton();
	private final JButton mainWindowStatusPanelLoggedUserSettings = new JButton();

	private final JLabel mainWindowStatusPanelSeparatorLabel = new JLabel();
	private final JLabel mainWindowStatusPanelSeparatorLabel2 = new JLabel();
	private final JButton mainWindowStatusPanelLoggedUserLogout = new JButton();
	
	private final JMenuItem operationsProductsManagement = new JMenuItem();
	private final JMenuItem operationsOrdersManagement = new JMenuItem();
	private final JMenuItem operationsNewOrder = new JMenuItem();
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
		setBounds(100, 100, 682, 438);
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
		setTitle("eShop 1.00 (C) 2013 ����� ������ & ������ ������");
		setName("mainWindow");
		
		setJMenuBar(mainWindowMenu);
		
		mainWindowMenu.add(File);
		File.setText("����");
		
		File.add(FileConnectToDb);
		FileConnectToDb.addActionListener(new FileConnectToDbActionListener());
		FileConnectToDb.setText("������ �� � ��");
		
		File.add(FileDisconnectFromDb);
		FileDisconnectFromDb.addActionListener(new FileDisconnectFromDbActionListener());
		FileDisconnectFromDb.setText("������� �������� � ��");
		FileDisconnectFromDb.setEnabled(false);

		File.addSeparator();
		
		File.add(FileExit);
		FileExit.addActionListener(new FileExitActionListener());
		FileExit.setText("�����");
		
		mainWindowMenu.add(Operations);
		Operations.setText("��������");
		Operations.setEnabled(false);
		
		Operations.add(operationsNewOrder);
		operationsNewOrder.addActionListener(new OperationsNewOrderActionListener());
		operationsNewOrder.setText("���� �������");

		Operations.addSeparator();
		
		Operations.add(operationsProductsManagement);
		operationsProductsManagement.addActionListener(new OperationsProductsManagementActionListener());
		operationsProductsManagement.setText("���������� �� ��������");
		
		Operations.add(operationsOrdersManagement);
		operationsOrdersManagement.addActionListener(new OperationsOrdersManagementActionListener());
		operationsOrdersManagement.setText("���������� �� �������");
		
		mainWindowMenu.add(Help);
		Help.setText("�����");
		
		Help.add(HelpAbout);
		HelpAbout.addActionListener(new HelpAboutActionListener());
		HelpAbout.setText("�������");
		
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
		
		mainWindowStatusPanelLoggedUserLabel.setText("����������:");
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLabel, new CellConstraints("1, 1, 1, 2, fill, fill"));
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLogin, new CellConstraints(2, 1, 1, 2));
		mainWindowStatusPanelLoggedUserLogin.addActionListener(new MainWindowStatusPanelLoggedUserLoginActionListener());
		mainWindowStatusPanelLoggedUserLogin.setText("����");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserSettings, new CellConstraints(4, 1, 1, 2, CellConstraints.CENTER, CellConstraints.DEFAULT));
		mainWindowStatusPanelLoggedUserSettings.addActionListener(new MainWindowStatusPanelLoggedUserSettingsActionListener());
		mainWindowStatusPanelLoggedUserSettings.setMargin(new Insets(2, 14, 2, 14));
		mainWindowStatusPanelLoggedUserSettings.setText("���������");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelSeparatorLabel, new CellConstraints(3, 1, 1, 2));
		mainWindowStatusPanelSeparatorLabel.setText("   ");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelSeparatorLabel2, new CellConstraints(5, 1, 1, 2));
		mainWindowStatusPanelSeparatorLabel2.setText("   ");
		
		mainWindowStatusPanel.add(mainWindowStatusPanelLoggedUserLogout, new CellConstraints(6, 1, 1, 2));
		mainWindowStatusPanelLoggedUserLogout.addActionListener(new MainWindowStatusPanelLoggedUserLogoutActionListener());
		mainWindowStatusPanelLoggedUserLogout.setText("�����");
		mainWindowStatusPanelSetEnabled(false);
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
			
			databaseConnectWindow.dbPortal.finalize();
			
			this.FileConnectToDb.setEnabled(true);
			this.FileDisconnectFromDb.setEnabled(false);			
			this.Operations.setEnabled(false);
			this.mainWindowStatusPanelSetEnabled(false);	
			this.mainWindowStatusPanelLoggedUserLabel.setText("����������: ");
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
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("����������: " + operatorUserLoginWindow.loggedUserNames);
			}
			else {
				
				mainWindowPointer.Operations.setEnabled(false);
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("����������: ");
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
				
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("����������: " + operatorUserSettingsWindow.operatorFirstNameLastNameCombination);
			}
			else { //if operator was deleted
				
				mainWindowPointer.Operations.setEnabled(false);
				mainWindowPointer.mainWindowStatusPanelLoggedUserLabel.setText("����������: ");
			}
		}
	};
	
	////////////////////////////////////////////////////////////////////////
	
	protected void mainWindowStatusPanelLoggedUserLogout_actionPerformed(ActionEvent e) {
		
		this.Operations.setEnabled(false);
		this.mainWindowStatusPanelLoggedUserLabel.setText("����������: ");
		operatorUserLoginWindow.loggedUserId = -1;
	}
	
	protected void helpAbout_actionPerformed(ActionEvent e) {
		
		JOptionPane.showMessageDialog(this, "eShop ver.-1.00\n(C) 2013 ����� ������ & ������ ������", "�������", JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void operationsProductsManagement_actionPerformed(ActionEvent e) {
		//TODO...		
	}
	protected void operationsNewOrder_actionPerformed(ActionEvent e) {
		//TODO...
	}
	protected void operationsOrdersManagement_actionPerformed(ActionEvent e) {
		//TODO...
	}
	
	
	
	
}
