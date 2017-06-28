package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eg.edu.alexu.csd.database.mylibrary.controller.ManagerContoller;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class TotalSales extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2449782923627799077L;
	
	private ManagerContoller managerContoller;
	private CardLayout frameCardLayout;
	private JPanel framePanel, info, btns;
	private JTable table;
	private JButton backB;
	
	private ArrayList<Data> books;

	public TotalSales(ManagerContoller managerContoller, CardLayout mainLibraryCardLayout, JPanel mainLibraryPanel) {
		this.managerContoller = managerContoller;
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		
		backB = new JButton("Back");
		Back b = new Back();
		backB.addActionListener(b);
		
		info = new JPanel();
		btns = new JPanel();
		
		table = new JTable();
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
	
	public void ShowUI(){
		books = getBooks();
		table.setModel(updateBooksTable(table, books) );
		MessageFormat header = new MessageFormat("top sales");
		MessageFormat footer = new MessageFormat("{1,number,interger}");
		revalidate();
		repaint();
		
	}
	
	private TableModel updateBooksTable(JTable table, ArrayList<Data> bookList){
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Constants.BOOK_ISBN);
		columnNames.add(Constants.BOOK_TITLE);
		columnNames.add(Constants.BOOK_COPIES_NUMBER);
		columnNames.add("Total price");
		tableModel.setColumnIdentifiers(columnNames);
				
		for (Data book : bookList) {
			 Vector<Object> row = new Vector<Object>();
			 row.add(book.ISBN);
			 row.add(book.title);
			 row.add(book.quantity);
			 row.add(book.price);
			 tableModel.addRow(row);
		}
		
		tableModel.fireTableDataChanged();
		return tableModel;
	}
	

	private ArrayList<Data> getBooks() {
		ArrayList<Data> myData = new ArrayList<Data>();
		try {
			ResultSet rs = managerContoller.viewTotalSales();
			while(rs.next()){
				Data d = new Data();
				d.ISBN = rs.getLong(Constants.BOOK_ISBN);
				d.quantity = rs.getInt("total");
				d.price = rs.getInt(Constants.BOOK_PRICE) * d.quantity;
				d.title = rs.getString(Constants.BOOK_TITLE);
				myData.add(d);
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
	
	private class Data {
		private long ISBN;
		private int quantity, price;
		private String title;
	}
}
