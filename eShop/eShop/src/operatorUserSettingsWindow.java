import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.sql.ResultSet;
import md5_calculator.Md5hashcalc;

public class operatorUserSettingsWindow extends /*JFrame*/ JDialog {
	private static final long serialVersionUID = 3004L;

	private final JLabel usernameLabel = new JLabel();
	private final JLabel passwordLabel = new JLabel();
	private final JLabel passwordAgainLabel = new JLabel();
	private final JLabel nameLabel = new JLabel();
	private final JLabel lastNameLabel = new JLabel();
	private final JButton updateButton = new JButton();
	private final JButton deleteOperator = new JButton();
	private final JTextField firstNameTextField = new JTextField();
	private final JTextField lastNameTextField = new JTextField();
	private final JPasswordField passwordPasswordField = new JPasswordField();
	private final JPasswordField passwordAgainPasswordField = new JPasswordField();	
	
	private String currentOperatorPassword = "";
	public static String operatorFirstNameLastNameCombination = "";	
	
	/**
	 * Dialog constructor
	 * @param owner Owner of the window
	 * @param modal Modal dialog
	 */
	public operatorUserSettingsWindow(Frame owner, boolean modal) {
		super(owner, modal);
		setBounds(100, 100, 314, 255);
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
					operatorUserSettingsWindow frame = new operatorUserSettingsWindow();
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
	public operatorUserSettingsWindow() {
		super();
		setBounds(100, 100, 314, 255);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			jbInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}		
	}
	
	private void jbInit() throws Exception {
		getContentPane().setLayout(null);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("��������� �� ����������");
		setName("operatorUserSettings");
		
		getContentPane().add(usernameLabel);
		usernameLabel.setText("����������:");
		usernameLabel.setBounds(10, 0, 278, 16);
		
		getContentPane().add(passwordLabel);
		passwordLabel.setText("������:");
		passwordLabel.setBounds(10, 76, 97, 16);
		
		getContentPane().add(passwordAgainLabel);
		passwordAgainLabel.setText("������ ������:");
		passwordAgainLabel.setBounds(10, 95, 97, 16);
		
		getContentPane().add(nameLabel);
		nameLabel.setText("���:");
		nameLabel.setBounds(10, 32, 97, 16);
		
		getContentPane().add(lastNameLabel);
		lastNameLabel.setText("�������:");
		lastNameLabel.setBounds(10, 54, 97, 16);
		
		getContentPane().add(updateButton);
		updateButton.addActionListener(new UpdateButtonActionListener());
		updateButton.setText("������");
		updateButton.setBounds(90, 130, 137, 26);
		
		getContentPane().add(firstNameTextField);
		firstNameTextField.setBounds(113, 30, 175, 20);
		
		getContentPane().add(lastNameTextField);
		lastNameTextField.setBounds(113, 52, 175, 20);
		
		getContentPane().add(passwordPasswordField);
		passwordPasswordField.setBounds(113, 74, 175, 20);
		
		getContentPane().add(passwordAgainPasswordField);
		passwordAgainPasswordField.setBounds(113, 97, 175, 20);
		
		getCurrentUserData();
		
		getContentPane().add(deleteOperator);
		deleteOperator.addActionListener(new DeleteOperatorActionListener());
		deleteOperator.setText("������ ��������");
		deleteOperator.setBounds(90, 189, 137, 26);
	}
	
	private void getCurrentUserData() {
		
		if (databaseConnectWindow.dbPortal == null) {
			return;
		}
		if (databaseConnectWindow.dbPortal.isConnected() == false) {
			return;
		}
		if (operatorUserLoginWindow.loggedUserId == -1) {
			return;
		}
		
		databaseConnectWindow.dbPortal.freeQueryNonQueryTemporaryResults();
		ResultSet rs = databaseConnectWindow.dbPortal.executeQuery("SELECT operator_username, operator_password, " + 
				"operator_first_name, operator_last_name FROM operators WHERE operator_id=" + operatorUserLoginWindow.loggedUserId);
		
		if ((rs == null) || (databaseConnectWindow.dbPortal.getLastError() != null)) {
			return;
		}
		
		try {
			
			usernameLabel.setText("����������: " + rs.getString(1));
			currentOperatorPassword = rs.getString(2);
			firstNameTextField.setText(rs.getString(3));
			lastNameTextField.setText(rs.getString(4));
		}
		catch (Exception ex) {			
		}
		
		operatorFirstNameLastNameCombination = firstNameTextField.getText() + " " + lastNameTextField.getText();
	}
	
	private class UpdateButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateButton_actionPerformed(e);
		}
	}
	private class DeleteOperatorActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			deleteOperator_actionPerformed(e);
		}
	}
	protected void updateButton_actionPerformed(ActionEvent e) {
		
		char[] pass = passwordPasswordField.getPassword();
		String password = "";
		
		for (int i = 0; i < pass.length; i++) {
			
			password += pass[i];
		}
		
		char[] pass2 = passwordAgainPasswordField.getPassword();
		String password2 = "";
		
		for (int j = 0; j < pass2.length; j++) {
			
			password2 += pass2[j];
		}
		
		if (password.compareTo(password2) != 0) {
			
			JOptionPane.showMessageDialog(this, "�������� �� ��������!", "������ ��� ������� �� ������", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (password.length() > 3) { //password update will be performed too
			
			try {
				
				password = Md5hashcalc.calculateMD5hash(password);
				currentOperatorPassword = password;
			}
			catch (Exception ex) {
				
				JOptionPane.showMessageDialog(this, "������� � ���������� �� ���� �� �������� ����\n�� ����� �������� ������ �������� � ������������!", "������� ��� ������� �� ������", JOptionPane.ERROR_MESSAGE);
			}			
		}
		else {
			if (password.length() > 0) {
				
				JOptionPane.showMessageDialog(this, "�������� ������ �� � ���� 4 �������!", "������ ��� ������� �� ������", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if ((firstNameTextField.getText().length() <= 3) || (lastNameTextField.getText().length() <= 3)) {
			
			JOptionPane.showMessageDialog(this, "��������� ������ ��� ��� ������� (������ �� �� ������ �� 3 �������)!", "������ ��� ������������", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		databaseConnectWindow.dbPortal.freeParameterizedQueryNonQueryTemporaryResults();
		if (databaseConnectWindow.dbPortal.executeParameterizedNonQuery("UPDATE operators SET operator_password=?, " +
				"operator_first_name=?, operator_last_name=? WHERE operator_id=?", currentOperatorPassword, 
				firstNameTextField.getText(), lastNameTextField.getText(), new Integer(operatorUserLoginWindow.loggedUserId)) != 1) {
		
			JOptionPane.showMessageDialog(this, "������ ��� ���������� �� �������!", "������ ��� ������������", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		operatorFirstNameLastNameCombination = firstNameTextField.getText() + " " + lastNameTextField.getText();
		operatorUserLoginWindow.loggedUserNames = operatorFirstNameLastNameCombination; //in case
		
		this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	protected void deleteOperator_actionPerformed(ActionEvent e) {
		
		//we can not delete not existing operator
		if (operatorUserLoginWindow.loggedUserId == -1) {
			return;
		}
		
		if (JOptionPane.showConfirmDialog(this, "����������� �� ��������� �� �������� �� ������ �����\n" + 
				"� ������ ��������, ����� ��� � ��������.\n\n\t���������?", "���������� ��������� �� ��������", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			databaseConnectWindow.dbPortal.freeQueryNonQueryTemporaryResults();
			if (databaseConnectWindow.dbPortal.executeNonQuery("DELETE FROM operators WHERE operator_id=" + operatorUserLoginWindow.loggedUserId) != 1) {

				JOptionPane.showMessageDialog(this, "����������� � ���������!", "��������� ��������� �� ��������", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			operatorUserLoginWindow.loggedUserId = -1;
			this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		
		return;
	}

}
