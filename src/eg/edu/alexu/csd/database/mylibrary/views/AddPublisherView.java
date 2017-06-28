package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class AddPublisherView extends JPanel {
	
	
	private CardLayout frameCardLayout;
	private JPanel framePanel;
	
	private JLabel nameL, addressL, phoneL;
	private JTextField nameTF, addressTF, phoneTF;
	private JButton doneB, backB;
	
	public AddPublisherView(CardLayout mainLibraryCardLayout, JPanel mainLibraryPanel) {
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		
		nameL = new JLabel("Name");
		addressL = new JLabel("Address");
		phoneL = new JLabel("Phone Number");
		
		nameTF = new JTextField();
		addressTF = new JTextField();
		phoneTF = new JTextField();
		
		doneB = new JButton("Done");
		backB = new JButton("Back");
		Done d = new Done();
		doneB.addActionListener(d);
		Back b = new Back();
		backB.addActionListener(b);
		
		JPanel info = new JPanel();
		JPanel btns = new JPanel();
		
		info.setLayout(new GridLayout(Constants.PUBLISHER_ATTRIBUTE_COUNT, 2));
		btns.setLayout(new GridLayout(1, 2));
		
		info.add(nameL);
		info.add(nameTF);
		info.add(addressL);
		info.add(addressTF);
		info.add(phoneL);
		info.add(phoneTF);
		
		btns.add(doneB);
		btns.add(backB);
		
		this.setLayout(new BorderLayout());
		this.add(info);
		this.add(btns, BorderLayout.SOUTH);
	}

	private class Done implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = nameTF.getText();
			String address = addressTF.getText();
			String phone = phoneTF.getText();
			
			if(name.length() > 0){
				String query = "select " + Constants.PUBLISHER_NAME + " from " + Constants.PUBLISHER_TABLE_NAME
						+ " where " + Constants.PUBLISHER_NAME + " = '" + name + "'";
				try {
					ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
					if(rs.next()){
						JOptionPane.showMessageDialog(null,
								"This publisher already exists", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else{
						if(address.length() > 0){
							query = "insert into "+ Constants.PUBLISHER_TABLE_NAME 
									+ " values ('" + name + "','" 
									+ address + "','" + phone + "')";
							System.out.println(query);
							MyDBConnection.getInstance().executeUpdateQuery(query);
							frameCardLayout.show(framePanel, "Manager View");
						}
						else{
							JOptionPane.showMessageDialog(null,
									"There is no address entered\n" + "Please Enter address", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(null,	e1.getMessage(), "Warning",	JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null,
						"There is no publishing year entered\n" + "Please Enter an author", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	private class Back implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearFields();
			frameCardLayout.show(framePanel, "Manager View");

		}

	}

	private void clearFields() {
		nameTF.setText("");
		addressTF.setText("");
		phoneTF.setText("");
		
	}
}
