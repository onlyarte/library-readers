package dao;

import models.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DbAccess {
	private static String DB_URL = "";
	private static  String DB_USER = "";
	private static  String DB_PASSWD = "";

	private static String databasePropPath = "./database.properties";

    private Connection connection = null;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;

    public DbAccess() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initDatabaseCredentials();
        } catch (Exception ex) {
            System.out.println("dao.DbAccess> " + ex.getMessage());
        }
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            return true;
        } catch (SQLException e) {
            System.out.println("connect> " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            System.out.println("disconnect> " + e.getMessage());
        }
    }
    
    private int getNextId(String table, String idFieldName) {
    	int i = -1;
    	try {
    		sql = "SELECT MAX(" + idFieldName + ") FROM " + table;
            statement.execute(sql);
            resultSet = statement.getResultSet();
            if ((resultSet != null) && (resultSet.next())) i = resultSet.getInt(1);
    	} catch (SQLException e) {
            System.out.println("getNextId> " + e.getMessage());
        }
    	return i + 1;
    }
    
    public Reader getReader(int readerId) {
    	Reader reader = null;
    	try {
            sql = "SELECT readerId, lastName, phoneNumber, bonusPoints FROM Readers WHERE readerId = " + readerId;
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            if ((resultSet != null) && (resultSet.next())) {
            	reader = new Reader(
            			resultSet.getInt(1),
            			resultSet.getString(2),
            			resultSet.getString(3),
            			resultSet.getInt(4));
            }
        } catch (SQLException e) {
            System.out.println("findReaderById>" + e.getMessage());
        }
        return reader;
    }

    public ArrayList<Reader> getReaders(String query) {
    	ArrayList<Reader> readers = new ArrayList<Reader>();
    	try {
    		sql = "SELECT readerId, lastName, phoneNumber, bonusPoints FROM Readers";
    		if (query != null && !query.equals("")) {
    			sql += " WHERE lastName LIKE '%" + query + "%' OR phoneNumber LIKE '%" + query + "%'";
    		}
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				readers.add(new Reader(
    						resultSet.getInt(1),
    						resultSet.getString(2),
    						resultSet.getString(3),
    						resultSet.getInt(4)));
    			}
    		}
    	} catch (SQLException e) {
            System.out.println("findReaders> " + e.getMessage());
        }
    	return readers;
    }
    
    private boolean isStringNumeric(String str) {  
      try {  
        Double.parseDouble(str);  
      } catch(NumberFormatException nfe) {  
        return false;  
      }  
      return true;  
    }
    
    public ArrayList<Debtor> getDebtors(String query) {
    	ArrayList<Debtor> debtors = new ArrayList<Debtor>();
    	try {
    		sql = "SELECT Readings.readingId, Readers.readerId, Readers.lastName AS readerLastName, IFNULL(Series.title, Publications.title) AS title, EditionCopies.editionCopyId, Readings.dateReceived "
				+ "FROM Readings "
					+ "INNER JOIN Readers ON Readings.readerId = Readers.readerId "
					+ "INNER JOIN EditionCopies ON Readings.editionCopyId = EditionCopies.editionCopyId "
					+ "INNER JOIN Editions ON EditionCopies.editionId = Editions.editionId "
					+ "LEFT OUTER JOIN Series ON Editions.editionId = Series.editionId "
					+ "LEFT OUTER JOIN Books ON Editions.editionId = Books.bookId "
					+ "LEFT OUTER JOIN Publications ON Books.bookId = Publications.bookId "
				+ "WHERE Readings.dateReturned IS NULL ";
    		if (query != null && !query.equals("")) {
    			if (isStringNumeric(query)) {
    				sql += " AND YEAR(dateReceived) = " + query;
    			} else {
    				sql += " AND (R.lastName LIKE '%" + query + "%' OR B.title LIKE '%" + query + "%')";
    			}
    		}
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				debtors.add(new Debtor(
    						resultSet.getInt(1),
    						resultSet.getInt(2),
    						resultSet.getString(3),
    						resultSet.getString(4),
    						resultSet.getInt(5),
    						resultSet.getString(6)));
    			}
    		}
    	} catch (SQLException e) {
            System.out.println("findDebtors> " + e.getMessage());
        }
    	return debtors;
    }

    public int insertReader(String lastName, String phoneNumber, int bonusPoints) {
    	int readerId = getNextId("Readers", "readerId");
    	int rows = 0;
        try {
        	preparedStatement = connection.prepareStatement(
        		"INSERT INTO Readers (readerId, lastName, phoneNumber, bonusPoints) VALUES(" + readerId + ",?,?,?)"
        	);
        	preparedStatement.setString(1, lastName);
        	preparedStatement.setString(2, phoneNumber);
        	preparedStatement.setInt(3, bonusPoints);
            rows = preparedStatement.executeUpdate();
            if (rows == 0) readerId = 0;
        } catch (SQLException e) {
            System.out.println("insertReader> " + e.getMessage());
        }
        return readerId;
    }
    
    public void updateReader(int readerId, String lastName, String phoneNumber, int bonusPoints) {
    	try {
    		sql = "UPDATE Readers SET";
    		boolean updatesDefined = false;
    		if (lastName != null) {
    			sql += " lastName = '" + lastName + "'";
    			updatesDefined = true;
    		}
    		if (phoneNumber != null) {
    			if (updatesDefined) sql += ",";
    			sql += " phoneNumber = '" + phoneNumber + "'";
    			updatesDefined = true;
    		}
    		if (bonusPoints > -1) {
    			if (updatesDefined) sql += ",";
    			sql += " bonusPoints = " + bonusPoints;
    			updatesDefined = true;
    		}
    		sql += " WHERE readerId = " + readerId;
    		if (updatesDefined) {
    			statement.executeUpdate(sql);
    		}
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
    	}
    }

    public void deleteReader(int readerId) {
    	try {
    		sql = "delete from Readers where readerId = " + readerId;
    		statement.executeUpdate(sql);
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList<Book> getBooks(String query) {
    	ArrayList<Book> books = new ArrayList<Book>();
    	try {
    		sql = SqlQueries.GetBooksQuery;
    		if (query != null && !query.equals("")) {
    			sql += " WHERE title LIKE '%" + query + "%'";
    		}
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				books.add(new Book(
    						resultSet.getInt("bookId"),
    						resultSet.getString("dateOfPublication"),
							resultSet.getString("title"),
    						resultSet.getString("type"),
    						resultSet.getInt("size"),
    						resultSet.getInt("hasElectronicCopy")));
    			}
    		}
    	} catch (SQLException e) {
            System.out.println("getBooks> " + e.getMessage());
        }
    	return books;
    }

    public ArrayList<Integer> getFreeEditionCopies(int bookId) {
    	ArrayList<Integer> freeCopies = new ArrayList<Integer>();
    	try {
    		sql = "SELECT EC.editionCopyId "
				+ "FROM Books B "
					+ "INNER JOIN Editions E ON B.editionId = E.editionId "
					+ "INNER JOIN EditionCopies EC ON E.editionId = EC.editionId "
				+ "WHERE B.bookId = " + bookId + " AND EC.editionCopyId NOT IN ("
					+ "SELECT EC1.editionCopyId "
					+ "FROM EditionCopies EC1 INNER JOIN Readings R1 ON EC1.editionCopyId = R1.editionCopyId "
					+ "WHERE R1.dateReturned IS NULL"
				+ ")";
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				freeCopies.add(resultSet.getInt(1));
    			}
    		}
    	}catch (SQLException e) {
            System.out.println("findReaders> " + e.getMessage());
        }
    	return freeCopies;
    }

    public int insertBook(String dateOfPublication, String title, String type, int size, int hasElectronicCopy, int numberOfCopies) {
    	int editionId = getNextId("Editions", "editionId");
    	int bookId = getNextId("Books", "bookId");
    	int rows = 0;
        try {
        	preparedStatement = connection.prepareStatement(
        		"INSERT INTO Editions (editionId, dateOfPublication, hasElectronicCopy) VALUES(?,?,?)"
        	);
        	preparedStatement.setInt(1, editionId);
        	preparedStatement.setString(2, dateOfPublication);
        	preparedStatement.setInt(3, hasElectronicCopy);
            rows = preparedStatement.executeUpdate();
            if (rows == 0) editionId = 0;
            rows = 0;

            preparedStatement = connection.prepareStatement(
        		"INSERT INTO Books (bookId, title, type, size, editionId) VALUES(?,?,?,?,?)"
        	);
        	preparedStatement.setInt(1, bookId);
        	preparedStatement.setString(2, title);
        	preparedStatement.setString(3, type);
        	preparedStatement.setInt(4, size);
        	preparedStatement.setInt(5, editionId);
            rows = preparedStatement.executeUpdate();
            if (rows == 0) bookId = 0;

            for (int i = 0; i < numberOfCopies; i++) {
            	preparedStatement = connection.prepareStatement(
            		"INSERT INTO EditionCopies (editionId) VALUES(?)"
            	);
            	preparedStatement.setInt(1, editionId);
            	preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("insertBook> " + e.getMessage());
        }
        return bookId;
    }
    
    public int insertReading(int readerId, int editionCopyId) {
    	int readingId = getNextId("Readings", "readingId");
    	int rows = 0;
        try {
        	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	preparedStatement = connection.prepareStatement(
        		"INSERT INTO Readings (readingId, readerId, editionCopyId, dateReceived) VALUES(?,?,?,?)"
        	);
        	preparedStatement.setInt(1, readingId);
        	preparedStatement.setInt(2, readerId);
        	preparedStatement.setInt(3, editionCopyId);
        	preparedStatement.setString(4, date);
            rows = preparedStatement.executeUpdate();
            if (rows == 0) readerId = 0;
        } catch (SQLException e) {
            System.out.println("insertReading> " + e.getMessage());
        }
        return readingId;
    }
    
    public void setReadingReturnDate(int readingId) {
    	try {
    		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    		sql = "UPDATE Readings SET dateReturned = '" + date + "' WHERE readingId = " + readingId;
    		statement.executeUpdate(sql);
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
    	}
    }

	private void initDatabaseCredentials(){
		BufferedReader reader=null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(databasePropPath);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line = reader.readLine();
			while (line != null) {
				int index = line.indexOf('=')+1;
				if(line.contains("DB_URL"))
					DB_URL = line.substring(index, line.length());

				if(line.contains("user"))
					DB_USER = line.substring(index, line.length());

				if(line.contains("password"))
					DB_PASSWD = line.substring(index, line.length());

				// read next line
				line = reader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try{
				reader.close();
				fis.close();
			}catch (Exception ex){

			}

		}
	}
}