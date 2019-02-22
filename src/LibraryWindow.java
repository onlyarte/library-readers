import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Console;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class LibraryWindow {
	private LibraryWindow window;
	private JFrame frame;
	private DbAccess db;
	
	private JTable readerReaderTable;
	private JTable readerDebtorTable;
	private JTable bookTable;
	
	private JButton readerReaderEditBtn;
	private JButton readerReaderTakeCopyBtn;

	private String readerReaderSearchQuery;

	public LibraryWindow() {
		window = this;

		initialize();

		db = new DbAccess();
		db.connect("Library", "root", "root");

		fillReaderReaderTable();
	}
	
	JComponent makeReaderReaderPanel() {
		JPanel readerReaderPanel = new JPanel(false);
		readerReaderPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		JPanel searchPanel = new JPanel();
		
		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);
		
		JButton searchBtn = new JButton("Шукати");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readerReaderSearchQuery = searchField.getText();
				fillReaderReaderTable();
			}
		});
		searchPanel.add(searchBtn);

		controlPanel.add(searchPanel, BorderLayout.LINE_START);

		JPanel actionPanel = new JPanel();

		JButton insertReaderBtn = new JButton("Додати");
		insertReaderBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new InsertReaderWindow(window, db);
			}
			
		});
		actionPanel.add(insertReaderBtn);
		
		readerReaderEditBtn = new JButton("Редагувати");
		readerReaderEditBtn.setVisible(false);
		readerReaderEditBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedReaderId = Integer.parseInt(readerReaderTable.getValueAt(readerReaderTable.getSelectedRow(), 0).toString());
				System.out.println(selectedReaderId);
				new EditReaderWindow(window, db, selectedReaderId);
			}
		});
		actionPanel.add(readerReaderEditBtn);
		
		readerReaderTakeCopyBtn = new JButton("Взяти прим.");
		readerReaderTakeCopyBtn.setToolTipText("Взяти примірник");
		readerReaderTakeCopyBtn.setVisible(false);
		readerReaderTakeCopyBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new TakeBookWindow();
			}
			
		});
		actionPanel.add(readerReaderTakeCopyBtn);
		
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		readerReaderPanel.add(controlPanel, BorderLayout.NORTH);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());
		
		readerReaderTable = new JTable();
		readerReaderTable.setFillsViewportHeight(true);
		readerReaderTable.setDefaultEditor(Object.class, null);
		readerReaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		String[] columnNames = { "ID", "Прізвище", "Номер телефону", "Бонуси" };
		DefaultTableModel readerReaderTableModel = (DefaultTableModel) readerReaderTable.getModel();
		readerReaderTableModel.setColumnIdentifiers(columnNames);
		
		readerReaderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (readerReaderTable.getSelectedRow() != -1) {
					readerReaderEditBtn.setVisible(true);
					readerReaderTakeCopyBtn.setVisible(true);
				}
			}
	    });

		JScrollPane scrollPane = new JScrollPane(readerReaderTable);
		tablePanel.add(scrollPane);
	
		readerReaderPanel.add(tablePanel, BorderLayout.CENTER);

		return readerReaderPanel;
	}
	
	JComponent makeReaderDebtorPanel() {
		JPanel debtorPanel = new JPanel(false);
		debtorPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		JPanel searchPanel = new JPanel();
		controlPanel.add(searchPanel, BorderLayout.LINE_START);
		
		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);
		
		JButton searchBtn = new JButton("Шукати");
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		JButton insertReaderBtn = new JButton("Повернути");
		actionPanel.add(insertReaderBtn);

		debtorPanel.add(controlPanel, BorderLayout.NORTH);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout());

        String[][] data = { 
            { "1", "Пурій", "C++ for beginners", "1", "14.01.2019" },
        };

        String[] columnNames = { "ID", "Прізвище", "Видання", "Примірник", "Видано" }; 

		readerDebtorTable = new JTable(data, columnNames);
		readerDebtorTable.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(readerDebtorTable);
		listPanel.add(scrollPane);

		debtorPanel.add(listPanel, BorderLayout.CENTER);

		return debtorPanel;
	}
	
	JComponent makeReaderPanel() {
		JPanel readerPanel = new JPanel(false);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JComponent panel1 = makeReaderReaderPanel();
		tabbedPane.addTab("Всі читачі", panel1);
		JComponent panel2 = makeReaderDebtorPanel();
		tabbedPane.addTab("Боржники", panel2);

		readerPanel.setLayout(new GridLayout(1, 1));
		readerPanel.add(tabbedPane);

		return readerPanel;
	}
	
	JComponent makeBookPanel() {
		JPanel bookPanel = new JPanel(false);
		bookPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		JPanel searchPanel = new JPanel();
		controlPanel.add(searchPanel, BorderLayout.LINE_START);
		
		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);
		
		JButton searchBtn = new JButton("Шукати");
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);
		
		JButton insertCopyBtn = new JButton("Додати примірники");
		actionPanel.add(insertCopyBtn);

		JButton insertBookBtn = new JButton("Додати");
		insertBookBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new InsertBookWindow();
			}
			
		});
		actionPanel.add(insertBookBtn);

		bookPanel.add(controlPanel, BorderLayout.NORTH);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout());

        String[][] data = { 
            { "1", "11.08.2018", "C++ for Beginners", "посібник", "255", "ні" },
        };

        String[] columnNames = { "ID", "Дата", "Назва", "Тип", "Об'єм", "Електронна копія" }; 

		bookTable = new JTable(data, columnNames);
		bookTable.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(bookTable);
		listPanel.add(scrollPane);

		bookPanel.add(listPanel, BorderLayout.CENTER);

		return bookPanel;
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(900, 600);
		frame.setTitle("Бібліотека");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JComponent panel1 = makeReaderPanel();
		tabbedPane.addTab("Читачі", panel1);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeBookPanel();
		tabbedPane.addTab("Книжки", panel2);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	}
	
	public void fillReaderReaderTable() {
		readerReaderEditBtn.setVisible(false);
		readerReaderTakeCopyBtn.setVisible(false);
		
		DefaultTableModel readerReaderTableModel = (DefaultTableModel) readerReaderTable.getModel();
		readerReaderTableModel.setRowCount(0);

		ArrayList<Reader> readers = db.getReaders(readerReaderSearchQuery);

		for (Reader reader: readers) {
			String[] tableRow = new String[4];
        	tableRow[0] = Integer.toString(reader.getReaderId());
        	tableRow[1] = reader.getLastName();
        	tableRow[2] = reader.getPhoneNumber();
        	tableRow[3] = Integer.toString(reader.getBonusPoints());
        	readerReaderTableModel.addRow(tableRow);
		}
		
		readerReaderTable.setModel(readerReaderTableModel);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibraryWindow window = new LibraryWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
