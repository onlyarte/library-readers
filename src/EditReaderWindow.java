import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditReaderWindow {
	
	private JFrame frame;
	
	private DbAccess db;

	public EditReaderWindow(DbAccess db, int readerId) {
		this.db = db;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Редагувати читача");
		frame.setSize(400, 120);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel lastNameLbl = new JLabel("Прізвище:");
		panel.add(lastNameLbl);
		
		JTextField lastNameField = new JTextField();
//		lastNameField.setText(reader.getLastName());
		panel.add(lastNameField);
		
		JLabel phoneLbl = new JLabel("Номер телефону:");
		panel.add(phoneLbl);
		
		JTextField phoneField = new JTextField();
//		phoneField.setText(reader.getPhoneNumber());
		panel.add(phoneField);
		
		JButton submitBtn = new JButton("Зберегти");
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
