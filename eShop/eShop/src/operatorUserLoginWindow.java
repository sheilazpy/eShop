import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import java.sql.ResultSet;

import database_management.MySQLdbManager;
import md5_calculator.Md5hashcalc;

public class operatorUserLoginWindow extends JFrame {

	private final JPanel groupLoginPanel = new JPanel();
	private final JPanel groupCreateNewUserPanel = new JPanel();
	private final JLabel loginUsernameLabel = new JLabel();
	private final JLabel loginPasswordLabel = new JLabel();
	private final JButton loginButton = new JButton();
	private final JTextField loginUsernameTextField = new JTextField();
	private final JPasswordField loginPasswordPasswordField = new JPasswordField();
	private final JLabel newUsernameLabel = new JLabel();
	private final JLabel newPasswordLabel = new JLabel();
	private final JLabel newUserFirstNameLabel = new JLabel();
	private final JLabel newUserLastNameLabel = new JLabel();
	private final JButton createNewOperatorButton = new JButton();
	private final JTextField newUsernameTextField = new JTextField();
	private final JTextField newFirstNameTextField = new JTextField();
	private final JPasswordField newPasswordPasswordField = new JPasswordField();
	private final JTextField newLastNameTextField = new JTextField();
	
	public static int loggedUserId = -1;
	public static String loggedUserNames = "";
	
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					operatorUserLoginWindow frame = new operatorUserLoginWindow();
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
	public operatorUserLoginWindow() {
		super();
		setBounds(100, 100, 518, 221);
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
		setTitle("���� �� ��������");
		setName("operatorLoginWindow");
		setResizable(false);
		setAlwaysOnTop(true);
		
		getContentPane().add(groupLoginPanel);
		groupLoginPanel.setLayout(null);
		groupLoginPanel.setBorder(new TitledBorder(null, "����:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		groupLoginPanel.setBounds(10, 10, 243, 133);
		
		groupLoginPanel.add(loginUsernameLabel);
		loginUsernameLabel.setText("����������:");
		loginUsernameLabel.setBounds(10, 31, 80, 16);
		
		groupLoginPanel.add(loginPasswordLabel);
		loginPasswordLabel.setText("������:");
		loginPasswordLabel.setBounds(10, 53, 80, 16);
		
		groupLoginPanel.add(loginButton);
		loginButton.addActionListener(new LoginButtonActionListener());
		loginButton.setText("����");
		loginButton.setBounds(127, 93, 106, 26);
		
		groupLoginPanel.add(loginUsernameTextField);
		loginUsernameTextField.setBounds(96, 29, 137, 20);
		
		groupLoginPanel.add(loginPasswordPasswordField);
		loginPasswordPasswordField.setBounds(96, 51, 137, 20);
		
		getContentPane().add(groupCreateNewUserPanel);
		groupCreateNewUserPanel.setLayout(null);
		groupCreateNewUserPanel.setBorder(new TitledBorder(null, "��� ��������:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		groupCreateNewUserPanel.setBounds(263, 10, 243, 178);
		
		groupCreateNewUserPanel.add(newUsernameLabel);
		newUsernameLabel.setText("��� ����������:");
		newUsernameLabel.setBounds(10, 32, 95, 16);
		
		groupCreateNewUserPanel.add(newPasswordLabel);
		newPasswordLabel.setText("������:");
		newPasswordLabel.setBounds(10, 54, 95, 16);
		
		groupCreateNewUserPanel.add(newUserFirstNameLabel);
		newUserFirstNameLabel.setText("���:");
		newUserFirstNameLabel.setBounds(10, 76, 95, 16);
		
		groupCreateNewUserPanel.add(newUserLastNameLabel);
		newUserLastNameLabel.setText("�������:");
		newUserLastNameLabel.setBounds(10, 98, 95, 16);
		
		groupCreateNewUserPanel.add(createNewOperatorButton);
		createNewOperatorButton.setText("������");
		createNewOperatorButton.setBounds(127, 142, 106, 26);
		
		groupCreateNewUserPanel.add(newUsernameTextField);
		newUsernameTextField.setBounds(111, 30, 122, 20);
		
		groupCreateNewUserPanel.add(newFirstNameTextField);
		newFirstNameTextField.setBounds(111, 74, 122, 20);
		
		groupCreateNewUserPanel.add(newPasswordPasswordField);
		newPasswordPasswordField.setBounds(111, 52, 122, 20);
		
		groupCreateNewUserPanel.add(newLastNameTextField);
		newLastNameTextField.setBounds(111, 96, 122, 20);
	}
	private class LoginButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			loginButton_actionPerformed(e);
		}
	}
	
	protected void loginButton_actionPerformed(ActionEvent e) {
		
		if (databaseConnectWindow.dbPortal != null) {
			
			if (databaseConnectWindow.dbPortal.isConnected()) {
				
				String password = "";
				
				char [] pass = loginPasswordPasswordField.getPassword();
				for (int i = 0; i < pass.length; i++) {
					
					password += pass[i];
				}
				
				//FIXME prone to sql injection
				
				ResultSet rs = null;
				
				try {				
					
					rs = databaseConnectWindow.dbPortal.executeQuery("SELECT operator_id, operator_first_name, operator_last_name FROM operators WHERE" + 
						" operator_username='" + loginUsernameTextField.getText() + "' AND operator_password='" +
						Md5hashcalc.calculateMD5hash(password) + "'");
				}
				catch (Exception ex) {
					System.exit(-1);
				}
				
				if (rs != null) {
					
					try {
						
						loggedUserId = rs.getInt(1);
						loggedUserNames = rs.getString(2) + " " + rs.getString(3);
					}
					catch (Exception ex) {
						
						JOptionPane.showMessageDialog(this, "������ ���������� ��� ������!", "������ ��� ����", 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				}
				else {
					JOptionPane.showMessageDialog(this, "������ ���������� ��� ������!", "������ ��� ����", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				return;
			}
		}
		else {
			return;
		}
		
	}

}
