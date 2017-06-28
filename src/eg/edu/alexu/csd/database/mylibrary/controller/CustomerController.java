package eg.edu.alexu.csd.database.mylibrary.controller;

import eg.edu.alexu.csd.database.mylibrary.interfaces.IUser;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Cart;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public class CustomerController implements IUser{
	
	@Override
	public void editPersonalData(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBookToCart(Book book, Cart cart, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewCart(Cart cart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromCart(Cart cart, Book book, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkOut(Cart cart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

}
