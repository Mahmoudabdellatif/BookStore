package eg.edu.alexu.csd.database.mylibrary.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import eg.edu.alexu.csd.database.mylibrary.LoginFrame;
import eg.edu.alexu.csd.database.mylibrary.MainLibraryFrame;
import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;

public class LoginActionListener implements ActionListener{
	
	private String username, password;
	private LoginFrame loginFrame;
	
	public LoginActionListener(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.username = loginFrame.getUsername();
		this.password = loginFrame.getPassword();
		String query = "Select * from "
				+ Constants.USER_TABLE_NAME
				+ " where "
				+ Constants.USER_USERNAME + " = \"" + username + "\"";
		System.out.println(query);
		ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
		try {
			if(rs.next()){
				String pass = rs.getString(Constants.USER_PASSWORD);
				if(pass.equals(password)){
					User user = StaticMethods.setUserInfo(rs);
					@SuppressWarnings("unused")
					MainLibraryFrame mainLibraryFrame = new MainLibraryFrame(user);
					loginFrame.setVisible(false);
				}
				else{
					// TODO show error message for wrong password.
					JOptionPane.showMessageDialog(null,"Wrong password","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"Wrong Username","Warning",JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	/*	
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setLastName("Elsayed");
		user.setFirstName("Mohamed");
		user.setEmail("Mohamed@gmail.com");
		user.setPhone("0123456789");
		user.setShippingAddress("Hamada");
		user.setAdministration(1);
		MainLibraryFrame mainLibraryFrame = new MainLibraryFrame(user);
		loginFrame.setVisible(false);*/
	}

}
