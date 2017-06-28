package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Queue;

import javax.management.Query;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class SignUpView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1913334180086935280L;
	
	private JLabel usernameL, lastNameL, firstNameL, emailL, phoneL, addressL,
					passwordL, confirmPasswordL;
	private JTextField usernameTF, lastNameTF, firstNameTF, emailTF, phoneTF,
						addressTF;
	private JPasswordField passwordTF, confirmPasswordTF;
	private JButton signupB, backB;
	private CardLayout viewCardLayout;
	private JPanel loginPanel;
	
	public SignUpView(CardLayout cl, JPanel loginPanel) {
		this.setLayout(new GridLayout(Constants.USER_ATTRIBUTE_COUNT+1, 2,5,5));
		this.viewCardLayout = cl;
		this.loginPanel = loginPanel;
		
		initialize();
		
		this.add(usernameL);
		this.add(usernameTF);
		this.add(passwordL);
		this.add(passwordTF);
		this.add(confirmPasswordL);
		this.add(confirmPasswordTF);
		this.add(firstNameL);
		this.add(firstNameTF);
		this.add(lastNameL);
		this.add(lastNameTF);
		this.add(emailL);
		this.add(emailTF);
		this.add(phoneL);
		this.add(phoneTF);
		this.add(addressL);
		this.add(addressTF);
		
		this.add(signupB);
		this.add(backB);
	}

	private void initialize() {
		usernameL = new JLabel("Username : ");
		passwordL = new JLabel("Password : ");
		confirmPasswordL = new JLabel("Confirm Password : ");
		firstNameL = new JLabel("First Name : ");
		lastNameL = new JLabel("Last Name : ");
		emailL = new JLabel("Email : ");
		phoneL = new JLabel("Phone : ");
		addressL = new JLabel("Shipping Address : ");
		usernameTF = new JTextField();
		passwordTF = new JPasswordField();
		confirmPasswordTF = new JPasswordField();
		firstNameTF = new JTextField();
		lastNameTF = new JTextField();
		emailTF = new JTextField();
		phoneTF = new JTextField();
		addressTF = new JTextField();
		signupB = new JButton("Sign Up");
		backB = new JButton("Back");
		
		Signup s = new Signup();
		signupB.addActionListener(s);
		Back b = new Back();
		backB.addActionListener(b);
	}
	
	private class Back implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			clearTextFields();
			viewCardLayout.show(loginPanel, "1");
		}
		
	}
	
	private class Signup implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean check = checkUserData();
			if(check){
				clearTextFields();
				// TODO see if it should log in instead
				viewCardLayout.show(loginPanel, "1");
			}
		}
		
	}

	private void clearTextFields() {
		usernameTF.setText("");
		passwordTF.setText("");
		confirmPasswordTF.setText("");
		firstNameTF.setText("");
		lastNameTF.setText("");
		emailTF.setText("");
		phoneTF.setText("");
		addressTF.setText("");
	}

	public boolean checkUserData() {
		User user = new User();
		if(usernameTF.getText().length() > 0){
			try {
				String query = "Select " + Constants.USER_USERNAME
						+ " from " + Constants.USER_TABLE_NAME
						+ " where " + Constants.USER_USERNAME 
						+ " = \"" + usernameTF.getText() + "\"";
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				if(rs.next()){
					JOptionPane.showMessageDialog(null, "Username Already Exists in database !", "Error",
                            JOptionPane.ERROR_MESSAGE);
				}
				else{
					user.setUsername(usernameTF.getText());
					String password = new String(passwordTF.getPassword());
					if(password.length() > 0){
						String confirmPass = new String(confirmPasswordTF.getPassword());
						if(confirmPass.equals(password)){
							user.setPassword(password);
							user.setFirstName(firstNameTF.getText());
							user.setLastName(lastNameTF.getText());
							query = "Select " + Constants.USER_EMAIL
									+ " from " + Constants.USER_TABLE_NAME
									+ " where " + Constants.USER_EMAIL 
									+ " = \"" + emailTF.getText() + "\"";
							rs = MyDBConnection.getInstance().executeQuery(query);
							if(rs.next()){
								JOptionPane.showMessageDialog(null, "Email Already Exists in database !", "Error",
			                            JOptionPane.ERROR_MESSAGE);
							}
							else{
								user.setPhone(phoneTF.getText());
								user.setEmail(emailTF.getText());
								if(addressTF.getText().length() > 0){
									user.setShippingAddress(addressTF.getText());
									user.setAdministration(0);
									insertUsert(user);
								}
								else{
									JOptionPane.showMessageDialog(null, "There's No Address Entered !", "Error",
				                            JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "There's No Confirm Password Entered !", "Error",
		                            JOptionPane.ERROR_MESSAGE);
							
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "There's No Password Entered !", "Error",
	                            JOptionPane.ERROR_MESSAGE);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
			
		}
		else{
			
		}
		return false;
	}

	private void insertUsert(User user) {
		String query = "insert into " + Constants.USER_TABLE_NAME
				+ " values ( '" + user.getUsername()+ "', '"
				+ user.getPassword()+ "', '"
				+ user.getLastName()+ "', '"
				+ user.getFirstName()+ "', '"
				+ user.getEmail()+ "', '"
				+ user.getPhone()+ "', '"
				+ user.getShippingAddress() + "', "
				+ user.getAdministration() + ")";
		System.out.println(query);
		MyDBConnection.getInstance().executeUpdateQuery(query);
	}

}
