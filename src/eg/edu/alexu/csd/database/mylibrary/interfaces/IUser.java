package eg.edu.alexu.csd.database.mylibrary.interfaces;

import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Cart;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public interface IUser {
		
	/**
	 * Allow user to change any of his personal data.
	 * @param user
	 */
	public void editPersonalData(User user);
	
	/**
	 * Add a book to my cart for later using.
	 * @param book
	 * @param cart
	 * @param quantity number of book copies he want to add. 
	 * 		  (Normally that number is 1)
	 * For our UI there is a button associated with each book in the view
	 * to add the book to cart.
	 */
	public void addBookToCart(Book book, Cart cart, int quantity);
	
	/**
	 * The user can see the books which he had add in the cart
	 * & price of each book and total price of books.
	 * @param cart
	 */
	public void viewCart(Cart cart);
	
	/**
	 * During the view of cart the user can remove a certain book
	 * @param cart
	 * @param book
	 * @param quantity number of book copies he want to remove. 
	 * 		  (Normally that number is should be less than number he has)
	 * For our UI there is a button associated with each book in the view
	 * to remove the book from cart.
	 */
	public void removeFromCart(Cart cart, Book book,int quantity);
	
	/**
	 * Checking out user and register his purchases and check his credit 
	 * if there is enough then commit this purchase.
	 * @param cart
	 */
	public void checkOut(Cart cart);
	
	/**
	 * logout the current user and remove any unchecked out books from his cart.
	 */
	public void logout();
}
