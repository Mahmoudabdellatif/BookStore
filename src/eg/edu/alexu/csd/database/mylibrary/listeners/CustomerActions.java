package eg.edu.alexu.csd.database.mylibrary.listeners;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;
import eg.edu.alexu.csd.database.mylibrary.LoginFrame;
import eg.edu.alexu.csd.database.mylibrary.MainLibraryFrame;
import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.controller.CustomerController;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Cart;
import eg.edu.alexu.csd.database.mylibrary.models.Category;
import eg.edu.alexu.csd.database.mylibrary.models.Item;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public class CustomerActions {
	
	private MainLibraryFrame mainLibraryFrame;
	private User user;
	private HashMap<String, String> categories;
	private JPanel customerPanel;
	private JPanel mainPanel;
	private JPanel cartPanel;
	private JPanel settingsPanel;
	private JPanel checkoutPanel;
	private JPanel detailsPanel;
	private JTable cartTable;
	private JTable selectedBooksTable;
	private List<Book> selectedBooks;
	private Cart cart;
	CustomerController customerContoller;

	public CustomerActions(JPanel customerPanel, User user, MainLibraryFrame mainLibraryFrame) 
	{
		this.mainLibraryFrame = mainLibraryFrame;
		this.customerPanel = customerPanel;
		this.user = user;
		this.cart = new Cart();
		this.selectedBooks = new LinkedList<Book>();
		
	}
	
	public void setPanels(JPanel mainPanel,
			JPanel cartPanel, JPanel settingsPanel, JPanel checkoutPanel,
			CustomerController customerContoller) 
	{
		
		
		this.mainPanel = mainPanel;
		this.cartPanel = cartPanel;
		this.settingsPanel = settingsPanel;
		this.checkoutPanel = checkoutPanel;
		this.customerContoller = customerContoller;
//		detailsPanel = new JPanel();
		
		
	}
	
	

	
//Main Panel
	// TODO
	public void setInfoPanelActions(JLabel usernameLabel, JButton logoutBtn,
			JButton settingsBtn, JButton viewCartBtn) {
		
		usernameLabel.setText(user.getFirstName() + " " + user.getLastName());
		
		//Logout Button
		logoutBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{ 
				if(!cart.getItems().isEmpty())
				{
					int dialogResult = JOptionPane.showConfirmDialog (null, "There exist Items in your card, Are you sure ?!"
							,"Warning",JOptionPane.YES_NO_OPTION);
					if(dialogResult != JOptionPane.YES_OPTION)
						return;
				}
				mainLibraryFrame.setVisible(false);
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.setVisible(true);
			} });
		
		//Settings Button
		settingsBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { togglePanels(settingsPanel, mainPanel); } });
		
		//View Cart Button
		viewCartBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { togglePanels(cartPanel, mainPanel); } });
		
		
		
		
	}
	
	public void setTableActions(JTable table, JButton addToCartBtn,
			JButton viewDetailsBtn) {
		this.selectedBooksTable = table;
		
		//Add To Cart Button
		addToCartBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				int selectedBookIndex = table.getSelectedRow();
				if(selectedBookIndex == -1)//If no selected row
					JOptionPane.showMessageDialog(null, "No Selected Book !", "Error",
                            JOptionPane.ERROR_MESSAGE);
				else
				{
					Book selectedBook = selectedBooks.get(selectedBookIndex);//get selected book
					int noOfCopies = selectedBook.getNumberOfCopies();
					if(noOfCopies > 0)//make sure that no. Of copies > 0 to add the book to cart
					{
						selectedBook.setNumberOfCopies(selectedBook.getNumberOfCopies()-1);
						cart.addBook(selectedBook, 1);
						updateCartTable(cartTable, cart.getItems());
						updateTable(table);
					}
					else
						JOptionPane.showMessageDialog(null, "No. Of Copies is 0 !", "Error",
	                            JOptionPane.ERROR_MESSAGE);
				}
			} });
		
		//View Details Button
		viewDetailsBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{ 
				int selectedBookIndex = table.getSelectedRow();
				if(selectedBookIndex == -1)//If no selected row
					JOptionPane.showMessageDialog(null, "No Selected Book !", "Error",
                            JOptionPane.ERROR_MESSAGE);
				else
				{
					//Define detailsPanel
					
					detailsPanel = new JPanel();
					JPanel detailsTablePanel = new JPanel(new BorderLayout());
					JTable detailsTable = new JTable();
					String[] s = new String[]{"Key", "Value"};
					detailsTable = new JTable(new DefaultTableModel(s, 0));
					detailsTablePanel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);
					JButton backBtn = new JButton("Back");
					detailsPanel.add(detailsTablePanel);
					detailsPanel.add(backBtn);
					
					//Add Info to the details Table
					Book selectedBook = selectedBooks.get(selectedBookIndex);//get selected book
					updateDetailsTable(detailsTable, selectedBook);
					
					//Show detailsPanel
					togglePanels(detailsPanel, mainPanel);
					
					//Back Button
					backBtn.addActionListener(new ActionListener() {
						@Override public void actionPerformed(ActionEvent arg0) { togglePanels(mainPanel, detailsPanel); } });
				}
			} });
		
	}
	
	public void setSearchBarActions(JTextField searchText,
			JComboBox<String> comboBox, JButton searchBtn, JTable table) {
		
		//Adding Books Attributes to ComboBox
		if(user.getAdministration() == 1)
		{
			comboBox.addItem(Constants.BOOK_ISBN);
			comboBox.addItem(Constants.BOOK_THRESHOLD);
		}
		comboBox.addItem(Constants.BOOK_TITLE);
		comboBox.addItem(Constants.BOOK_PRICE);
		comboBox.addItem(Constants.BOOK_PUBLISH_YEAR);
		comboBox.addItem(Constants.BOOK_PUBLISHER_NAME);
		comboBox.addItem(Constants.CATEGORY_TABLE_NAME);
		
		//
		searchBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				//Make Sure That searchText is not empty
				//IF Combobox choice is "Category", Get Category Key depending on comboBox Choice 
				//ELSE choice = comboBox choice String
				//Execute query "SELECT * FROM Book WHERE <choice> = <searchText.text>"
				//Update Table
				
				String searchStr = searchText.getText();
				String combStr = (String)comboBox.getSelectedItem();
				if(searchStr == null || searchStr.equals("")) //Make Sure That searchText is not empty
				{
					JOptionPane.showMessageDialog(null, "Text Field Is Empty !!", "Error",
                            JOptionPane.ERROR_MESSAGE);
					return;
				}
				//IF Combobox choice is "Category", Get Category Key depending on comboBox Choice 
				if(combStr.equals(Constants.CATEGORY_TABLE_NAME))
				{
					combStr = Constants.BOOK_CATEGORY_KEY;
					System.out.println(searchStr);
					searchStr = getCategoryKey(searchStr);
					System.out.println(searchStr);
					if(searchStr == null)
					{
						JOptionPane.showMessageDialog(null, "Unknown Category !", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				//Execute query "SELECT * FROM Book WHERE <choice> = <searchText.text>"s
				String query = "SELECT * FROM " + Constants.BOOK_TABLE_NAME + " WHERE " + combStr + " = \"" + searchStr+"\"";
				System.out.println(query);
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				
				//Update Table
				try {
//					table.setModel(buildTableModel(rs, user.getAdministration()));
					fillBooksList(rs);
					updateTable(table);
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "No Results Found !", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
		 });
	}
	
	
	

//Settings Panel
	// TODO
	public void setSettingsPanel(JLabel usernameT, JTextField passwordT,
			JTextField lNameT, JTextField fNameT, JTextField emailT,
			JTextField phoneT, JTextField shippingAddT, JButton applyBtn, JButton cancelBtn) 
	{
		//Set Current Settings to Text Fields
		updateSettingsPanel(usernameT, passwordT, lNameT, fNameT, emailT, phoneT, shippingAddT);
		
		//Apply Button
		applyBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				
//				user.setUsername(usernameT.getText());
				user.setPassword(passwordT.getText());
				user.setLastName(lNameT.getText());
				user.setFirstName(fNameT.getText());
				user.setEmail(emailT.getText());
				user.setPhone(phoneT.getText());
				user.setShippingAddress(shippingAddT.getText());
//				user.setAdministration(Integer.parseInt(adminT.getText());
				togglePanels(mainPanel, settingsPanel);
				updateSettingsPanel(usernameT, passwordT, lNameT, fNameT, emailT, phoneT, shippingAddT);
			} });
		//Cancel Button
		cancelBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				togglePanels(mainPanel, settingsPanel);
				updateSettingsPanel(usernameT, passwordT, lNameT, fNameT, emailT, phoneT, shippingAddT);
			} });
		
	}


	
//Cart Panel
	// TODO
	public void setCartActions(JTable ordersTable, JButton decreaseBtn,
			JButton applyBtn) 
	{
		//Fill ordersTable with the orders of the current user
		this.cartTable = ordersTable;
		ArrayList<Item> cartItems = cart.getItems();
		updateCartTable(ordersTable, cartItems);
		
		//Decrease Button
		decreaseBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				//get the selected row in ordersTable
				//if Nothing is selected .. Popup Message ("Please Select a book !")
				//for the selected row decrease the ordered books by 1
				
				int selectedItemIndex = ordersTable.getSelectedRow();
				if(selectedItemIndex == -1)//If no selected row
					JOptionPane.showMessageDialog(null, "No Selected Book !", "Error",
                            JOptionPane.ERROR_MESSAGE);
				else
				{
					//get selected Item
					Item selectedItem = cartItems.get(selectedItemIndex);
					//make sure that Quantity > 0 to remove the book from cart
					int Quantity = selectedItem.getQuantity();
					cart.removeBook(selectedItem.getBook());
					increaseSelectedBook(selectedItem);
					updateCartTable(ordersTable, cart.getItems());
					updateTable(selectedBooksTable);
				}
				
			}

		});
		
		
		//Apply Button
		applyBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) 
			{
				//return
				togglePanels(mainPanel, cartPanel);
			}
		});
		

				
				
		
		
	}

	
	
//CheckOut Panel
// TODO
	public void setCheckOutPanel(JButton checkOutBtn) 
	{
		checkOutBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			
				togglePanels(checkoutPanel, mainPanel);
				
				//Check Out CODE HERE
				//<save current state before checkout process>
				//- Save user edits
				//- Save canges in book table
				//- clear JTable
				
				List<Book> oldBooks = new ArrayList<Book>();
				User oldUser = new User();
				try
				{
					//foreach Item in cart, decrease book no. Of Copies by its quantity
					for (Item item : cart.getItems()) 
					{
						//Fetch book of this item from database
						Book book = item.getBook();
						int quantity = item.getQuantity();
						
						//if(fetched no. Of Copies < quantity) error and re update the database with oldBooks
						String getBookQuery = "Select * From " + Constants.BOOK_TABLE_NAME + " Where " + Constants.BOOK_ISBN
								+ " = " + book.getISBN();
						ResultSet rs = MyDBConnection.getInstance().executeQuery(getBookQuery);
						rs.next();
						Book fetched = StaticMethods.setBookInfo(rs);
						if(fetched.getNumberOfCopies() < quantity)
						{
							throw new SQLException();
						}
						else
						{
							//else, Add fetched to oldBooks and update the database 
							oldBooks.add(fetched);
//							String updateBookQuery = "Update "+ Constants.BOOK_TABLE_NAME 
//									+ " Set " + Constants.BOOK_COPIES_NUMBER + " = " + (fetched.getNumberOfCopies() - quantity)
//									+ " Where " + Constants.BOOK_ISBN + " = " + book.getISBN();
//							 if(!MyDBConnection.getInstance().executeUpdateQuery(updateBookQuery)) throw new SQLException();
						}
					}
					
					for (Item item : cart.getItems()) 
					{
						//Fetch book of this item from database
						Book book = item.getBook();
						int quantity = item.getQuantity();
						
						Date date = new Date();
						LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						int year = localDate.getYear();
						int month = localDate.getMonth().getValue();
						int day = localDate.getDayOfMonth();
						String dateStr = year + "-";
						if(month < 10) dateStr += "0";
						dateStr += month + "-";
						if(day < 10) dateStr += "0";
						dateStr += day;
						//(`ISBN` INT, `quantity` INT, `username` VARCHAR(40), `date` DATE) RETURNS tinyint(1)
						String query = "Select buy_book (" + book.getISBN() + ", " + quantity
								+ ", '" + user.getUsername() + "', '" + dateStr + "')";
						MyDBConnection.getInstance().executeQuery(query);
					}
					cart.clear();
					updateCartTable(cartTable, cart.getItems());
					
					
					//read current user from database, then upload the new changes
					String getUserQuery = "Select * From " + Constants.USER_TABLE_NAME + " Where " + Constants.USER_USERNAME
							+ " = '" + user.getUsername() + "'";
					ResultSet rs = MyDBConnection.getInstance().executeQuery(getUserQuery);
					if(rs.next())
					{
						//save fetched user to oldUser
						oldUser = StaticMethods.setUserInfo(rs);
						//update database with current user
						StaticMethods.updateUserInDatabase(user);
						
					}
					
				}
				catch(SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Cannot Checkout. No Enough Book Copies !", "Error",
                            JOptionPane.ERROR_MESSAGE);
					
				}
				
				
				togglePanels(mainPanel, checkoutPanel);
			}
		});
		
	}
	



	

//Utility Methods
//TODO
	
	private void togglePanels(JPanel panelToShow, JPanel panelToHide)
	{
		customerPanel.setVisible(false);
		customerPanel.remove(panelToHide);
		customerPanel.add(panelToShow);
//		mainLibraryFrame.setSize(customerPanel.getSize());
		customerPanel.setVisible(true);
		mainLibraryFrame.pack();
		
	}
	
	private void updateSettingsPanel(JLabel usernameT, JTextField passwordT,
			JTextField lNameT, JTextField fNameT, JTextField emailT,
			JTextField phoneT, JTextField shippingAddT)
	{
		usernameT.setText(user.getUsername());
		passwordT.setText(user.getPassword());
		lNameT.setText(user.getLastName());
		fNameT.setText(user.getFirstName());
		emailT.setText(user.getEmail());
		phoneT.setText(user.getPhone());
		shippingAddT.setText(user.getShippingAddress());
	}
	
	private void updateDetailsTable(JTable detailsTable,
			Book book) 
	{
		//Reading Book Data
		long isbn = book.getISBN();
		String title = book.getTitle();
		String pulisher = book.getPublisher();
		String category = getCategoryName(book.getCategoryKey());
		int noOfCopies = book.getNumberOfCopies();
		int price = book.getPrice();
		int pYear = book.getpYear();
		int threshold = book.getThreshold();
		ArrayList<String> authors = book.getAuthors();
		System.out.println(authors.size());
				
		//Adding Book Data to Table
		DefaultTableModel tableModel = (DefaultTableModel) detailsTable.getModel();
		tableModel.setRowCount(0);
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Key");
		columnNames.add("Value");

		if(user.getAdministration() == 1) addDetailsRow("ISBN", isbn, tableModel);
		addDetailsRow("Title", title, tableModel);
		addDetailsRow("Publisher", pulisher, tableModel);
		addDetailsRow("Category", category, tableModel);
		addDetailsRow("No. Of Copies", noOfCopies, tableModel);
		addDetailsRow("Price", price, tableModel);
		addDetailsRow("Publish Year", pYear, tableModel);
		addDetailsRow("Threshold", threshold, tableModel);
		addDetailsRow("Authors", "", tableModel);
		for (String string : authors) {			
			addDetailsRow("", string, tableModel);
		}
		
		tableModel.fireTableDataChanged();
		detailsTable.setModel(tableModel);
		
		
	}
	
	private void addDetailsRow(String name, Object value, DefaultTableModel tableModel)
	{
		Vector<Object> row = new Vector<Object>();
		 row.add(name);
		 row.add(value);
		 tableModel.addRow(row);
	}
	
	private void updateCartTable(JTable table, ArrayList<Item> cartItems)
	{
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
//		Vector<String> columnNames = new Vector<String>();
//		columnNames.add("Book Title");
//		columnNames.add("Quantity");
		
		for (Item item : cartItems) {
			 Vector<Object> row = new Vector<Object>();
			 row.add(item.getBook().getTitle());
			 row.add(item.getQuantity());
			 tableModel.addRow(row);
		}
		
		tableModel.fireTableDataChanged();
		table.setModel(tableModel);
	}
	
	private void fillBooksList(ResultSet rs) throws SQLException {
		selectedBooks.clear();
		selectedBooks = StaticMethods.fillBooksInfo(rs);
	}

	private void updateTable(JTable table)
	{
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
		
		//Table Columns Names
		Vector<String> columnNames = new Vector<String>();
		 if(user.getAdministration() == 1)
			 columnNames.add("ISBN");
		 columnNames.add("Title");
		 columnNames.add("Price");
		 columnNames.add("Publiser");
		 columnNames.add("Number Of Copies");
		 columnNames.add("Category");
		 tableModel.setColumnIdentifiers(columnNames);
		 
		 
		for (Book book : selectedBooks) {
			 Vector<Object> row = new Vector<Object>();
			 if(user.getAdministration() == 1)
				row.add(book.getISBN());
			 row.add(book.getTitle());
			 row.add(book.getPrice());
			 row.add(book.getPublisher());
			 row.add(book.getNumberOfCopies());
			 String category = getCategoryName(book.getCategoryKey());
			 row.add(category);
			 tableModel.addRow(row);
		}
		
		tableModel.fireTableDataChanged();
		table.setModel(tableModel);
	}
	
	private void increaseSelectedBook(Item selectedItem) 
	{
		Book selectedBook = selectedItem.getBook();
		selectedBook.setNumberOfCopies(selectedBook.getNumberOfCopies()+1);
		for (Book book : selectedBooks) 
		{
			if(book.getISBN() == selectedBook.getISBN())
			{
				book = selectedBook;
				return;
			}
		}
		selectedBooks.add(selectedBook);
	}
	
	private void initalizeCategoryMap()
	{
		List<Category> cats = StaticMethods.getCategories();
		if(categories == null) categories = new HashMap<String, String>();
		for (Category category : cats) {
			 categories.put(category.getKey(), category.getName());
		}
	}
	
	private String getCategoryName(String key)
	{
		//Define hash map with int key and string value and if not null, get key given value.
		if(categories == null)
		{
			initalizeCategoryMap();
		}
		
		return categories.get(key);
		
	}
	
	private String getCategoryKey(String name)
	{
		//Define hash map with int key and string value and if not null, get key given value.
		if(categories == null)
		{
			initalizeCategoryMap();
		}
		for (String key : categories.keySet()) {
			if(categories.get(key).equals(name))
				return key;
		}
		return null;
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs, int admin)
	        throws SQLException 
	{

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    int column = 2;
	    if(admin == 1) column = 1;
	    for (column = admin; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }
	    
	    return new DefaultTableModel(data, columnNames);

	}

	

}