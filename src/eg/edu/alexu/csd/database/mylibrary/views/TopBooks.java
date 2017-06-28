package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import eg.edu.alexu.csd.database.mylibrary.controller.ManagerContoller;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;

public class TopBooks extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 252121571126147454L;
	
	private ManagerContoller managerContoller;
	private CardLayout frameCardLayout;
	private JPanel framePanel;

	private JButton backB;
	
	private ArrayList<Book> books;

	public TopBooks(ManagerContoller managerContoller, CardLayout mainLibraryCardLayout, JPanel mainLibraryPanel) {
		this.managerContoller = managerContoller;
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		
		backB = new JButton("Back");
		Back b = new Back();
		backB.addActionListener(b);
	}
	
	public void ShowUI(){
		JPanel info = new JPanel();
		JPanel btns = new JPanel();
		
		books = getBooks();
		
		JTable table = new JTable(getData(),getColumnNames());
		table.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		this.setLayout(new BorderLayout());
		info.setLayout(new FlowLayout());
		btns.setLayout(new FlowLayout());
		
		info.add(scrollPane);
		
		btns.add(backB);
		
		this.add(info, BorderLayout.NORTH);
		this.add(btns, BorderLayout.SOUTH);
	}
	
	private Object[][] getData() {
		Object[][] data = new Object[books.size()][5];
		for(int i = 0; i < books.size(); i++){
			data[i][0] = books.get(i).getISBN();
			data[i][1] = books.get(i).getTitle();
			data[i][2] = books.get(i).getNumberOfPurchases();
		}
		return data;
	}

	private String[] getColumnNames() {
		String[] names = {
				Constants.BOOK_ISBN,
				Constants.BOOK_TITLE,
				"total number sold"
		};
		return names;
	}

	private ArrayList<Book> getBooks() {
		ArrayList<Book> myData = new ArrayList<Book>();
		try {
			ResultSet rs = managerContoller.viewTotalSales();
			for(int i = 0; i < 10 && rs.next(); i++){
				Book book = new Book();
				book.setISBN(rs.getLong(Constants.BOOK_ISBN));
				book.setNumberOfPurchases(rs.getInt(Constants.USER_PURCHASES_COUNT));
				book.setTitle(rs.getString(Constants.BOOK_TITLE));
				myData.add(book);
			}
			return myData;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private class Back implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			frameCardLayout.show(framePanel, "Manager View");
		}
		
	}
	
}