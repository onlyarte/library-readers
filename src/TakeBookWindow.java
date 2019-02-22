import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TakeBookWindow {
	
	private JFrame frame;

	public TakeBookWindow() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Взяти видання");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel typeLbl = new JLabel("Тип видання:");
		panel.add(typeLbl);
		
		JComboBox<String> typeField = new JComboBox<String>();
		typeField.addItem("книга");
		typeField.addItem("збірка");
		panel.add(typeField);
		
		JLabel bookLbl = new JLabel("Книга:");
		panel.add(bookLbl);
		
		JComboBox<String> bookField = new JComboBox<String>();
		bookField.addItem("C++ for Beginners");
		panel.add(bookField);
		
		JLabel copyLbl = new JLabel("Примірник:");
		panel.add(copyLbl);
		
		JComboBox<String> copyField = new JComboBox<String>();
		copyField.addItem("1");
		panel.add(copyField);
		
		JButton submitBtn = new JButton("Взяти");
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
