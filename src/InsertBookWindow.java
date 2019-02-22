import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InsertBookWindow {
	
	private JFrame frame;

	public InsertBookWindow() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Додати книгу");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6,2));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel dateLbl = new JLabel("Дата:");
		panel.add(dateLbl);
		
		JTextField dateField = new JTextField();
		panel.add(dateField);
		
		JLabel titleLbl = new JLabel("Назва:");
		panel.add(titleLbl);
		
		JTextField titleField = new JTextField();
		panel.add(titleField);
		
		JLabel typeLbl = new JLabel("Тип:");
		panel.add(typeLbl);
		
		JComboBox<String> typeField = new JComboBox<String>();
		typeField.addItem("посібник");
		typeField.addItem("підручник");
		panel.add(typeField);
		
		JLabel sizeLbl = new JLabel("Об'єм:");
		panel.add(sizeLbl);
		
		JTextField sizeField = new JTextField();
		panel.add(sizeField);
		
		JLabel eVersionLbl = new JLabel("Е-копія:");
		panel.add(eVersionLbl);
		
		JCheckBox eVersion = new JCheckBox("");
	    panel.add(eVersion);
		
		JButton submitBtn = new JButton("Зберегти");
		panel.add(submitBtn);
		
	    frame.getContentPane().add(panel);
	}

}
