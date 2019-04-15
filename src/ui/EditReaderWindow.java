package ui;

import dao.DbAccess;
import models.Reader;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditReaderWindow {
	private JFrame frame;
	private LibraryWindow mainWindow;
	private DbAccess db;
	private int readerId;
	private Reader reader;

	public EditReaderWindow(LibraryWindow mainWindow, DbAccess db, int readerId) {
		this.db = db;
		this.mainWindow = mainWindow;
		this.readerId = readerId;
		
		reader = db.getReader(readerId);
		
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Редагувати читача");
		frame.setSize(400, 160);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel message = new JLabel();
		message.setForeground(Color.red);
		
		JLabel surnameLbl = new JLabel("Прізвище:");
		panel.add(surnameLbl);
		
		JTextField surnameField = new JTextField();
		surnameField.setText(reader.getLastName());
		panel.add(surnameField);
		
		JLabel phoneLbl = new JLabel("Номер телефону:");
		panel.add(phoneLbl);
		
		JTextField phoneField = new JTextField();
		phoneField.setText(reader.getPhoneNumber());
		panel.add(phoneField);
		
		JLabel bonusLbl = new JLabel("Бонуси:");
		panel.add(bonusLbl);
		
		JTextField bonusField = new JTextField();
		bonusField.setText(String.valueOf(reader.getBonusPoints()));
		panel.add(bonusField);
		
		panel.add(message);
		
		JButton submitBtn = new JButton("Зберегти");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String lastName = surnameField.getText();
					String phoneNumber = phoneField.getText();
					int bonusPoints = Integer.parseInt(bonusField.getText());

					if (lastName.length() < 2) throw new Exception("Прізвище закоротке");
					if (phoneNumber.length() < 5) throw new Exception("Телефон закороткий");
					if (bonusPoints < 0) throw new Exception("Бонуси ненатуральне число");

					db.updateReader(readerId, lastName, phoneNumber, bonusPoints);

					message.setText("Збережено!");

					surnameField.setEditable(false);
					phoneField.setEditable(false);
					bonusField.setEditable(false);
					submitBtn.setEnabled(false);

					mainWindow.fillReaderReaderTable();
				} catch (Exception exc) {
					message.setText("Щось неправильно!");
				}
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
