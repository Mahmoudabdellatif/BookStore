package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.models.Item;
import eg.edu.alexu.csd.database.mylibrary.models.Order;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class OrderView extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1886466981643118651L;
	
	private CardLayout frameCardLayout;
	private JPanel framePanel, info, btns;
	
	private JTable table;
	private JScrollPane scrollPane;
	private JButton backB;
	
	private ArrayList<Order> orders;
	
	private boolean added = false;
	
	public OrderView(JPanel mainLibraryPanel, CardLayout mainLibraryCardLayout) {
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		
		orders = new ArrayList<Order>();
		backB = new JButton("Back");
		Back b = new Back();
		backB.addActionListener(b);
				
	}
	
	public void showOrders(){
		if(!added){
			info = new JPanel();
			btns = new JPanel();
			table = new JTable();
			table.setEnabled(false);
			scrollPane = new JScrollPane(table);
			this.setLayout(new BorderLayout());
			info.setLayout(new FlowLayout());
			btns.setLayout(new FlowLayout());
			
			info.add(scrollPane);
			
			btns.add(backB);
			
			this.add(info, BorderLayout.NORTH);
			this.add(btns, BorderLayout.SOUTH);
			added = true;
		}
		
		orders = getOrders();
		table.setModel(updateOwnerTable(table, orders) );
		revalidate();
		repaint();
	}
	
	private TableModel updateOwnerTable(JTable table, ArrayList<Order> orderList)
	{
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Constants.ORDER_NUMBER);
		columnNames.add(Constants.ORDER_ISBN);
		columnNames.add(Constants.ORDER_QUANTITY);
		tableModel.setColumnIdentifiers(columnNames);
				
		for (Order order : orderList) {
			 Vector<Object> row = new Vector<Object>();
			 row.add(order.getOrderNumber());
			 row.add(order.getISBN());
			 row.add(order.getQuantity());
			 tableModel.addRow(row);
		}
		
		tableModel.fireTableDataChanged();
		return tableModel;
	}

	private Object[][] getData() {
		Object[][] data = new Object[orders.size()][3];
		for(int i = 0; i < orders.size(); i++){
			data[i][0] = orders.get(i).getOrderNumber();
			data[i][1] = orders.get(i).getISBN();
			data[i][2] = orders.get(i).getQuantity();
		}
		return data;
	}

	private String[] getColumnNames() {
		String[] names = {
				Constants.ORDER_NUMBER,
				Constants.ORDER_ISBN,
				Constants.ORDER_QUANTITY
		};
		return names;
	}

	private ArrayList<Order> getOrders() {
		ArrayList<Order> orderList = new ArrayList<Order>();
		try {
			String query = "Select * from " + Constants.ORDER_TABLE_NAME;
			ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
			while(rs.next()){
				Order order = new Order();
				order.setOrderNumber(rs.getInt(Constants.ORDER_NUMBER));
				order.setISBN(rs.getLong(Constants.ORDER_ISBN));
				order.setQuantity(rs.getInt(Constants.ORDER_QUANTITY));
				order.setAccepted(rs.getInt(Constants.ORDER_ACCEPTANCE));
				orderList.add(order);
			}
			return orderList;
		} catch (Exception e) {
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
