package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import eg.edu.alexu.csd.database.mylibrary.MainLibraryFrame;
import eg.edu.alexu.csd.database.mylibrary.controller.CustomerController;
import eg.edu.alexu.csd.database.mylibrary.listeners.CustomerActions;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public class CustomerView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4900502505319510760L;
	
	private MainLibraryFrame mainLibraryFrame;
	private User user;
	private JPanel customerPanel;
	private JPanel mainPanel;
	private JPanel cartPanel;
	private JPanel settingsPanel;
	private JPanel checkoutPanel;
	private CustomerController customerContoller;
	private CustomerActions customerActions;

	public CustomerView(User user, MainLibraryFrame mainLibraryFrame) {
		
		this.mainLibraryFrame = mainLibraryFrame;
		this.add(new JButton("customer"));
		if(user == null)
			return;
		this.user = user;
		customerContoller = new CustomerController();
		customerPanel = new JPanel();
		customerPanel.setLayout(new GridBagLayout());
		customerActions = new CustomerActions(customerPanel, user, mainLibraryFrame);
		
		this.setLayout(new BorderLayout());
		this.add(customerPanel,BorderLayout.EAST);
		
		mainPanel = initializeMainView();
		cartPanel = initializeCartView();
		settingsPanel = initializeSettingsView();
		customerActions.setPanels(mainPanel, cartPanel, settingsPanel, checkoutPanel, customerContoller);
		
		customerPanel.add(mainPanel);
		
	}


	private JPanel initializeMainView() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		mainPanel.setLayout();
		
		//Info Panel
		JPanel userInfoPanel = new JPanel();
		JLabel usernameLabel = new JLabel();
		JButton logoutBtn = new JButton("Logout");
		JButton settingsBtn = new JButton("Settings");
		JButton viewCartBtn = new JButton("View Cart");
		userInfoPanel.add(usernameLabel);
		userInfoPanel.add(logoutBtn);
		userInfoPanel.add(settingsBtn);
		userInfoPanel.add(viewCartBtn);
		mainPanel.add(userInfoPanel);
		
		//Search Bar
		JPanel searchPanel = new JPanel();
		JTextField searchText = new JTextField(40);
		JComboBox<String> categoryComboBox = new JComboBox<String>();
		JButton searchBtn = new JButton("Search");
		searchPanel.add(searchText);
		searchPanel.add(categoryComboBox);
		searchPanel.add(searchBtn);
		mainPanel.add(searchPanel);
		
		//Table
		JPanel tablePanel = new JPanel(new BorderLayout());
		JTable table = new JTable();
		
		JButton addToCartBtn = new JButton("Add To Cart");
		JButton viewDetailsBtn = new JButton("View Details");
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel tableButtons = new JPanel();
		tableButtons.add(addToCartBtn);
		tableButtons.add(viewDetailsBtn);
		mainPanel.add(tablePanel);
		mainPanel.add(tableButtons);
		
		//TODO Check Out Panel
		//Check out
		JButton checkOutBtn = new JButton("Check Out");
		mainPanel.add(checkOutBtn);
		
		customerActions.setInfoPanelActions(usernameLabel, logoutBtn, settingsBtn, viewCartBtn);
		customerActions.setSearchBarActions(searchText, categoryComboBox, searchBtn, table);
		customerActions.setTableActions(table, addToCartBtn, viewDetailsBtn);
		checkoutPanel = initializeCheckoutView(checkOutBtn);
		
		return mainPanel;
		
	}
	



	private JPanel initializeCartView() {
		JPanel cartPanel = new JPanel();
		JTable ordersTable = new JTable();
		String[] s = new String[]{"Title", "Quantity"};
		ordersTable = new JTable(new DefaultTableModel(s, 0));
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
		cartPanel.add(tablePanel);
		
		JButton decreaseBtn = new JButton("Decrease");
		cartPanel.add(decreaseBtn);
		
		
		JButton applyBtn = new JButton("Apply");
		cartPanel.add(applyBtn);
		
		customerActions.setCartActions(ordersTable, decreaseBtn, applyBtn);
		
		return cartPanel;
	}

	private JPanel initializeSettingsView() {
		JPanel settingsPanel = new JPanel(new GridLayout(0,2));
		
		JLabel usernameL = new JLabel("User Name");
		JLabel usernameT = new  JLabel();
		
		JLabel passwordL = new JLabel("Password");
		JPasswordField passwordT = new JPasswordField();
		
		JLabel lNameL = new JLabel("Last Name");
		JTextField lNameT = new JTextField(10);
		
		JLabel fNameL = new JLabel("First Name");
		JTextField fNameT = new JTextField(10);
		
		JLabel emailL = new JLabel("Email");
		JTextField emailT = new JTextField(10);
		
		JLabel phoneL = new JLabel("Phone");
		JTextField phoneT = new JTextField(10);
		
		JLabel shippingAddL = new JLabel("Shipping Address");
		JTextField shippingAddT = new JTextField(10);
		
		JLabel adminL = new JLabel("Adminstration");
		JLabel adminT = new JLabel();
		if(user.getAdministration()==1) adminT.setText("Admin");
		if(user.getAdministration()==0) adminT.setText("Customer");
		
		
		JButton applyBtn = new JButton("Apply");
		JButton cancelBtn = new JButton("Cancel");
		
		
		settingsPanel.add(usernameL);
		settingsPanel.add(usernameT);
		
		settingsPanel.add(passwordL);
		settingsPanel.add(passwordT);
		
		settingsPanel.add(lNameL);
		settingsPanel.add(lNameT);
		
		settingsPanel.add(fNameL);
		settingsPanel.add(fNameT);
		
		settingsPanel.add(emailL);
		settingsPanel.add(emailT);
		
		settingsPanel.add(phoneL);
		settingsPanel.add(phoneT);
		
		settingsPanel.add(shippingAddL);
		settingsPanel.add(shippingAddT);
		
		settingsPanel.add(adminL);
		settingsPanel.add(adminT);
		
		settingsPanel.add(applyBtn);
		settingsPanel.add(cancelBtn);
		customerActions.setSettingsPanel(usernameT, passwordT, lNameT, fNameT, emailT, phoneT, shippingAddT, applyBtn, cancelBtn);
		
		return settingsPanel;
	}

//TODO
	private JPanel initializeCheckoutView(JButton checkBtn) {
		JPanel checkoutPanel = new JPanel();
		JLabel loadingLabel = new JLabel("Wait " + user.getFirstName() + " ,Chcking out ...");
//		JButton checkBtn = new JButton("Check Out");
		
		checkoutPanel.add(loadingLabel);
		
		customerActions.setCheckOutPanel(checkBtn);
		
		
		
		return checkoutPanel;
	}
	
	
	
	
}
