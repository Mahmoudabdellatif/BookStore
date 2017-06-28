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

import eg.edu.alexu.csd.database.mylibrary.controller.ManagerContoller;
import eg.edu.alexu.csd.database.mylibrary.models.Order;
import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;

public class TopCustomers extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3424130105375582247L;
	
	private ManagerContoller managerContoller;
	private CardLayout frameCardLayout;
	private JPanel framePanel, info, btns;

	private JButton backB;
	private JTable table;
	
	private ArrayList<User> users;

	public TopCustomers(ManagerContoller managerContoller, CardLayout mainLibraryCardLayout, JPanel mainLibraryPanel) {
		this.managerContoller = managerContoller;
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		
		users = new ArrayList<User>();
		
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
		users = getUsers();
		table.setModel(updateCustomerTable(table, users) );
		revalidate();
		repaint();
	}
	
	private TableModel updateCustomerTable(JTable table, ArrayList<User> userList){
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Constants.USER_USERNAME);
		columnNames.add(Constants.USER_FIRST_NAME);
		columnNames.add(Constants.USER_LAST_NAME);
		columnNames.add("Purchases count");
		tableModel.setColumnIdentifiers(columnNames);
				
		for (User user : userList) {
			 Vector<Object> row = new Vector<Object>();
			 row.add(user.getUsername());
			 row.add(user.getFirstName());
			 row.add(user.getLastName());
			 row.add(user.getNumberOfPurchases());
			 tableModel.addRow(row);
		}
		
		tableModel.fireTableDataChanged();
		return tableModel;
	}

	private ArrayList<User> getUsers() {
		ArrayList<User> myData = new ArrayList<User>();
		try {
			ResultSet rs = managerContoller.viewTopFiveCustomers();
			for(int i = 0; i < 5 && rs.next(); i++){
				User user = new User();
				user.setUsername(rs.getString(Constants.USER_USERNAME));
				user.setFirstName(rs.getString(Constants.USER_FIRST_NAME));
				user.setLastName(rs.getString(Constants.USER_LAST_NAME));
				user.setNumberOfPurchases(rs.getInt(Constants.USER_PURCHASES_COUNT));
				myData.add(user);
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
