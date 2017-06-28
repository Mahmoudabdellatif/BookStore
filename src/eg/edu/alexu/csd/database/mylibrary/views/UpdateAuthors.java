package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class UpdateAuthors extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2958058255164938942L;
	
	private CardLayout frameCardLayout;
	private JPanel framePanel;
	private UpdateBookView updateBookView;
	private UpdateAuthors updateAuthors;
	
	private ArrayList<JLabel> authorsL;
	private ArrayList<String> authors;
	
	private JPanel labelsPanel, btnsPanel;
	private JButton addB, doneB, removeB;

	public UpdateAuthors(ArrayList<String> authors, CardLayout frameCardLayout,
			JPanel framePanel, UpdateBookView updateBookView) {
		// TODO Auto-generated constructor stub
		authorsL =  new ArrayList<JLabel>();
		this.authors = authors;
		this.updateBookView = updateBookView;
		this.updateAuthors = this;
		this.frameCardLayout = frameCardLayout;
		this.framePanel = framePanel;
		
		labelsPanel = new JPanel();
		btnsPanel = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane(labelsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
		addB = new JButton("Add Author");
		removeB = new JButton("RemoveAuthor");
		doneB = new JButton("Done");
		
		labelsPanel.setLayout(new GridLayout(authors.size(), 1));
		for(int i = 0; i < authors.size(); i++){
			JLabel label = new JLabel(authors.get(i));
			authorsL.add(label);
			labelsPanel.add(authorsL.get(i));
		}
		
		AddAuthor aa = new AddAuthor();
		addB.addActionListener(aa);
		RemoveAuthor ra = new RemoveAuthor();
		removeB.addActionListener(ra);
		Done d = new Done();
		doneB.addActionListener(d);
		
		btnsPanel.setLayout(new GridLayout(1, 3));
		btnsPanel.add(addB);
		btnsPanel.add(removeB);
		btnsPanel.add(doneB);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane);
		this.add(btnsPanel, BorderLayout.SOUTH);
	}
	
	private class AddAuthor implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String author = JOptionPane.showInputDialog("Enter Author Name :","");
			if(author.length()>0){
				if(!authors.contains(author)){
					authors.add(author);
					JLabel label = new JLabel(author);
					authorsL.add(label);
					labelsPanel.add(authorsL.get(authors.indexOf(author)));
					labelsPanel.revalidate();
					labelsPanel.repaint();
				}
				else{
					JOptionPane.showMessageDialog(null,"This author already added","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"You didn't enter a name.","Warning",JOptionPane.WARNING_MESSAGE);
			}
			labelsPanel.revalidate();
			labelsPanel.repaint();
		}
	}
	
	private class RemoveAuthor implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String author = JOptionPane.showInputDialog("Enter Author Name :","");
			if(author.length()>0){
				if(authors.contains(author)){
					labelsPanel.remove(authorsL.get(authors.indexOf(author)));
					authorsL.remove(authors.indexOf(author));
					authors.remove(author);
					labelsPanel.revalidate();
					labelsPanel.repaint();
				}
				else{
					JOptionPane.showMessageDialog(null,"This author doesn't exist","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"You didn't enter a name.","Warning",JOptionPane.WARNING_MESSAGE);
			}
			labelsPanel.revalidate();
			labelsPanel.repaint();
		}
	}
	
	private class Done implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			updateBookView.updateAuthors(authors);
			frameCardLayout.show(framePanel, Constants.MANAGER_UPDATE_BOOK);
			framePanel.remove(updateAuthors);
		}
		
	}
}
