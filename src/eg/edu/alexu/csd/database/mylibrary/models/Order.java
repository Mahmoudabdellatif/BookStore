package eg.edu.alexu.csd.database.mylibrary.models;

public class Order {
	
	private long ISBN;
	private int orderNumber, quantity, accepted;
	
	public long getISBN() {
		return ISBN;
	}
	
	public void setISBN(long iSBN) {
		ISBN = iSBN;
	}
	
	public int isAccepted() {
		return accepted;
	}
	
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
