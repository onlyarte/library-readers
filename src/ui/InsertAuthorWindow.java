package ui;

import dao.DbAccess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertAuthorWindow {
    private JFrame frame;
    private LibraryWindow mainWindow;
    private DbAccess db;

    public InsertAuthorWindow(LibraryWindow mainWindow, DbAccess db) {
        this.mainWindow = mainWindow;
        this.db = db;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Додати автора");
        frame.setSize(400, 160);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel message = new JLabel();
        message.setForeground(Color.red);

        JLabel surnameLbl = new JLabel("Прізвище:");
        panel.add(surnameLbl);

        JTextField fullNameField = new JTextField();
        panel.add(fullNameField);

        panel.add(message);

        JButton submitBtn = new JButton("Додати");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fullName = fullNameField.getText();

                    if (fullName.length() < 2) throw new Exception("Ім'я має мати щонайменше 2 символи");

                    db.insertAuthor(fullName);

                    message.setText("Додано!");

                    fullNameField.setEditable(false);
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
