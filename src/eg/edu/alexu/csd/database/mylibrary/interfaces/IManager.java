package eg.edu.alexu.csd.database.mylibrary.interfaces;

import java.sql.ResultSet;

import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Order;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public interface IManager {
	
	/**
	 * Get the new book you want to add and send it to the data base
	 * @param book
	 */
	public void addNewBook(Book book);
	
	/**
	 * Takes the book after its attributes are updated and update
	 * this book in the database
	 * @param updatedBook The book after update to be written in dataBase
	 */
	public void updateBook(Book updatedBook);
	
	/**
	 * Manager can add an order manually.
	 * @param book
	 * @param quantity
	 */
	public void placeOrder(Order order);
	
	/**
	 * Manager confirm that the order was received.
	 * @param order
	 */
	public void confirmOrder(Order order);
	
	/**
	 * Manager remove an order that is no longer need, 
	 *  so this order will not be received.
	 * @param order
	 */
	public void rejectOrder(Order order);
	
	/**
	 * Manager can promote a customer to a manager.
	 * @param user
	 */
	public void promoteCustomer(User user);
	
	/**
	 * 
	 * @return result set contains ISBN, title, quantity of sells
	 * 			, Total selling price of book (at least)
	 */
	public ResultSet viewTotalSales();
	
	/**
	 * 
	 * @return result set contains user attribute number of purchases.
	 */
	public ResultSet viewTopFiveCustomers();
	
	/**
	 * 
	 * @return result set contains book attributes and quantity of sells.
	 */
	public ResultSet viewTopTenSellingBooks();
}
