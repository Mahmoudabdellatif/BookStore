package eg.edu.alexu.csd.database.mylibrary.controller;

import java.sql.ResultSet;
import java.util.ArrayList;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.interfaces.IManager;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Order;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public class ManagerContoller implements IManager {
	
	//TODO Mahmoud & Shady will call there functions here

	@Override
	public void addNewBook(Book book) {
		
		MyDBConnection.getInstance().executeQuery(""
				+ "SELECT add_book( '"+ book.getTitle() +
				"', '" + book.getPublisher() + 
				"', " + book.getpYear() + 
				", " + book.getPrice() + 
				", '" + book.getCategoryKey() +
				"', " + book.getNumberOfCopies() + 
				", " + book.getThreshold() + ")");
		ArrayList<String> auth = book.getAuthors();
		for(String author : auth){
			MyDBConnection.getInstance().executeQuery(""
					+ "SELECT add_author( '"+ author + "' )");
		}
		
	}

	@Override
	public void updateBook(Book book) {
		// TODO Auto-generated method stub
		String q = ""
				+ "UPDATE book SET"+  
				" title =  '" + book.getTitle() +
				"', publisher_name =  '" + book.getPublisher() + 
				"', p_year =  " + book.getpYear() + 
				", price = " + book.getPrice() + 
				", category_key =  '" + book.getCategoryKey() +
				"', no_of_copies =  " + book.getNumberOfCopies() + 
				", threshold = " + book.getThreshold()  + " WHERE book.ISBN = " + book.getISBN();
		
		MyDBConnection.getInstance().executeUpdateQuery(q);
		
		ArrayList<String> auth = book.getAuthors();
		MyDBConnection.getInstance().executeUpdateQuery("Delete from author where ISBN = "+book.getISBN());
		for(String author : auth){
			System.out.println("heyy " + author);
			MyDBConnection.getInstance().executeQuery(""
					+ "SELECT add_author( '"+ author + "' )");
			
		}
	}

	@Override
	public void placeOrder(Order order) {
		// TODO Auto-generated method stub
		MyDBConnection.getInstance().executeUpdateQuery(""
				+ "INSERT INTO book_order (ISBN, quantity) VALUES(" + 
				order.getISBN()+ ", " + order.getQuantity()
				+ ")");
	}

	@Override
	public void confirmOrder(Order order) {
		// TODO Auto-generated method stub
		MyDBConnection.getInstance().executeUpdateQuery(
				"DELETE FROM book_order WHERE order_no = " + order.getOrderNumber());
	}

	@Override
	public void rejectOrder(Order order) {
		this.confirmOrder(order);
		
	}

	@Override
	public void promoteCustomer(User user) {
		// TODO Auto-generated method stub
		MyDBConnection.getInstance().executeUpdateQuery(
				"UPDATE user SET adminstration = 1 "
				+ "WHERE user_name = '" + user.getUsername() + "'");
	}

	@Override
	public ResultSet viewTotalSales() {
		// TODO Auto-generated method stub
		
		return MyDBConnection.getInstance().executeQuery("CALL total_sales()");
		
		
	}

	@Override
	public ResultSet viewTopFiveCustomers() {
		// TODO Auto-generated method stub
		return MyDBConnection.getInstance().executeQuery("CALL top_5_customers()");
		
	}

	@Override
	public ResultSet viewTopTenSellingBooks() {
		// TODO Auto-generated method stub
		return MyDBConnection.getInstance().executeQuery("CALL top_10_selling_books()");
		
	}

}
