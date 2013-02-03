import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import java.sql.ResultSet;
import md5_calculator.Md5hashcalc;

public class operatorUserLoginWindow extends /*JFrame*/ JDialog {
	private static final long serialVersionUID = 3003L;

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
	private final JLabel newPasswordAgainLabel = new JLabel();
	private final JPasswordField newPasswordPasswordAgainField = new JPasswordField();
	
	public static int loggedUserId = -1;
	public static String loggedUserNames = "";	
	
	/**
	 * Dialog constructor
	 * @param owner Owner of the window
	 * @param modal Modal dialog
	 */
	public operatorUserLoginWindow(Frame owner, boolean modal) {
		super(owner, modal);
		setBounds(100, 100, 518, 242);
		setLocationRelativeTo(null);
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
					operatorUserLoginWindow frame = new operatorUserLoginWindow();
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
	public operatorUserLoginWindow() {
		super();
		setBounds(100, 100, 518, 242);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}		
	}
	
	private void jbInit() throws Exception {
		
		getRootPane().setDefaultButton(loginButton);
		
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
		loginUsernameTextField.addFocusListener(new LoginUsernameTextFieldFocusListener());
		loginUsernameTextField.setBounds(96, 29, 137, 20);
		
		groupLoginPanel.add(loginPasswordPasswordField);
		loginPasswordPasswordField.addFocusListener(new LoginPasswordPasswordFieldFocusListener());
		loginPasswordPasswordField.setBounds(96, 51, 137, 20);
		
		getContentPane().add(groupCreateNewUserPanel);
		groupCreateNewUserPanel.setLayout(null);
		groupCreateNewUserPanel.setBorder(new TitledBorder(null, "��� ��������:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		groupCreateNewUserPanel.setBounds(259, 10, 243, 194);
		
		groupCreateNewUserPanel.add(newUsernameLabel);
		newUsernameLabel.setText("��� ����������:");
		newUsernameLabel.setBounds(10, 32, 95, 16);
		
		groupCreateNewUserPanel.add(newPasswordLabel);
		newPasswordLabel.setText("������:");
		newPasswordLabel.setBounds(10, 54, 95, 16);
		
		groupCreateNewUserPanel.add(newUserFirstNameLabel);
		newUserFirstNameLabel.setText("���:");
		newUserFirstNameLabel.setBounds(10, 98, 95, 16);
		
		groupCreateNewUserPanel.add(newUserLastNameLabel);
		newUserLastNameLabel.setText("�������:");
		newUserLastNameLabel.setBounds(10, 120, 95, 16);
		
		groupCreateNewUserPanel.add(createNewOperatorButton);
		createNewOperatorButton.addActionListener(new CreateNewOperatorButtonActionListener());
		createNewOperatorButton.setText("������");
		createNewOperatorButton.setBounds(127, 158, 106, 26);
		
		groupCreateNewUserPanel.add(newUsernameTextField);
		newUsernameTextField.setBounds(111, 30, 122, 20);
		
		groupCreateNewUserPanel.add(newFirstNameTextField);
		newFirstNameTextField.setBounds(111, 96, 122, 20);
		
		groupCreateNewUserPanel.add(newPasswordPasswordField);
		newPasswordPasswordField.setBounds(111, 52, 122, 20);
		
		groupCreateNewUserPanel.add(newLastNameTextField);
		newLastNameTextField.setBounds(111, 118, 122, 20);
		
		groupCreateNewUserPanel.add(newPasswordAgainLabel);
		newPasswordAgainLabel.setText("������ ������:");
		newPasswordAgainLabel.setBounds(10, 76, 95, 16);
		
		groupCreateNewUserPanel.add(newPasswordPasswordAgainField);
		newPasswordPasswordAgainField.setBounds(111, 75, 122, 20);
	}
	private class LoginButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			loginButton_actionPerformed(e);
		}
	}
	private class CreateNewOperatorButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createNewOperatorButton_actionPerformed(e);
		}
	}
	private class LoginPasswordPasswordFieldFocusListener extends FocusAdapter {
		public void focusGained(FocusEvent e) {
			loginPasswordPasswordField_focusGained(e);
		}
	}
	private class LoginUsernameTextFieldFocusListener extends FocusAdapter {
		public void focusGained(FocusEvent e) {
			loginUsernameTextField_focusGained(e);
		}
	}
	
	protected void loginButton_actionPerformed(ActionEvent e) {
		
		if (databaseConnectWindow.dbPortal != null) {
			
			if (databaseConnectWindow.dbPortal.isConnected()) {
				
				String password = "";
				
				char[] pass = loginPasswordPasswordField.getPassword();
				for (int i = 0; i < pass.length; i++) {
					
					password += pass[i];
				}
				
				ResultSet rs = null;
				
				try {				
					
					databaseConnectWindow.dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
					rs = databaseConnectWindow.dbPortal.executeParameterizedQuery("SELECT operator_id, operator_first_name, " + "" +
							"operator_last_name FROM operators WHERE operator_username = ? AND operator_password = ?", 
							loginUsernameTextField.getText(), Md5hashcalc.calculateMD5hash(password));							
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
				
				JOptionPane.showMessageDialog(this, "���� ������ � MySQL �������!", "������� � ��������", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else {
			
			JOptionPane.showMessageDialog(this, "���� ������ � MySQL �������!", "������� � ��������", JOptionPane.ERROR_MESSAGE);
			return;
		}		
	}
	
	protected void createNewOperatorButton_actionPerformed(ActionEvent e) {
		
		if (databaseConnectWindow.dbPortal != null) {
			
			if (databaseConnectWindow.dbPortal.isConnected()) {
				
				char[] pass = newPasswordPasswordField.getPassword();
				String password = "";
				String encryptedPassword = "";
				for (int i = 0; i < pass.length; i++) {
					
					password += pass[i];
				}
				
				char[] pass2 = newPasswordPasswordAgainField.getPassword();
				
				if (pass.length == pass2.length) {
				
						for (int i = 0; i < pass.length; i++) {
							
							if (pass[i] != pass2[i]) {
								
								JOptionPane.showMessageDialog(this, "�������� �� ��������!", "������", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
				}
				else {
				
					JOptionPane.showMessageDialog(this, "�������� �� ��������!", "������", JOptionPane.ERROR_MESSAGE);
					return;
				}
								
				if ((newUsernameTextField.getText().length() > 3) && (password.length() > 3) && 
						(newFirstNameTextField.getText().length() > 3) && (newLastNameTextField.getText().length() > 3)) {
					
					try {
						
						encryptedPassword = Md5hashcalc.calculateMD5hash(password);
					}
					catch (Exception ex) {
						
						JOptionPane.showMessageDialog(this, "������� ������ ��� ������������!", "������� ������", JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
					}
					
					databaseConnectWindow.dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
					ResultSet rs = databaseConnectWindow.dbPortal.executeParameterizedQuery(
							"SELECT COUNT(*) FROM operators WHERE operator_username=?",	newUsernameTextField.getText());
					
					int usersCount = 0;
					try {
						
						usersCount = rs.getInt(1);
					}
					catch (Exception ex) {						
					}					
					
					if ((databaseConnectWindow.dbPortal.getLastError() == null) && (usersCount == 0)) {						
						
						//no such existing username so now we create it
						
						databaseConnectWindow.dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
						
						if (databaseConnectWindow.dbPortal.executeParameterizedNonQuery(
								"INSERT INTO operators(operator_username, operator_password, operator_first_name, " + 
								"operator_last_name) VALUES(?, ?, ?, ?)", newUsernameTextField.getText(), encryptedPassword,
								newFirstNameTextField.getText(), newLastNameTextField.getText()) != 1) {
							
							JOptionPane.showMessageDialog(this, "������ ��� ��������� �� ��� ����������!", "������", JOptionPane.ERROR_MESSAGE);
						}
						else { //everything ok so login now
							
							loginUsernameTextField.setText(newUsernameTextField.getText());
							loginPasswordPasswordField.setText(password);
							loginButton_actionPerformed(null);
						}						
					}
					else {
						
						if (databaseConnectWindow.dbPortal.getLastError() != null) {
							
							JOptionPane.showMessageDialog(this, "���� ������ � MySQL �������!", "������� � ��������", JOptionPane.ERROR_MESSAGE);							
						}
						else {
							
							JOptionPane.showMessageDialog(this, "��������� �� ��� ������������� ��� ���� ����������.", "��������� �� ������������� ���", JOptionPane.ERROR_MESSAGE);							
						}
					}					
				}
				else {
					
					JOptionPane.showMessageDialog(this, "���������� � �� �������� ��-�������� ����� (������ �� 3 �������)!", "���������� �����", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				
				JOptionPane.showMessageDialog(this, "���� ������ � MySQL �������!", "������� � ��������", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			
			JOptionPane.showMessageDialog(this, "���� ������ � MySQL �������!", "������� � ��������", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	protected void loginPasswordPasswordField_focusGained(FocusEvent e) {
		
		loginPasswordPasswordField.setSelectionStart(0);
		loginPasswordPasswordField.setSelectionEnd(loginPasswordPasswordField.getPassword().length);		
	}
	
	protected void loginUsernameTextField_focusGained(FocusEvent e) {
		
		loginUsernameTextField.setSelectionStart(0);
		loginUsernameTextField.setSelectionEnd(loginUsernameTextField.getText().length());
	}

}
