package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eg.edu.alexu.csd.database.mylibrary.MainLibraryFrame;
import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.controller.ManagerContoller;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Order;
import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;

public class ManagerView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2118227822835057171L;

	private JPanel customerPanel, managerPanel;
	private ArrayList<JButton> managerButtons;
	private ManagerContoller managerContoller;
	private ManagerActionListener managerActionListener;
	private String[] managerActionsText;
	private CardLayout mainLibraryCardLayout;
	
	private JPanel mainLibraryPanel;
	private AddPublisherView addPublisherView;
	private AddNewBookView addNewBookView;
	private UpdateBookView updateBookView;
	private OrderView orderView;
	private TotalSales topSales;
	private TopCustomers topCustomers;
	private TopBooks topBooks;

	public ManagerView(User user, CardLayout cl, JPanel contentPane, MainLibraryFrame mainLibraryFrame) {
		managerContoller = new ManagerContoller();
		customerPanel = new CustomerView(user, mainLibraryFrame);
		managerPanel = new JPanel();
		this.mainLibraryCardLayout = cl;
		this.setLayout(new BorderLayout());
		this.mainLibraryPanel = contentPane;
		this.add(managerPanel, BorderLayout.WEST);
		this.add(customerPanel,BorderLayout.EAST);
		
		addPublisherView = new AddPublisherView(mainLibraryCardLayout, mainLibraryPanel);
		addNewBookView = new AddNewBookView(managerContoller
				, mainLibraryCardLayout, mainLibraryPanel);
		updateBookView = new UpdateBookView(managerContoller
				, mainLibraryCardLayout,mainLibraryPanel);
		orderView = new OrderView(mainLibraryPanel, mainLibraryCardLayout);
		topSales = new TotalSales(managerContoller
				, mainLibraryCardLayout, mainLibraryPanel);
		topCustomers = new TopCustomers(managerContoller
				, mainLibraryCardLayout, mainLibraryPanel);
		topBooks = new TopBooks(managerContoller
				, mainLibraryCardLayout, mainLibraryPanel);
		mainLibraryPanel.add(addPublisherView, Constants.MANAGER_ADD_PUBLISHER);
		mainLibraryPanel.add(addNewBookView, Constants.MANAGER_ADD_BOOK);
		mainLibraryPanel.add(updateBookView	, Constants.MANAGER_UPDATE_BOOK);
		mainLibraryPanel.add(orderView, Constants.MANAGER_VIEW_ORDERS);
		mainLibraryPanel.add(topSales, Constants.MANAGER_TOP_SALES);
		mainLibraryPanel.add(topCustomers, Constants.MANAGER_TOP_CUSTOMERS);
		mainLibraryPanel.add(topBooks, Constants.MANAGER_TOP_BOOKS);
		
		initializeManagerButtons();

	}

	private void initializeManagerButtons() {
		managerButtons = new ArrayList<JButton>();
		managerActionListener = new ManagerActionListener();
		managerActionsText = new String[] {
				Constants.MANAGER_ADD_PUBLISHER,
				Constants.MANAGER_ADD_BOOK, 
				Constants.MANAGER_UPDATE_BOOK,
				Constants.MANAGER_MAKE_ORDER,
				Constants.MANAGER_VIEW_ORDERS,
				Constants.MANAGER_ACCEPT_ORDER,
				Constants.MANAGER_REJECT_ORDER,
				Constants.MANAGER_PROMOTE_CUSTOMER,
				Constants.MANAGER_TOP_SALES,
				Constants.MANAGER_TOP_CUSTOMERS,
				Constants.MANAGER_TOP_BOOKS };
		managerPanel.setLayout(new GridLayout(managerActionsText.length, 1));
		for (int i = 0; i < managerActionsText.length; i++) {
			JButton button = new JButton(managerActionsText[i]);
			button.addActionListener(managerActionListener);
			managerButtons.add(button);
			managerPanel.add(managerButtons.get(i));
		}
	}

	private class ManagerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonString = getButtonString(e);
			switch (buttonString) {
			case "Add Publisher":
				addPublisher();
				break;
			case "Add new book":
				addNewBook();
				break;
			case "Update book data":
				updateBook();
				break;
			case "Make an Order":
				makeOrder();
				break;
			case "Confirm Order":
				acceptOrder();
				break;
			case "Reject Order":
				rejectOrder();
				break;
			case "View Orders":
				viewOrders();
				break;
			case "Promote Customer":
				promoteCustomer();
				break;
			case "Top Sales":
				topSales();
				break;
			case "Top 5 Customers":
				topCustomers();
				break;
			case "Top 10 Books":
				topBooks();
				break;
			default:
				break;
			}
		}

		private String getButtonString(ActionEvent e) {
			for (int i = 0; i < managerButtons.size(); i++) {
				if (e.getSource() == managerButtons.get(i)) {
					return managerButtons.get(i).getText();
				}
			}
			return null;
		}

	}
	
	private void addNewBook() {
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_ADD_BOOK);
	}

	private void addPublisher() {
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_ADD_PUBLISHER);
	}

	private void updateBook() {
		try{
			long ISBN = Long.parseLong(JOptionPane.showInputDialog("Enter Book ISBN :",""));
			String query = "select * from "	+ Constants.BOOK_TABLE_NAME
					+ " where " + Constants.BOOK_ISBN + " = " + ISBN;
			try {
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				if(rs.next()){
					Book book = StaticMethods.setBookInfo(rs);
					if(book != null){
						System.out.println(book.getISBN()+ "  2hhhhh");
						this.updateBookView.setBook(book);
						mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_UPDATE_BOOK);
					}
					else{
						JOptionPane.showMessageDialog(null,"Unexpected error on server\n"
								+ "Please try again","Warning",JOptionPane.WARNING_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"There is no book with this ISBN","Warning",JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"Enter Numbers only next time","Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void makeOrder() {
		try {
			long ISBN = Long.parseLong(JOptionPane.showInputDialog("Enter Book ISBN :",""));
			String query = "select * from "	+ Constants.BOOK_TABLE_NAME
					+ " where " + Constants.BOOK_ISBN + " = " + ISBN;
			ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
			try {
				if(rs.next()){
					try {
						int quantity = Integer.parseInt(
								JOptionPane.showInputDialog("Enter Book quantity :",""));
						Order order = new Order();
						order.setISBN(ISBN);
						order.setAccepted(1);
						order.setQuantity(quantity);
						managerContoller.placeOrder(order);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,"Order was not placed","Warning",JOptionPane.WARNING_MESSAGE);
					}
					
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Enter Numbers only next time","Warning",JOptionPane.WARNING_MESSAGE);
		}	
	}
	
	private void acceptOrder() {
		try {
			int orderNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Order Number :",""));
			String query = "select * from "	+ Constants.ORDER_TABLE_NAME
					+ " where " + Constants.ORDER_NUMBER + " = " + orderNumber;
			try {
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				if(rs.next()){
					Order order = new Order();
					order.setISBN(rs.getLong(Constants.ORDER_ISBN));
					order.setOrderNumber(rs.getInt(Constants.ORDER_NUMBER));
					order.setAccepted(1);
					order.setQuantity(rs.getInt(Constants.ORDER_QUANTITY));
					managerContoller.confirmOrder(order);					
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Enter Numbers only next time","Warning",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	private void rejectOrder() {
		try {
			int orderNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Order Number :",""));
			String query = "select * from "	+ Constants.ORDER_TABLE_NAME
					+ " where " + Constants.ORDER_NUMBER + " = " + orderNumber;
			try {
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				if(rs.next()){
					Order order = new Order();
					order.setOrderNumber(rs.getInt(Constants.ORDER_NUMBER));
					order.setISBN(rs.getLong(Constants.ORDER_ISBN));
					order.setAccepted(1);
					order.setQuantity(rs.getInt(Constants.ORDER_QUANTITY));
					managerContoller.rejectOrder(order);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Enter Numbers only next time","Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void viewOrders() {
		orderView.showOrders();
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_VIEW_ORDERS);
	}
	
	private void promoteCustomer() {
		try {
			String username = JOptionPane.showInputDialog("Enter user name you want to promote :","");
			String query = "select * from "	+ Constants.USER_TABLE_NAME
					+ " where " + Constants.USER_USERNAME + " = '" + username+ "'";
			try {
				ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
				if(rs.next()){
					User user = StaticMethods.setUserInfo(rs);
					managerContoller.promoteCustomer(user);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Enter Numbers only next time","Warning",JOptionPane.WARNING_MESSAGE);
		}		
	}
	
	private void topSales() {
		topSales.ShowUI();
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_TOP_SALES);		
	}
	
	private void topCustomers() {
		topCustomers.ShowUI();
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_TOP_CUSTOMERS);
		
	}

	private void topBooks() {
		topBooks.ShowUI();
		mainLibraryCardLayout.show(mainLibraryPanel, Constants.MANAGER_TOP_BOOKS);
		
	}

}
