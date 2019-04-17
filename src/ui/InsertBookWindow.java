package ui;

import dao.DbAccess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertBookWindow {
	private JFrame frame;
	private LibraryWindow mainWindow;
	private DbAccess db;

	public InsertBookWindow(LibraryWindow mainWindow, DbAccess db) {
		this.mainWindow = mainWindow;
		this.db = db;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Додати книгу");
		frame.setSize(400, 280);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel dateLbl = new JLabel("Дата (yyyy-mm-dd):");
		panel.add(dateLbl);
		
		JTextField dateField = new JTextField();
		panel.add(dateField);

		JLabel topicLbl = new JLabel("Тема:");
		panel.add(topicLbl);

		JTextField topicField = new JTextField();
		panel.add(topicField);
		
		JLabel titleLbl = new JLabel("Назва:");
		panel.add(titleLbl);
		
		JTextField titleField = new JTextField();
		panel.add(titleField);
		
		JLabel typeLbl = new JLabel("Тип:");
		panel.add(typeLbl);
		
		JComboBox<String> typeField = new JComboBox<String>();
		typeField.addItem("monographia");
		typeField.addItem("posibnyk");
		typeField.addItem("pidruchnyk");
		panel.add(typeField);

		JLabel keywordsLbl = new JLabel("Ключові слова (через кому):");
		panel.add(keywordsLbl);

		JTextField keywordsField = new JTextField();
		panel.add(keywordsField);
		
		JLabel sizeLbl = new JLabel("Об'єм:");
		panel.add(sizeLbl);
		
		JTextField sizeField = new JTextField();
		panel.add(sizeField);
		
		JLabel eVersionLbl = new JLabel("Е-копія:");
		panel.add(eVersionLbl);
		
		JCheckBox eVersion = new JCheckBox("");
	    panel.add(eVersion);
	    
	    JLabel copiesLbl = new JLabel("Екземпляри:");
	    panel.add(copiesLbl);
	    
	    JTextField copiesField = new JTextField();
	    panel.add(copiesField);

		JLabel authorsLbl = new JLabel("Автори:");
		panel.add(authorsLbl);

		JTextField authorsField = new JTextField();
		panel.add(authorsField);
		
	    JLabel message = new JLabel();
		message.setForeground(Color.red);
		panel.add(message);
		
		JButton submitBtn = new JButton("Додати");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String dateOfPublication = dateField.getText();
					String topic = topicField.getText();
					String title = titleField.getText();
					String type = typeField.getSelectedItem().toString();
					String keywords = keywordsField.getText();
					String[] authors = authorsField.getText().split(", ");
					int size = Integer.parseInt(sizeField.getText());
					int hasElectronicCopy = eVersion.isSelected() ? 1 : 0;
					int numberOfCopies = Integer.parseInt(copiesField.getText());

					if (title.length() < 1) throw new Exception("Назва закоротка");
					if (keywords.length() < 1) throw new Exception("Невідповідні ключові слова");
					if (size < 1) throw new Exception("Неправильний розмір");
					if (numberOfCopies < 1) throw new Exception("Щонайменше одна копія");

					db.insertBook(dateOfPublication,topic, title, type, keywords, authors, size, hasElectronicCopy, numberOfCopies);

					message.setText("Додано!");

					dateField.setEditable(false);
					titleField.setEditable(false);
					typeField.setEnabled(false);
					sizeField.setEditable(false);
					eVersion.setEnabled(false);
					copiesField.setEditable(false);
					submitBtn.setEnabled(false);

					mainWindow.fillBookTable(title);
				} catch (Exception exc) {
					message.setText("Щось неправильно!" + exc.getMessage());
				}
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}

