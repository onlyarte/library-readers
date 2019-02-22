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
	private DbAccess db;
	private LibraryWindow mainWindow;

	public InsertReaderWindow(DbAccess db, LibraryWindow mainWindow) {
		this.db = db;
		this.mainWindow = mainWindow;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Додати читача");
		frame.setSize(400, 120);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,2));
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
		
		panel.add(message);
		
		JButton submitBtn = new JButton("Зберегти");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (surnameField.getText().length() < 2 || phoneField.getText().length() < 5) {
					message.setText("Щось неправильно!");
					return;
				}
				db.insertReader(surnameField.getText(), phoneField.getText(), 5);
				message.setText("Додано!");
				surnameField.setEditable(false);
				phoneField.setEditable(false);
				submitBtn.setEnabled(false);
				mainWindow.fillReaderReaderTable();
			}
		});
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
