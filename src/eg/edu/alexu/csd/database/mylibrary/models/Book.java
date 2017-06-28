package eg.edu.alexu.csd.database.mylibrary.models;

import java.util.ArrayList;

public class Book {
	
	private long ISBN;
	private int pYear, price, numberOfCopies, threshold, numberOfPurchases;
	private String title, publisher, categoryKey;
	private ArrayList<String> authors;
	
	public Book() {
		authors = new ArrayList<String>();
	}
	
	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	
	public void addAuthor(String author){
		if(!authors.contains(author)){
			authors.add(author);
		}
	}

	public long getISBN() {
		return ISBN;
	}
	
	public void setISBN(long iSBN) {
		ISBN = iSBN;
	}
	
	public int getpYear() {
		return pYear;
	}
	
	public void setpYear(int pYear) {
		this.pYear = pYear;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getNumberOfCopies() {
		return numberOfCopies;
	}
	
	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}
	
	public int getThreshold() {
		return threshold;
	}
	
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getNumberOfPurchases() {
		return numberOfPurchases;
	}

	public void setNumberOfPurchases(int numberOfPurchases) {
		this.numberOfPurchases = numberOfPurchases;
	}
	
}
