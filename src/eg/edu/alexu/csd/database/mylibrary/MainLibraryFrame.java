package eg.edu.alexu.csd.database.mylibrary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Report;
import eg.edu.alexu.csd.database.mylibrary.views.CustomerView;
import eg.edu.alexu.csd.database.mylibrary.views.ManagerView;

public class MainLibraryFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3874141230819609648L;
	
	private JPanel contentPane;
	private CardLayout cl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainLibraryFrame frame = new MainLibraryFrame(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param user 
	 */
	public MainLibraryFrame(User user) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
		cl = new CardLayout();
		contentPane.setLayout(cl);
		int admin = user.getAdministration();
		if(admin == 1){
			contentPane.add(new ManagerView(user,cl,contentPane,this),"Manager View");
			cl.show(contentPane, "Manager View");
			pack();
		}
		else{
			contentPane.add(new CustomerView(user,this));
			cl.show(contentPane, "Manager View");
			pack();
		}
		Report r = new Report();
		r.prepareReports(MyDBConnection.getInstance().getConnection());
	}

}
