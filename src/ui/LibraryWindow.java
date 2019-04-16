package ui;
import dao.DbAccess;
import models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LibraryWindow {
	private LibraryWindow window;
	private JFrame frame;
	private DbAccess db;
	
	private JTable readerReaderTable;
	private JTable readerDebtorTable;
	private JTable bookTable;
	private JTable seriesTable;
	private JTable authorTable;
	private JTable searchTable;
	
	private JButton readerReaderEditBtn;
	private JButton readerReaderTakeCopyBtn;
	
	private JButton readerDebtorReturnCopyBtn;

	private String readerReaderSearchQuery;
	private String readerDebtorSearchQuery;

	public LibraryWindow() {
		window = this;

		initialize();

		db = new DbAccess();
		db.connect();

		fillReaderReaderTable();
		fillReaderDebtorTable();
		fillBookTable("");
		fillSeriesTable("");
		fillAuthorsTable("");
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
//				int selectedReaderId = Integer.parseInt(readerReaderTable.getValueAt(readerReaderTable.getSelectedRow(), 0).toString());
//				new ui.TakeBookWindow(window, db, selectedReaderId);
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
		JPanel readerDebtorPanel = new JPanel(false);
		readerDebtorPanel.setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());

		JPanel searchPanel = new JPanel();
		controlPanel.add(searchPanel, BorderLayout.LINE_START);

		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);

		JButton searchBtn = new JButton("Шукати");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readerDebtorSearchQuery = searchField.getText();
				fillReaderDebtorTable();
			}
		});
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		readerDebtorReturnCopyBtn = new JButton("Повернути");
		readerDebtorReturnCopyBtn.setVisible(false);
		readerDebtorReturnCopyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int selectedReadingId = Integer.parseInt(readerDebtorTable.getValueAt(readerDebtorTable.getSelectedRow(), 0).toString());
//				db.setReadingReturnDate(selectedReadingId);
//				fillReaderDebtorTable();
			}
		});
		actionPanel.add(readerDebtorReturnCopyBtn);

		readerDebtorPanel.add(controlPanel, BorderLayout.NORTH);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout());

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());

		readerDebtorTable = new JTable();
		readerDebtorTable.setFillsViewportHeight(true);
		readerDebtorTable.setDefaultEditor(Object.class, null);
		readerDebtorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[] columnNames = { "ID транзакції", "ID читача", "Прізвище", "Видання", "Примірник", "Видано" };
		DefaultTableModel readerDebtorTableModel = (DefaultTableModel) readerDebtorTable.getModel();
		readerDebtorTableModel.setColumnIdentifiers(columnNames);

		readerDebtorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (readerDebtorTable.getSelectedRow() != -1) {
					readerDebtorReturnCopyBtn.setVisible(true);
				}
			}
	    });

		JScrollPane scrollPane = new JScrollPane(readerDebtorTable);
		tablePanel.add(scrollPane);

		readerDebtorPanel.add(tablePanel, BorderLayout.CENTER);

		return readerDebtorPanel;
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
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String bookSearchQuery = searchField.getText();
				fillBookTable(bookSearchQuery);
			}
		});
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		JButton insertBookBtn = new JButton("Додати");
		insertBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertBookWindow(window, db);
			}
		});
		actionPanel.add(insertBookBtn);

		bookPanel.add(controlPanel, BorderLayout.NORTH);


		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());

		bookTable = new JTable();
		bookTable.setFillsViewportHeight(true);
		bookTable.setDefaultEditor(Object.class, null);
		bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[] columnNames = { "ID", "Дата", "Тема", "Назва", "Тип", "Об'єм", "Електронна копія" };
		DefaultTableModel readerReaderTableModel = (DefaultTableModel) bookTable.getModel();
		readerReaderTableModel.setColumnIdentifiers(columnNames);

		JScrollPane scrollPane = new JScrollPane(bookTable);
		tablePanel.add(scrollPane);

		bookPanel.add(tablePanel, BorderLayout.CENTER);

		return bookPanel;
	}

	JComponent makeSeriesPanel() {
		JPanel seriesPanel = new JPanel(false);
		seriesPanel.setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());

		JPanel searchPanel = new JPanel();
		controlPanel.add(searchPanel, BorderLayout.LINE_START);

		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);

		JButton searchBtn = new JButton("Шукати");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String seriesSearchQuery = searchField.getText();
				fillSeriesTable(seriesSearchQuery);
			}
		});
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		JButton insertSeriesBtn = new JButton("Додати");
		insertSeriesBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertSeriesWindow(window, db);
			}
		});
		actionPanel.add(insertSeriesBtn);

		seriesPanel.add(controlPanel, BorderLayout.NORTH);


		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());

		seriesTable = new JTable();
		seriesTable.setFillsViewportHeight(true);
		seriesTable.setDefaultEditor(Object.class, null);
		seriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[] columnNames = { "ID", "Дата", "Назва", "Кількість публ.", "Публікації", "Електр. копія" };
		DefaultTableModel seariesTableModel = (DefaultTableModel) seriesTable.getModel();
		seariesTableModel.setColumnIdentifiers(columnNames);

		JScrollPane scrollPane = new JScrollPane(seriesTable);
		tablePanel.add(scrollPane);

		seriesPanel.add(tablePanel, BorderLayout.CENTER);

		return seriesPanel;
	}

	JComponent makeAuthorPanel() {
		JPanel authorPanel = new JPanel(false);
		authorPanel.setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());

		JPanel searchPanel = new JPanel();
		controlPanel.add(searchPanel, BorderLayout.LINE_START);

		JTextField searchField = new JTextField(20);
		searchPanel.add(searchField);

		JButton searchBtn = new JButton("Шукати");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String authorSearchQuery = searchField.getText();
				fillAuthorsTable(authorSearchQuery);
			}
		});
		searchPanel.add(searchBtn);

		JPanel actionPanel = new JPanel();
		controlPanel.add(actionPanel, BorderLayout.LINE_END);

		JButton insertBookBtn = new JButton("Додати");
		insertBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertAuthorWindow(window, db);
			}
		});
		actionPanel.add(insertBookBtn);

		authorPanel.add(controlPanel, BorderLayout.NORTH);


		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());

		authorTable = new JTable();
		authorTable.setFillsViewportHeight(true);
		authorTable.setDefaultEditor(Object.class, null);
		authorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[] columnNames = { "ID", "Повне ім'я" };
		DefaultTableModel readerReaderTableModel = (DefaultTableModel) authorTable.getModel();
		readerReaderTableModel.setColumnIdentifiers(columnNames);

		JScrollPane scrollPane = new JScrollPane(authorTable);
		tablePanel.add(scrollPane);

		authorPanel.add(tablePanel, BorderLayout.CENTER);

		return authorPanel;
	}

	JComponent makeSearchPanel() {
		JPanel searchPanel = new JPanel(false);
		searchPanel.setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(7,2));
		controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel authorLbl = new JLabel("Автор:");
		controlPanel.add(authorLbl);

		JTextField authorField = new JTextField();
		controlPanel.add(authorField);

		JLabel topicLbl = new JLabel("Тема:");
		controlPanel.add(topicLbl);

		JTextField topicField = new JTextField();
		controlPanel.add(topicField);

		JLabel typeLbl = new JLabel("Тип:");
		controlPanel.add(typeLbl);

		JTextField typeField = new JTextField();
		controlPanel.add(typeField);

		JLabel keyWordLbl = new JLabel("Ключове слово:");
		controlPanel.add(keyWordLbl);

		JTextField keyWordField = new JTextField();
		controlPanel.add(keyWordField);

		JLabel fromLbl = new JLabel("З:");
		controlPanel.add(fromLbl);
		DateFormatter dateFormatter=new DateFormatter(new SimpleDateFormat("yyyy-mm-dd"));

		JFormattedTextField fromField = new JFormattedTextField(dateFormatter);
		controlPanel.add(fromField);

		JLabel toLbl = new JLabel("До:");
		controlPanel.add(toLbl);

		JFormattedTextField toField= new JFormattedTextField(dateFormatter);
		controlPanel.add(toField);

		JButton searchBtn = new JButton("Шукати");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList id=null;
				try {
					ArrayList name=new ArrayList();
					name=db.searchData(authorField.getText(),keyWordField.getText(),topicField.getText(),typeField.getText(),fromField.getText(),toField.getText());
					if(name==null)JOptionPane.showMessageDialog(null, "Your data is wrong!");
					else{
						id= db.getResultOfSearch(name);
						ArrayList oneRow=new ArrayList();
						ArrayList<ArrayList> all=new ArrayList<ArrayList>();
						for(int i=0;i<name.size();i++){
							oneRow=new ArrayList();
							oneRow.add(id.get(i));
							oneRow.add(name.get(i));

							all.add(oneRow);

						}


						String[] names={ "ID","Назва" };
						searchPanel.add(addTable(names,all), BorderLayout.CENTER);
					}
				} catch (SQLException e1) {

					e1.printStackTrace();
				}

			}
		});
		controlPanel.add(searchBtn);
		JButton rateBtn = new JButton("Рейтинг");
		rateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ArrayList> id;
				try {
					id = db.getRating();
					String[] names={ "ID","Назва","Кількість","Дата" };
					searchPanel.add(addTable(names,id), BorderLayout.CENTER);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
		controlPanel.add(rateBtn);

		searchPanel.add(controlPanel, BorderLayout.NORTH);



		return searchPanel;
	}
	private JPanel addTable(String[] columnNames,ArrayList<ArrayList> data){
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());
		tablePanel.setVisible(true);
		searchTable = new JTable();
		searchTable.setFillsViewportHeight(true);
		searchTable.setDefaultEditor(Object.class, null);
		searchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		DefaultTableModel searchTableModel = (DefaultTableModel) searchTable.getModel();
		searchTableModel.setColumnIdentifiers(columnNames);
	/*	for(int i=0;i<data.size();i++) {
			searchTableModel.addRow((data.get(i).toArray()));
		}

	*/
			searchTableModel.setRowCount(0);

			for (int i=0;i<data.size();i++) {
				String[] tableRow = new String[columnNames.length];
				tableRow[0] = Integer.toString((Integer) data.get(i).get(0));
				for(int s=1;s<columnNames.length;s++){
					if(s==2 )tableRow[s] = Integer.toString((Integer) data.get(i).get(s));
					else if(s==3)tableRow[s] = ((Date) data.get(i).get(s)).toString();
				else tableRow[s] = (String)data.get(i).get(s);}
				searchTableModel.addRow(tableRow);
			}

			searchTable.setModel(searchTableModel);
		JScrollPane scrollPane = new JScrollPane(searchTable);
		tablePanel.add(scrollPane);
		return tablePanel;
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

		JComponent panel3 = makeSeriesPanel();
		tabbedPane.addTab("Збірники", panel3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent panel4 = makeAuthorPanel();
		tabbedPane.addTab("Автори", panel4);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_4);
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

	public void fillReaderDebtorTable() {
		readerDebtorReturnCopyBtn.setVisible(false);

		DefaultTableModel readerDebtorTableModel = (DefaultTableModel) readerDebtorTable.getModel();
		readerDebtorTableModel.setRowCount(0);

		ArrayList<Debtor> debtors = db.getDebtors(readerDebtorSearchQuery);
		// { "ID", "Прізвище", "Видання", "Примірник", "Видано" }

		for (Debtor debtor: debtors) {
			String[] tableRow = new String[6];
			tableRow[0] = Integer.toString(debtor.getReadingId());
        	tableRow[1] = Integer.toString(debtor.getReaderId());
        	tableRow[2] = debtor.getReaderLastName();
        	tableRow[3] = debtor.getEditionTitle();
        	tableRow[4] = Integer.toString(debtor.getEditionCopyId());
        	tableRow[5] = debtor.getDateReceived();
        	readerDebtorTableModel.addRow(tableRow);
		}
		
		readerDebtorTable.setModel(readerDebtorTableModel);
	}
	
	public void fillBookTable(String bookSearchQuery) {
		DefaultTableModel bookTableModel = (DefaultTableModel) bookTable.getModel();
		bookTableModel.setRowCount(0);

		ArrayList<models.Book> books = db.getBooks(bookSearchQuery);

		for (models.Book book: books) {
			String[] tableRow = new String[7];
        	tableRow[0] = Integer.toString(book.getBookId());
        	tableRow[1] = book.getDateOfPublication();
			tableRow[2] = book.getTopic();
        	tableRow[3] = book.getTitle();
        	tableRow[4] = book.getType();
        	tableRow[5] = Integer.toString(book.getSize());
        	tableRow[6] = book.getHasElectronicCopy() == 1 ? "Так" : "Ні";
        	bookTableModel.addRow(tableRow);
		}

		bookTable.setModel(bookTableModel);
	}

	public void fillSeriesTable(String seriesSearchQuery) {
		DefaultTableModel seriesTableModel = (DefaultTableModel) seriesTable.getModel();
		seriesTableModel.setRowCount(0);

		ArrayList<Series> series = db.getSeries(seriesSearchQuery);

		for (Series series1: series) {
			String[] tableRow = new String[6];
			tableRow[0] = Integer.toString(series1.getSeriesId());
			tableRow[1] = series1.getDateOfPublication();
			tableRow[2] = series1.getTitle();
			tableRow[3] = Integer.toString(series1.getNumberOfPublications());
			tableRow[4] = series1.getPublicationNames();
			tableRow[5] = series1.getHasElectronicCopy() == 1 ? "Так" : "Ні";
			seriesTableModel.addRow(tableRow);
		}

		seriesTable.setModel(seriesTableModel);
	}

	public void fillAuthorsTable(String authorSearchQuery) {
		DefaultTableModel authorsTableModel = (DefaultTableModel) authorTable.getModel();
		authorsTableModel.setRowCount(0);

		ArrayList<Author> authors = db.getAuthors(authorSearchQuery);

		for (Author author: authors) {
			String[] tableRow = new String[6];
			tableRow[0] = Integer.toString(author.getAuthorId());
			tableRow[1] = author.getFullName();
			authorsTableModel.addRow(tableRow);
		}

		authorTable.setModel(authorsTableModel);
	}
}
