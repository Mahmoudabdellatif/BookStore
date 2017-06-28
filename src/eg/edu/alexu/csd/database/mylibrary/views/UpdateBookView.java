package eg.edu.alexu.csd.database.mylibrary.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eg.edu.alexu.csd.database.mylibrary.MyDBConnection;
import eg.edu.alexu.csd.database.mylibrary.controller.ManagerContoller;
import eg.edu.alexu.csd.database.mylibrary.models.Book;
import eg.edu.alexu.csd.database.mylibrary.models.Category;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;
import eg.edu.alexu.csd.database.mylibrary.utils.StaticMethods;

public class UpdateBookView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6602933288980444556L;

	private CardLayout frameCardLayout;
	private JPanel framePanel;
	private ManagerContoller managerContoller;
	private UpdateAuthors updateAuthor;

	private Book book;

	private JLabel titleL, publisherL, pYearL, priceL, categoryL, numberOfCopiesL, thresholdL, authorL, authorsL;
	private JTextField titleTF, publisherTF, pYearTF, priceTF, numberOfCopiesTF, thresholdTF;
	private JComboBox<String> categotyList;
	private JButton addAuthorsB, doneB, backB;
	private ArrayList<Category> categories;
	private ArrayList<String> authors;
	private String category;

	private boolean added = false;
	
	public UpdateBookView(ManagerContoller managerContoller, CardLayout mainLibraryCardLayout,
			JPanel mainLibraryPanel) {
		this.frameCardLayout = mainLibraryCardLayout;
		this.framePanel = mainLibraryPanel;
		this.managerContoller = managerContoller;
		
		titleL = new JLabel("Title : ");
		publisherL = new JLabel("Publisher : ");
		pYearL = new JLabel("Publishing year : ");
		priceL = new JLabel("Price : ");
		categoryL = new JLabel("Category : ");
		numberOfCopiesL = new JLabel("NUmber Of Copies : ");
		thresholdL = new JLabel("Threshold : ");
		authorL = new JLabel("Authors : ");
		authorsL = new JLabel("");

		titleTF = new JTextField("");
		publisherTF = new JTextField("");
		pYearTF = new JTextField("");
		priceTF = new JTextField("");
		numberOfCopiesTF = new JTextField("");
		thresholdTF = new JTextField("");
		addAuthorsB = new JButton("Update Authors");
		doneB = new JButton("Done");
		backB = new JButton("Back");

		UpdateAuthor ua = new UpdateAuthor();
		addAuthorsB.addActionListener(ua);
		Done d = new Done();
		doneB.addActionListener(d);
		Back b = new Back();
		backB.addActionListener(b);

	}

	public void setBook(Book book) {
		this.book = book;
		setUpdateUI();
		revalidate();
		repaint();
	}

	private void setUpdateUI() {
		if(!added){
			categotyList = new JComboBox<String>();
			// TODO remove what is used for debug
			categories = StaticMethods.getCategories();
//			categories = new ArrayList<Category>();
//			Category ca = new Category();
//			ca.setKey("c");
//			ca.setName("Category1");
//			categories.add(ca);
			
			JPanel info = new JPanel();
			JPanel btns = new JPanel();

			this.setLayout(new BorderLayout());
			info.setLayout(new GridLayout(Constants.BOOK_ATTRIBUTE_COUNT, 2));
			btns.setLayout(new GridLayout(1, 3));

			info.add(titleL);
			info.add(titleTF);
			info.add(publisherL);
			info.add(publisherTF);
			info.add(categoryL);
			info.add(categotyList);
			info.add(pYearL);
			info.add(pYearTF);
			info.add(priceL);
			info.add(priceTF);
			info.add(numberOfCopiesL);
			info.add(numberOfCopiesTF);
			info.add(thresholdL);
			info.add(thresholdTF);
			info.add(authorL);
			info.add(authorsL);

			btns.add(addAuthorsB);
			btns.add(doneB);
			btns.add(backB);
			
			CategoryChange cc = new CategoryChange();
			categotyList.addActionListener(cc);

			this.add(info);
			this.add(btns, BorderLayout.SOUTH);
			added = true;
		}
		
		titleTF.setText(book.getTitle());
		publisherTF.setText(book.getPublisher());
		pYearTF.setText(Integer.toString(book.getpYear()));
		priceTF.setText(Integer.toString(book.getPrice()));
		numberOfCopiesTF.setText(Integer.toString(book.getNumberOfCopies()));
		thresholdTF.setText(Integer.toString(book.getThreshold()));

		authors = book.getAuthors();
		for (int i = 0; i < authors.size(); i++) {
			if (authorsL.getText().length() > 0) {
				authorsL.setText(authorsL.getText() + ",");
			}
			authorsL.setText(authorsL.getText() + authors.get(i));
		}

		for (int i = 0; i < categories.size(); i++) {
			categotyList.addItem(categories.get(i).getName());
			if (categories.get(i).getKey().equals(book.getCategoryKey())) {
				category = categories.get(i).getName();
				categotyList.setSelectedIndex(i);
			}
		}

	}

	private class CategoryChange implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			category = (String) cb.getSelectedItem();
		}

	}

	private class UpdateAuthor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updatePressed();
		}
	}

	private void updatePressed() {
		updateAuthor = new UpdateAuthors(authors, frameCardLayout, framePanel, this);
		framePanel.add(updateAuthor, "Update Author");
		frameCardLayout.show(framePanel, "Update Author");
	}

	private class Done implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (titleTF.getText().length() > 0) {
				book.setTitle(titleTF.getText());
				if (publisherTF.getText().length() > 0) {
					book.setPublisher(publisherTF.getText());
					String query = "select " + Constants.PUBLISHER_NAME + " from " + Constants.PUBLISHER_TABLE_NAME
							+ " where " + Constants.PUBLISHER_NAME + " = '" + book.getPublisher()+"'";
					try {
						ResultSet rs = MyDBConnection.getInstance().executeQuery(query);
						if (rs.next()) {
							for (int i = 0; i < categories.size(); i++) {
								if (categories.get(i).getName().equals(category)) {
									book.setCategoryKey(categories.get(i).getKey());
									break;
								}
							}
							if (pYearTF.getText().length() > 0) {
								try {
									book.setpYear(Integer.parseInt(pYearTF.getText()));
									Date date = new Date();
									LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
									int year = localDate.getYear();
									if (year >= book.getpYear()) {
										if (priceTF.getText().length() > 0) {
											if (numberOfCopiesTF.getText().length() > 0) {
												try {
													book.setPrice(Integer.parseInt(priceTF.getText()));
													book.setNumberOfCopies(
															Integer.parseInt(numberOfCopiesTF.getText()));
													if (thresholdTF.getText().length() > 0) {
														try {
															book.setThreshold(Integer.parseInt(thresholdTF.getText()));
															if (!authors.isEmpty()) {
																book.setAuthors(authors);
																try {
																	managerContoller.updateBook(book);
																	clearFields();
																	frameCardLayout.show(framePanel, "Manager View");
																} catch (Exception e5) {
																	JOptionPane.showMessageDialog(null, e5.getMessage(),
																			"Warning", JOptionPane.WARNING_MESSAGE);
																}
															} else {
																JOptionPane.showMessageDialog(null,
																		"There is no Authors entered\n"
																				+ "Please Enter an author",
																		"Warning", JOptionPane.WARNING_MESSAGE);
															}
														} catch (Exception e4) {
															JOptionPane.showMessageDialog(null,
																	"Error!!\n"
																			+ "Please Enter numbers only in threshold",
																	"Warning", JOptionPane.WARNING_MESSAGE);
														}
													} else {
														JOptionPane.showMessageDialog(null,
																"There is no threshold entered\n"
																		+ "Please Enter threshold",
																"Warning", JOptionPane.WARNING_MESSAGE);
													}
												} catch (Exception e3) {
													JOptionPane.showMessageDialog(null,
															"Error!!\n"
																	+ "Please Enter numbers only in Number of copies",
															"Warning", JOptionPane.WARNING_MESSAGE);
												}
											} else {
												JOptionPane.showMessageDialog(null,
														"There is no Number of copies entered\n"
																+ "Please Enter number of copies",
														"Warning", JOptionPane.WARNING_MESSAGE);
											}
										} else {
											JOptionPane.showMessageDialog(null,
													"There is no price was entered\n" + "Please Enter publisher",
													"Warning", JOptionPane.WARNING_MESSAGE);
										}

									} else {
										JOptionPane.showMessageDialog(null,
												"Error!!\n" + "Enter a year less than " + (year + 1), "Warning",
												JOptionPane.WARNING_MESSAGE);
									}

								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null,
											"Error!!\n" + "Please Enter numbers only in Publishing year", "Warning",
											JOptionPane.WARNING_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"There is no publishing year entered\n" + "Please Enter an author", "Warning",
										JOptionPane.WARNING_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"This publisher not exist\n", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "There is no publisher entered\n" + "Please Enter publisher",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "There is no title entered\n" + "Please Enter Title", "Warning",
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

	public void updateAuthors(ArrayList<String> authorList) {
		this.authors = authorList;
		authorsL.setText("");
		for (int i = 0; i < authors.size(); i++) {
			if (authorsL.getText().length() > 0) {
				authorsL.setText(authorsL.getText() + ",");
			}
			authorsL.setText(authorsL.getText() + authors.get(i));
		}
		framePanel.revalidate();
		framePanel.repaint();
	}

	private void clearFields() {
		titleTF.setText("");
		publisherTF.setText("");
		pYearTF.setText("");
		priceTF.setText("");
		numberOfCopiesTF.setText("");
		thresholdTF.setText("");
		authorsL.setText("");
		authors.clear();
	}

}
