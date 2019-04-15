package ui;

import dao.*;
import models.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TakeBookWindow {
	private JFrame frame;
	private LibraryWindow mainWindow;
	private DbAccess db;
	private int selectedBookId;
	private int readerId;

	public TakeBookWindow(LibraryWindow mainWindow, DbAccess db, int readerId) {
		this.mainWindow = mainWindow;
		this.db = db;
		this.readerId = readerId;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Взяти примірник");
		frame.setSize(400, 140);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel bookLbl = new JLabel("Книга:");
		panel.add(bookLbl);
		
		JComboBox<String> bookField = new JComboBox<String>();
		panel.add(bookField);
		
		JLabel copyLbl = new JLabel("Примірник:");
		panel.add(copyLbl);
		
		JComboBox<String> copyField = new JComboBox<String>();
		panel.add(copyField);
		
		JLabel message = new JLabel();
		message.setForeground(Color.red);
		panel.add(message);
		
		ArrayList<Book> books = db.getBooks(null);
		for (Book book: books) {
			bookField.addItem(book.getBookId() + " " + book.getTitle());
		}
		selectedBookId = books.get(0).getBookId();
		ArrayList<Integer> freeCopies = db.getFreeEditionCopies(selectedBookId);
		for (Integer freeCopy: freeCopies) {
			copyField.addItem(String.valueOf(freeCopy));
		}

		bookField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String selectedBook = bookField.getSelectedItem().toString();
				selectedBookId = Integer.parseInt(selectedBook.substring(0, selectedBook.indexOf(' ')));
				ArrayList<Integer> freeCopies = db.getFreeEditionCopies(selectedBookId);
				copyField.removeAllItems();
				for (Integer freeCopy: freeCopies) {
					copyField.addItem(String.valueOf(freeCopy));
				}
			}
		});
		
		JButton submitBtn = new JButton("Взяти");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int copyId = Integer.parseInt(copyField.getSelectedItem().toString());
				db.insertReading(readerId, copyId);
				mainWindow.fillReaderDebtorTable();

				message.setText("Збережено!");

				bookField.setEnabled(false);
				copyField.setEnabled(false);
				submitBtn.setEnabled(false);
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
