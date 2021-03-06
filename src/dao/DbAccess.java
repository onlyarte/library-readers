package dao;

import models.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbAccess {
	private static String DB_URL = "";
	private static String DB_USER = "";
	private static String DB_PASSWD = "";

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
    				sql += " AND (Readers.lastName LIKE '%" + query + "%' OR Publications.title LIKE '%" + query + "%' OR Series.title LIKE '%" + query + "%')";
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

    public int getPublicationIdOfBook(int bookId) {
    	int pubId = 0;
    	try {
			preparedStatement = connection.prepareStatement(SqlQueries.GetPublicationOfBook
					+ " WHERE bookId = " + bookId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("publicationId");
			}
		} catch (SQLException e) {
			System.out.println("getPublicationsOfBook> " + e.getMessage());
		}
    	return pubId;
	}
    
    public ArrayList<Book> getBooks(String query) {
    	ArrayList<Book> books = new ArrayList<Book>();
    	try {
    		sql = SqlQueries.GetBooksQuery;
    		if (query != null && !query.equals("")) {
    			sql += " WHERE title LIKE '%" + query + "%' OR topic like '%"+ query +"%' OR type like '%"+ query+"%'";
    		}
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				books.add(new Book(
    						resultSet.getInt("bookId"),
    						resultSet.getString("dateOfPublication"),
							resultSet.getString("topic"),
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

	public ArrayList<Series> getSeries(String query) {
		ArrayList<Series> series = new ArrayList<Series>();
		try {
			sql = SqlQueries.GetSeriesQuery;
			if (query != null && !query.equals("")) {
				sql += " WHERE SeriesTitle LIKE '%" + query + "%' OR PublicationTitle like '%"+ query +"%'\n" +
				"order by T1.seriesId";
			}
			statement.execute(sql);
			ResultSet resultSet = statement.getResultSet();
			if (resultSet != null) {
				int previousSeriesID = -1;
				while (resultSet.next()) {
					Series series1 = new Series(resultSet.getInt("seriesId"),
							resultSet.getString("dateOfPublication"),
							resultSet.getString("SeriesTitle"),
							resultSet.getInt("numberOfBooks"),
							resultSet.getString("PublicationTitle"),
							resultSet.getInt("hasElectronicCopy"));

					series.add(series1);
				}
			}
		} catch (SQLException e) {
			System.out.println("getBooks> " + e.getMessage());
		}
		return series;
	}

	public ArrayList<Author> getAuthors(String query) {
		ArrayList<Author> authors = new ArrayList<Author>();
		try {
			sql = SqlQueries.GetAuthorsQuery;
			if (query != null && !query.equals("")) {
				sql += " WHERE name LIKE '%" + query + "%'";
			}
			statement.execute(sql);
			ResultSet resultSet = statement.getResultSet();
			if (resultSet != null) {
				while (resultSet.next()) {
					authors.add(new Author(
							resultSet.getInt("authorId"),
							resultSet.getString("name")));
				}
			}
		} catch (SQLException e) {
			System.out.println("getAuthors> " + e.getMessage());
		}
		return authors;
	}

	public ArrayList<Author> getAuthorsOfPublication(int pubId) {
		ArrayList<Author> authors = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(SqlQueries.GetAuthorsOfPublication
					+ "WHERE PublicationId = " + pubId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					authors.add(new Author(0, resultSet.getString("name")));
				}
			}
			return authors;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Publication> getPublications(String query) {
    	ArrayList<Publication> publications = new ArrayList<>();
    	try {
    		sql = SqlQueries.GetPublications;
			if (query != null && !query.equals("")) {
				sql += " WHERE name LIKE '%" + query + "%'";
			}
			statement.executeQuery(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if (resultSet != null) {
    			while (resultSet.next()) {
    				publications.add(new Publication(
    						resultSet.getInt("publicationId"),
							resultSet.getString("title"),
							resultSet.getObject("seriesId") != null
					));
				}
			}
		} catch (SQLException e) {
    		e.printStackTrace();
		}
    	return publications;
	}

	public String getKeywords(int publicationId) {
    	String keywords = "";
		try {
			preparedStatement = connection.prepareStatement(SqlQueries.GetKeywords
					+ " WHERE publicationId = " + publicationId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				keywords = resultSet.getString("keyWord");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keywords;
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

    public int insertBook(String dateOfPublication, String topic, String title, String type, String keywords, String[] authors, int size, int hasElectronicCopy, int numberOfCopies) {
    	int editionId = getNextId("Editions", "editionId");
		int editionTopicId = getNextId("EditionTopics", "editionTopicId");
		int publicationId = getNextId("Publications", "publicationId");
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

            // Insert into EditionTopics
			preparedStatement = connection.prepareStatement(
					"INSERT INTO EditionTopics (editionTopicId, editionId, topic) VALUES(?,?,?)"
			);
			preparedStatement.setInt(1, editionTopicId);
			preparedStatement.setInt(2, editionId);
			preparedStatement.setString(3, topic);
			preparedStatement.executeUpdate();

			// Insert into Publications
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Publications (publicationId, title, bookId) VALUES(?,?,?)"
			);
			preparedStatement.setInt(1, publicationId);
			preparedStatement.setString(2, title);
			preparedStatement.setInt(3, bookId);
			preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(
        		"INSERT INTO Books (bookId, type, size, editionId) VALUES(?,?,?,?)"
        	);
        	preparedStatement.setInt(1, bookId);
        	preparedStatement.setString(2, type);
        	preparedStatement.setInt(3, size);
        	preparedStatement.setInt(4, editionId);
            rows = preparedStatement.executeUpdate();
            if (rows == 0) bookId = 0;

            for (int i = 0; i < numberOfCopies; i++) {
            	preparedStatement = connection.prepareStatement(
            		"INSERT INTO EditionCopies (editionId) VALUES(?)"
            	);
            	preparedStatement.setInt(1, editionId);
            	preparedStatement.executeUpdate();
            }

            for (String authorName : authors) {
            	ArrayList<Author> author = getAuthors(authorName);
            	preparedStatement = connection.prepareStatement(
            			"INSERT INTO publicationauthors (publicationId, authorId) VALUES " +
								"(" + publicationId + ", " + author.get(0).getAuthorId() + ")"
				);
            	preparedStatement.executeUpdate();
			}

            preparedStatement = connection.prepareStatement(
            		"INSERT INTO publicationkeywords (publicationId, keyWord) VALUES " +
							"(" + publicationId + ", '" + keywords + "')"
			);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertBook> " + e.getMessage());
        }
        return bookId;
    }

	public int insertSeries(String dateOfPublication, String title, String topic, int numOfPublications, String publicationsName, String publicationsKeywords, int hasElectronicCopy, int numberOfCopies) {
		int editionId = getNextId("Editions", "editionId");
		int editionTopicId = getNextId("EditionTopics", "editionTopicId");
		int publicationId = getNextId("Publications", "publicationId");
		int seriesId = getNextId("Series", "seriesId");
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

			// Insert into EditionTopics
			preparedStatement = connection.prepareStatement(
					"INSERT INTO EditionTopics (editionTopicId, editionId, topic) VALUES(?,?,?)"
			);
			preparedStatement.setInt(1, editionTopicId);
			preparedStatement.setInt(2, editionId);
			preparedStatement.setString(3, topic);
			preparedStatement.executeUpdate();

			// Insert into Publications
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Publications (publicationId, title, seriesId) VALUES(?,?,?)"
			);
			preparedStatement.setInt(1, publicationId);
			preparedStatement.setString(2, title);
			preparedStatement.setInt(3, seriesId);
			preparedStatement.executeUpdate();

			// insert into Series
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Series (seriesId, title, numberOfBooks, editionId) VALUES(?,?,?,?)"
			);
			preparedStatement.setInt(1, seriesId);
			preparedStatement.setString(2, title);
			preparedStatement.setInt(3, numOfPublications);
			preparedStatement.setInt(4, editionId);
			rows = preparedStatement.executeUpdate();
			if (rows == 0) seriesId = 0;

			for (int i = 0; i < numberOfCopies; i++) {
				preparedStatement = connection.prepareStatement(
						"INSERT INTO EditionCopies (editionId) VALUES(?)"
				);
				preparedStatement.setInt(1, editionId);
				preparedStatement.executeUpdate();
			}

//			for (String authorName : authors) {
//				ArrayList<Author> author = getAuthors(authorName);
//				preparedStatement = connection.prepareStatement(
//						"INSERT INTO PublicationAuthors (publicationId, authorId) VALUES " +
//								"(" + publicationId + ", " + author.get(0).getAuthorId() + ")"
//				);
//				preparedStatement.executeUpdate();
//			}

//			preparedStatement = connection.prepareStatement(
//					"INSERT INTO PublicationsKeyWords (publicationId, keyWord) VALUES " +
//							"(" + publicationId + ", '" + publicationsKeywords + "')"
//			);
//			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("insertBook> " + e.getMessage());
		}
		return seriesId;
	}

	public int insertAuthor(String fullName) {
		int authorId = getNextId("Authors", "authorId");
		int rows = 0;
		try {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Authors (name) VALUES (?)"
			);
			preparedStatement.setString(1, fullName);
			rows = preparedStatement.executeUpdate();
			if (rows == 0) authorId = 0;
			rows = 0;
		} catch (SQLException e) {
			System.out.println("insertBook> " + e.getMessage());
		}
		return authorId;
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
	public ArrayList getResultOfSearch(ArrayList searchRes) throws SQLException {
		ArrayList id=new ArrayList();
		if(!searchRes.isEmpty()){
			for(int i=0;i<searchRes.size();i++){
				String que="Select editionCopyId FROM editioncopies "
						+ "Where editionId IN(SELECT editionId from books"
						+ " WHERE bookId IN(SELECT bookId FROM publications WHERE title='"+(searchRes.get(i))+"'))";

				String que1="Select editionCopyId FROM editioncopies "
						+ "Where editionId IN(SELECT editionId from series"
						+ " WHERE seriesId IN(SELECT bookId FROM publications WHERE title='"+(searchRes.get(i))+"'))";
				ResultSet res=statement.executeQuery(que);
				while(res.next()){
					id.add(res.getInt(1));
					if(res.isLast())break;
				}
				res=statement.executeQuery(que1);
				while(res.next()){
					id.add(res.getInt(1));
					if(res.isLast())break;
				}
			}
			return id;}
		else
			return null;
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
	public ArrayList<ArrayList> getRating() throws SQLException{
		ArrayList result=new ArrayList();
		ArrayList result1=new ArrayList();
		String que="Select editioncopies.editionCopyId,publications.title,COUNT(readings.editionCopyId) as num,MAX(readings.dateReceived) as dates FROM editioncopies,publications,books,readings "
				+ " WHERE books.editionId=editioncopies.editionId"
				+ " AND readings.editionCopyId=editioncopies.editionCopyId "
				+ " AND publications.bookId=books.bookId "
				+ " GROUP BY readings.editionCopyId "
				+ " Order BY num desc,dates desc;";

		ResultSet res=statement.executeQuery(que);

		while(res.next()){
			result=new ArrayList();
			result.add(res.getInt(1));
			result.add(res.getString(2));
			result.add(res.getInt(3));
			result.add(res.getDate(4));
			result1.add(result);
		}
		return result1;
	}
	public ArrayList searchData(String auther,String word,String topic,String type,String dateFieldf,String dateFieldt) throws SQLException{
		ArrayList result=new ArrayList();
		ArrayList result1=new ArrayList();
		ArrayList result2=new ArrayList();
		ArrayList result3=new ArrayList();
		ArrayList result4=new ArrayList();
		ArrayList result5=new ArrayList();
		ArrayList result6=new ArrayList();
		if(!auther.isEmpty()){
			String que="Select title FROM publications "
					+ "Where publicationId IN(SELECT publicationId from publicationauthors"
					+ " WHERE publicationAuthorId IN(SELECT authorId from authors "
					+ "WHERE name='"+auther+"'))";
			resultSet=statement.executeQuery(que);
			while(resultSet.next()){
				result.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			if(result.isEmpty()){JOptionPane.showMessageDialog(null, "Your data is wrong!");
				return null;
			}
		}
		if(!word.isEmpty()){
			String que1="Select title FROM publications"
					+ " Where publicationId IN(SELECT publicationId from publicationkeywords"
					+ " WHERE keyWord='"+word+"')";
			resultSet=statement.executeQuery(que1);
			while(resultSet.next()){
				result1.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}

			if(result1.isEmpty()) {JOptionPane.showMessageDialog(null, "Your data is wrong!");
				return null;
			}}
		if(!topic.isEmpty()){String que2="Select title FROM publications"
				+" Where bookId IN(SELECT bookId from books"
				+ " WHERE editionId in (SELECT editionId from editiontopics"
				+ " WHERE topic='"+topic+"'))";
			String que21="Select title FROM publications"
					+" Where seriesId IN(SELECT seriesId from series"
					+ " WHERE editionId in (SELECT editionId from editiontopics"
					+ " WHERE topic='"+topic+"'))";
			resultSet=statement.executeQuery(que2);

			while(resultSet.next()){
				result2.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			resultSet=statement.executeQuery(que21);
			while(resultSet.next()){
				result2.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			if(result2.isEmpty()) {JOptionPane.showMessageDialog(null, "Your data is wrong!");
				return null;
			}
		}
		if(!type.isEmpty()){
			String que3="SELECT title FROM publications"
					+ " WHERE bookId IN(SELECT bookId from books"
					+ " WHERE type='"+type+"')";
			resultSet=statement.executeQuery(que3);
			while(resultSet.next()){
				result4.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			if(result4.isEmpty()){JOptionPane.showMessageDialog(null, "Your data is wrong!");
				return null;
			}
		}
		if(!dateFieldf.isEmpty()&&!dateFieldt.isEmpty()){String que4="SELECT title FROM publications "
				+ " WHERE bookId IN(SELECT bookId from books"
				+ " WHERE editionId in (SELECT editionId from editions"
				+ " WHERE dateOfPublication between '"+dateFieldf +"' and '"+dateFieldt+"'))";
			resultSet=statement.executeQuery(que4);
			while(resultSet.next()){
				result5.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			String que41="SELECT title FROM publications "
					+ " WHERE seriesId IN(SELECT seriesId from series"
					+ " WHERE editionId in (SELECT editionId from editions"
					+ " WHERE dateOfPublication between '"+dateFieldf +"' and '"+dateFieldt+"'))";
			resultSet=statement.executeQuery(que41);
			while(resultSet.next()){
				result5.add(resultSet.getString(1));
				if(resultSet.isLast())break;
			}
			if(result5.isEmpty()) {JOptionPane.showMessageDialog(null, "Your data is wrong!");
				return null;
			}
		}
		ArrayList resultAll=new ArrayList();
		if(!result.isEmpty()){
			resultAll.add(result);
		}
		if(!result1.isEmpty()){
			resultAll.add(result1);
		}
		if(!result2.isEmpty()){
			resultAll.add(result2);
		}
		if(!result3.isEmpty()){
			resultAll.add(result3);
		}
		if(!result4.isEmpty()){
			resultAll.add(result4);
		}
		if(!result5.isEmpty()){
			resultAll.add(result5);
		}
		if(!result6.isEmpty()){
			resultAll.add(result6);
		}
		result=new ArrayList();
		if(resultAll.isEmpty())return null;
		else
			return result=takeRes(resultAll);
	}
	public ArrayList takeRes(ArrayList<ArrayList> res){
		ArrayList result=new ArrayList();
		if(res.size()>1){
			for(int i=0;i<res.size();i++){
				for(int c=0;c<res.get(i).size();c++){
					boolean  isd=true;
					for(int m=0;m<res.size();m++){
						if(!res.get(m).contains(res.get(i).get(c))){
							isd=false;
							break;
						}


					}
					if(isd && !result.contains(res.get(i).get(c)))result.add(res.get(i).get(c));
				}
			}
			return result;
		}
		else return res.get(0);

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