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

public class InsertReaderWindow {
	private JFrame frame;
	private LibraryWindow mainWindow;
	private DbAccess db;

	public InsertReaderWindow(LibraryWindow mainWindow, DbAccess db) {
		this.mainWindow = mainWindow;
		this.db = db;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Додати читача");
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
		panel.add(surnameField);
		
		JLabel phoneLbl = new JLabel("Номер телефону:");
		panel.add(phoneLbl);
		
		JTextField phoneField = new JTextField();
		panel.add(phoneField);
		
		JLabel bonusLbl = new JLabel("Бонуси:");
		panel.add(bonusLbl);
		
		JTextField bonusField = new JTextField();
		bonusField.setText("5");
		panel.add(bonusField);
		
		panel.add(message);
		
		JButton submitBtn = new JButton("Додати");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String lastName = surnameField.getText();
					String phoneNumber = phoneField.getText();
					int bonusPoints = Integer.parseInt(bonusField.getText());

					if (lastName.length() < 2) throw new Exception("Прізвище має мати щонайменше 2 символи");
					if (phoneNumber.length() < 5) throw new Exception("Номер телефону має мати щонайменше 5 символів");
					if (bonusPoints < 0) throw new Exception("Бонуси мають бути натуральним числом");

					db.insertReader(lastName, phoneNumber, bonusPoints);

					message.setText("Додано!");

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
