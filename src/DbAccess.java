import java.sql.*;
import java.util.ArrayList;

public class DbAccess {
    private Connection connection = null;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;

    DbAccess() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("DbAccess> " + ex.getMessage());
        }
    }

    public boolean connect(String db, String user, String passwd) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?useSSL=false&allowPublicKeyRetrieval=true", user, passwd);
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
    		sql = "SELECT bookId, dateOfPublication, title, type, size, hasElectronicCopy FROM Books T1 "
				+ "INNER JOIN Editions T2 ON T1.editionId = T2.editionId";
    		if (query != null && !query.equals("")) {
    			sql += " WHERE title LIKE '%" + query + "%'";
    		}
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				books.add(new Book(
    						resultSet.getInt(1),
    						resultSet.getString(2),
    						resultSet.getString(3),
    						resultSet.getString(4),
    						resultSet.getInt(5),
    						resultSet.getInt(6)));
    			}
    		}
    	} catch (SQLException e) {
            System.out.println("findReaders> " + e.getMessage());
        }
    	return books;
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
}