package eg.edu.alexu.csd.database.mylibrary;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import eg.edu.alexu.csd.database.mylibrary.listeners.LoginActionListener;
import eg.edu.alexu.csd.database.mylibrary.views.SignUpView;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6989641139540986569L;
	
	private JPanel contentPane, loginPanel, SignupPanel;
	private JPasswordField passwordField;
	private JTextField textField;
	private CardLayout cl;
	private static LoginFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("CSED Library");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		cl = new CardLayout();
		contentPane.setLayout(cl);
		
		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(102, 133, 84, 14);
		loginPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(102, 172, 84, 14);
		loginPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(196, 169, 189, 20);
		loginPanel.add(passwordField);
		
		textField = new JTextField("");
		textField.setBounds(196, 130, 189, 20);
		loginPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLogin.setBounds(263, 228, 89, 30);
		loginPanel.add(btnLogin);
		
		JButton btnSignup = new JButton("SignUp");
		btnSignup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSignup.setBounds(126, 228, 89, 30);
		loginPanel.add(btnSignup);
		
		LoginActionListener la = new LoginActionListener(this);
		btnLogin.addActionListener(la);
		
		SignUp su = new SignUp();
		btnSignup.addActionListener(su);
		
		SignupPanel = new SignUpView(cl, contentPane);
		contentPane.add(loginPanel, "1");
		contentPane.add(SignupPanel, "2");
	}
	public String getUsername()
	{
		return textField.getText();
	}
	public String getPassword()
	{
		return new String(passwordField.getPassword());
	}
	private class SignUp implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			cl.show(contentPane, "2");
		}
		
	}
	
}
