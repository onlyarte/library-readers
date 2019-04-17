package ui;

import dao.DbAccess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		panel.setLayout(new GridLayout(9,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel dateLbl = new JLabel("Дата (yyyy-mm-dd):");
		panel.add(dateLbl);
		
		JTextField dateField = new JTextField();
		panel.add(dateField);
		
		JLabel titleLbl = new JLabel("Назва:");
		panel.add(titleLbl);
		
		JTextField titleField = new JTextField();
		panel.add(titleField);

		JLabel topicLbl = new JLabel("Тема:");
		panel.add(topicLbl);

		JTextField topicField = new JTextField();
		panel.add(topicField);
		
		JLabel numOfPublLbl = new JLabel("Кількість публ.:");
		panel.add(numOfPublLbl);
		
		JTextField numOfPublField = new JTextField();
		panel.add(numOfPublField);
		
		JLabel publNamesLbl = new JLabel("Публікації (через кому):");
		panel.add(publNamesLbl);
		
		JTextField publicationsNameField = new JTextField();
		panel.add(publicationsNameField);

		JLabel keywordsLbl = new JLabel("Ключові слова (через кому):");
		panel.add(keywordsLbl);

		JTextField keywordsField = new JTextField();
		panel.add(keywordsField);
		
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
					String dateOfPublication = dateField.getText();
					String title = titleField.getText();
					String topic = topicField.getText();
					int numOfPublications = Integer.parseInt(numOfPublField.getText());
					String publicationsName = publicationsNameField.getText();
					String publicationsKeywords = keywordsField.getText();
					int hasElectronicCopy = eVersion.isSelected() ? 1 : 0;
					int numberOfCopies = Integer.parseInt(copiesField.getText());

					if (title.length() < 1) throw new Exception("Назва закоротка");
					if (numOfPublications < 1) throw new Exception("Неправильний розмір");
					if (numberOfCopies < 1) throw new Exception("Щонайменше одна копія");

					db.insertSeries(dateOfPublication, title, topic, numOfPublications, publicationsName, publicationsKeywords, hasElectronicCopy, numberOfCopies);

					message.setText("Додано!");

					dateField.setEditable(false);
					titleField.setEditable(false);
					numOfPublField.setEnabled(false);
					publicationsNameField.setEditable(false);
					keywordsField.setEditable(false);
					eVersion.setEnabled(false);
					copiesField.setEditable(false);
					submitBtn.setEnabled(false);

					mainWindow.fillSeriesTable(title);
				} catch (Exception exc) {
					message.setText("Щось неправильно!");
				}
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}

