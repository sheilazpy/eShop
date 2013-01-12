import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

public class mainWindow extends JFrame {

	private final JMenuBar mainWindowMenu = new JMenuBar();
	private final JMenu File = new JMenu();
	private final JMenuItem FileConnectToDb = new JMenuItem();
	private final JMenu Operations = new JMenu();
	private final JMenu Help = new JMenu();
	private final JMenuItem HelpAbout = new JMenuItem();
	private final JMenuItem FileExit = new JMenuItem();
	private static mainWindow mainWindowPointer;
	
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
		getContentPane().setLayout(null);
		addWindowListener(new ThisWindowListener());
		setTitle("eShop 1.00 (C) 2013 ����� ������ & ������ ������");
		setName("mainWindow");
		
		setJMenuBar(mainWindowMenu);
		
		mainWindowMenu.add(File);
		File.setText("����");
		
		File.add(FileConnectToDb);
		FileConnectToDb.addActionListener(new FileConnectToDbActionListener());
		FileConnectToDb.setText("������ �� � ��");

		File.addSeparator();
		
		File.add(FileExit);
		FileExit.addActionListener(new FileExitActionListener());
		FileExit.setText("�����");
		
		mainWindowMenu.add(Operations);
		Operations.setText("��������");
		Operations.setEnabled(false);
		
		mainWindowMenu.add(Help);
		Help.setText("�����");
		
		Help.add(HelpAbout);
		HelpAbout.setText("�������");
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

	protected void this_windowClosing(WindowEvent e) {
		
		//JOptionPane.showMessageDialog(null, "aa", "aa", JOptionPane.INFORMATION_MESSAGE);
		
		if (databaseConnectWindow.dbPortal != null) {
			
			if (databaseConnectWindow.dbPortal.isConnected()) {
				
				databaseConnectWindow.dbPortal.disconnect();
				databaseConnectWindow.dbPortal.finallize();
			}
		}
		this.dispose();
		//System.exit(0);
	}
	
	protected void fileExit_actionPerformed(ActionEvent e) {

		this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	protected void fileConnectToDb_actionPerformed(ActionEvent e) {
		
		//databaseConnectWindow.main(null);
		databaseConnectWindow dcw = new databaseConnectWindow();
		dcw.setVisible(true);
		dcw.addWindowListener(databaseConnectWindowClosing);
	}
	private static WindowListener databaseConnectWindowClosing = new WindowAdapter() {
		
        public void windowClosing(WindowEvent e) {
            
        	e.getWindow().dispose(); 
        	
        	if (databaseConnectWindow.dbPortal != null) {
    			
    			if (databaseConnectWindow.dbPortal.isConnected()) {
    				mainWindowPointer.Operations.setEnabled(true);
    			}
    			else {
    				mainWindowPointer.Operations.setEnabled(false);
    			}
    		}            
        }
    };
	
}
