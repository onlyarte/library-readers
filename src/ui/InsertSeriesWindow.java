package ui;

import dao.DbAccess;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InsertSeriesWindow {
	private JFrame frame;
	private LibraryWindow mainWindow;
	private DbAccess db;

	public InsertSeriesWindow(LibraryWindow mainWindow, DbAccess db) {
		this.mainWindow = mainWindow;
		this.db = db;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Додати збірник");
		frame.setSize(400, 270);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(7,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel dateLbl = new JLabel("Дата (yyyy-mm-dd):");
		panel.add(dateLbl);
		
		JTextField dateField = new JTextField();
		panel.add(dateField);
		
		JLabel titleLbl = new JLabel("Назва:");
		panel.add(titleLbl);
		
		JTextField titleField = new JTextField();
		panel.add(titleField);
		
		JLabel numOfPublLbl = new JLabel("Кількість публ.:");
		panel.add(numOfPublLbl);
		
		JTextField numOfPublField = new JTextField();
		panel.add(numOfPublField);
		
		JLabel publNamesLbl = new JLabel("Публікації (через кому):");
		panel.add(publNamesLbl);
		
		JTextField authorField = new JTextField();
		panel.add(authorField);
		
		JLabel eVersionLbl = new JLabel("Е-копія:");
		panel.add(eVersionLbl);
		
		JCheckBox eVersion = new JCheckBox("");
	    panel.add(eVersion);
	    
	    JLabel copiesLbl = new JLabel("Екземпляри:");
	    panel.add(copiesLbl);
	    
	    JTextField copiesField = new JTextField();
	    panel.add(copiesField);
		
	    JLabel message = new JLabel();
		message.setForeground(Color.red);
		panel.add(message);
		
		JButton submitBtn = new JButton("Додати");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
//					String dateOfPublication = dateField.getText();
//					String title = titleField.getText();
//					String type = typeField.getSelectedItem().toString();
//					int size = Integer.parseInt(sizeField.getText());
//					int hasElectronicCopy = eVersion.isSelected() ? 1 : 0;
//					int numberOfCopies = Integer.parseInt(copiesField.getText());
//
//					if (title.length() < 1) throw new Exception("Назва закоротка");
//					if (size < 1) throw new Exception("Неправильний розмір");
//					if (numberOfCopies < 1) throw new Exception("Щонайменше одна копія");
//
//					db.insertBook(dateOfPublication, title, type, size, hasElectronicCopy, numberOfCopies);
//
//					message.setText("Додано!");
//
//					dateField.setEditable(false);
//					titleField.setEditable(false);
//					typeField.setEnabled(false);
//					sizeField.setEditable(false);
//					eVersion.setEnabled(false);
//					copiesField.setEditable(false);
//					submitBtn.setEnabled(false);
//
//					mainWindow.fillBookTable();
				} catch (Exception exc) {
					message.setText("Щось неправильно!");
				}
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}

