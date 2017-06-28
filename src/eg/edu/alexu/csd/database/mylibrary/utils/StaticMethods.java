package eg.edu.alexu.csd.database.mylibrary.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Cart;
import eg.edu.alexu.csd.database.mylibrary.models.Category;
import eg.edu.alexu.csd.database.mylibrary.models.Item;
import eg.edu.alexu.csd.database.mylibrary.models.User;

public class StaticMethods {
	
	public static ArrayList<Category> getCategories(){
		ArrayList<Category> categories = new ArrayList<Category>();
		String query = "Select * from " + Constants.CATEGORY_TABLE_NAME;
		ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
		try {
			while(rs.next()){
				Category c = new Category();
				c.setName(rs.getString(Constants.CATEGORY_NAME));
				c.setKey(rs.getString(Constants.CATEGORY_KEY));
				categories.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	public static Book setBookInfo(ResultSet rs){
		Book book = new Book();
		try {
			book.setISBN(rs.getLong(Constants.BOOK_ISBN));
			System.out.println(rs.getLong(Constants.BOOK_ISBN)+ "   1hhhhh");
			book.setTitle(rs.getString(Constants.BOOK_TITLE));
			book.setPublisher(rs.getString(Constants.BOOK_PUBLISHER_NAME));
			book.setCategoryKey(rs.getString(Constants.BOOK_CATEGORY_KEY));
			book.setNumberOfCopies(rs.getInt(Constants.BOOK_COPIES_NUMBER));
			book.setPrice(rs.getInt(Constants.BOOK_PRICE));
			book.setpYear(rs.getInt(Constants.BOOK_PUBLISH_YEAR));
			book.setThreshold(rs.getInt(Constants.BOOK_THRESHOLD));
			String query = "SELECT * FROM " + Constants.AUTHOR_TABLE_NAME 
					+ " WHERE " +Constants.BOOK_ISBN + " = " + book.getISBN();
			try{
				rs = MyDBConnection.getInstance().executeQuery(query);
				while(rs.next()){
					book.addAuthor(rs.getString(Constants.AUTHOR_NAME));
				}
			}catch (Exception e) {}
			return book;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
	}
	
	public static ArrayList<Book> fillBooksInfo(ResultSet rs){
		ArrayList<Book> selectedBooks = new ArrayList<Book>();
		try {
			while (rs.next()) 
			{
				Book book = new Book();
				book.setISBN(rs.getLong(Constants.BOOK_ISBN));
				book.setTitle(rs.getString(Constants.BOOK_TITLE));
				book.setPublisher(Constants.BOOK_PUBLISHER_NAME);
				book.setCategoryKey(rs.getString(Constants.BOOK_CATEGORY_KEY));
				book.setNumberOfCopies(rs.getInt(Constants.BOOK_COPIES_NUMBER));
				book.setPrice(rs.getInt(Constants.BOOK_PRICE));
				book.setpYear(rs.getInt(Constants.BOOK_PUBLISH_YEAR));
				book.setThreshold(rs.getInt(Constants.BOOK_THRESHOLD));
				selectedBooks.add(book);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for (Book book : selectedBooks) {
			String query = "SELECT * FROM " + Constants.AUTHOR_TABLE_NAME 
					+ " WHERE " + Constants.BOOK_ISBN + " = " + book.getISBN();
			try{
				rs = MyDBConnection.getInstance().executeQuery(query);
				while(rs.next()){
					book.addAuthor(rs.getString(Constants.AUTHOR_NAME));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return selectedBooks;
	}

	
	public static User setUserInfo(ResultSet rs){
		User user = new User();
		try {
			user.setUsername(rs.getString(Constants.USER_USERNAME));
			user.setPassword(rs.getString(Constants.USER_PASSWORD));
			user.setLastName(rs.getString(Constants.USER_LAST_NAME));
			user.setFirstName(rs.getString(Constants.USER_FIRST_NAME));
			user.setEmail(rs.getString(Constants.USER_EMAIL));
			user.setPhone(rs.getString(Constants.USER_PHONE));
			user.setShippingAddress(rs.getString(Constants.USER_SHIPPING_ADDRESS));
			user.setAdministration(rs.getInt(Constants.USER_ADMINISTRATION));
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void updateUserInDatabase(User user) throws SQLException{
		boolean admin = false;
		if(user.getAdministration() == 1) admin = true;
		String setString = Constants.USER_USERNAME + " = '" + user.getUsername() + "', "
				+ Constants.USER_PASSWORD + " = '" + user.getPassword() + "', "
				+ Constants.USER_LAST_NAME + " = '" + user.getLastName() + "', "
				+ Constants.USER_FIRST_NAME + " = '" + user.getFirstName() + "', "
				+ Constants.USER_EMAIL + " = '" + user.getEmail() + "', "
				+ Constants.USER_PHONE + " = '" + user.getPhone() + "', "
				+ Constants.USER_SHIPPING_ADDRESS + " = '" + user.getShippingAddress() + "', "
				+ Constants.USER_ADMINISTRATION + " = " + user.getAdministration() + "";
		String updateQuery = "Update "+ Constants.USER_TABLE_NAME
				+ " Set " + setString
				+ " Where " + Constants.USER_USERNAME + " = '" + user.getUsername() + "'";
		System.out.println(updateQuery);
		MyDBConnection.getInstance().executeUpdateQuery(updateQuery);
	}
	
	
	//Utility For Testing
	public static Book getRandomBook()
	{
		Book book = new Book();
		Random r = new Random();
		int ri = r.nextInt(10)+1;
		book.setISBN(r.nextInt(1000000));
		book.setTitle("Title " + ri);
		book.setPublisher("publisher " + ri);
		book.setCategoryKey("cat " + ri);
		book.setNumberOfCopies(ri);
		book.setPrice(ri);
		book.setpYear(ri);
		book.setThreshold(ri);
		book.addAuthor("Author 1 of " + ri);
		book.addAuthor("Author 2 of " + ri);
		return book;
	}
	
	public static Item getRandomItem()
	{
		Random r = new Random();
		Item item = new Item();
		Book book = getRandomBook();
		item.setBook(book);
		item.setQuantity(r.nextInt(book.getNumberOfCopies()));
		
		return item;
	}
	
	public static Cart getRandomCart(int size)
	{
		Random r = new Random();
		Cart cart = new Cart();
		for(int i = 0; i < size; i++)
		{
			Book book = getRandomBook();
			cart.addBook(book, r.nextInt(book.getNumberOfCopies()+1));
		}
		return cart;
		
		
	}
}
