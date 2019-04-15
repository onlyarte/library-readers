package dao;

public class SqlQueries {

    public static String GetBooksQuery = "SELECT T1.bookId, dateOfPublication, title, type, size, hasElectronicCopy\n" +
            "FROM Books T1\n" +
            "\tINNER JOIN Editions T2 ON T1.editionId = T2.editionId\n" +
            "    INNER JOIN Publications T3 ON T1.bookId = T3.bookId";
}
