package eg.edu.alexu.csd.database.mylibrary.models;

import java.util.ArrayList;

public class Cart {

	private ArrayList<Item> books;
	
	public Cart() {
		books = new ArrayList<Item>();
	}
	
	public void addBook(Book book, int quantity){
		for(int i = 0; i < books.size(); i++){
			if(books.get(i).getBook().getISBN() == book.getISBN()){
				quantity += books.get(i).getQuantity();
				books.get(i).setQuantity(quantity);
				return;
			}
		}
		Item item = new Item();
		item.setBook(book);
		item.setQuantity(quantity);
		books.add(item);
	}
	
	/**
	 * 
	 * @param book
	 * @param quantity
	 * @return false if quantity more than book quantity in item 
	 * 			else return true. 
	 */
	public void removeBook(Book book){
		for(int i = 0; i < books.size(); i++){
			if(books.get(i).getBook().getISBN() == book.getISBN() ){
				if(books.get(i).getQuantity() <= 1)
				{
					books.remove(i);
				}
				else
				{
					books.get(i).setQuantity(books.get(i).getQuantity() - 1);
				}
			}
		}
	}
	public ArrayList<Item> getItems()
	{
		return books;
	}
	public void setItems(ArrayList<Item> books)
	{
		this.books = books;
	}

	public void clear()
	{
		books.clear();
	}
	
	
}
