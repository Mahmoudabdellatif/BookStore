package eg.edu.alexu.csd.database.mylibrary.utils;

public class Constants {
	
	//Author table column names in DB
	public static String AUTHOR_TABLE_NAME = "Author"; 
	public static String AUTHOR_NAME = "A_name";

	//Book table column names in DB
	public static String BOOK_TABLE_NAME = "Book";
	public static String BOOK_ISBN = "ISBN";
	public static String BOOK_TITLE = "title";
	public static String BOOK_PUBLISHER_NAME = "publisher_name";
	public static String BOOK_PUBLISH_YEAR = "p_year";
	public static String BOOK_PRICE = "price";
	public static String BOOK_CATEGORY_KEY = "category_key";
	public static String BOOK_COPIES_NUMBER = "no_of_copies";
	public static String BOOK_THRESHOLD = "threshold";
	public static int BOOK_ATTRIBUTE_COUNT = 8;
	
	//Order table column names in DB
	public static String ORDER_TABLE_NAME = "book_order";
	public static String ORDER_NUMBER = "order_no";
	public static String ORDER_ISBN = "ISBN";
	public static String ORDER_QUANTITY = "quantity";
	public static String ORDER_ACCEPTANCE = "acceptance";
	public static int ORDER_ATTRIBUTE_COUNT = 4;
	
	//User table column names in DB
	public static String USER_TABLE_NAME = "User";
	public static String USER_USERNAME = "user_name";
	public static String USER_PASSWORD = "password";
	public static String USER_LAST_NAME = "L_name";
	public static String USER_FIRST_NAME = "F_name";
	public static String USER_EMAIL = "email";
	public static String USER_PHONE = "phone_num";
	public static String USER_SHIPPING_ADDRESS = "shipping_address";
	public static String USER_ADMINISTRATION = "adminstration";
	public static String USER_PURCHASES_COUNT = "total";
	public static int USER_ATTRIBUTE_COUNT = 8;
	
	//Category table column names in DB
	public static String CATEGORY_TABLE_NAME = "Category";
	public static String CATEGORY_NAME = "c_name";
	public static String CATEGORY_KEY = "c_key";
	public static int CATEGORY_ATTRIBUTE_COUNT = 2;
	
	//Publisher table column names in DB
	public static String PUBLISHER_TABLE_NAME = "publisher";
	public static String PUBLISHER_NAME = "p_name";
	public static String PUBLISHER_ADDRESS = "address";
	public static String PUBLISHER_TELEPHONE = "tel_no";
	public static int PUBLISHER_ATTRIBUTE_COUNT = 3;
	
	//Manager Button Names
	public static String MANAGER_ADD_PUBLISHER = "Add Publisher";
	public static String MANAGER_ADD_BOOK = "Add new book";
	public static String MANAGER_UPDATE_BOOK = "Update book data";
	public static String MANAGER_MAKE_ORDER = "Make an Order";
	public static String MANAGER_ACCEPT_ORDER = "Confirm Order";
	public static String MANAGER_REJECT_ORDER = "Reject Order";
	public static String MANAGER_VIEW_ORDERS = "View Orders";
	public static String MANAGER_PROMOTE_CUSTOMER = "Promote Customer";
	public static String MANAGER_TOP_SALES = "Top Sales";
	public static String MANAGER_TOP_CUSTOMERS = "Top 5 Customers";
	public static String MANAGER_TOP_BOOKS = "Top 10 Books";
	
}
