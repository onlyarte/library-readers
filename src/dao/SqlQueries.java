package dao;

public class SqlQueries {

    public static String GetBooksQuery = "SELECT T1.bookId, dateOfPublication, topic, title, type, size, hasElectronicCopy\n" +
            "FROM Books T1\n" +
            "\tINNER JOIN Editions T2 ON T1.editionId = T2.editionId\n" +
            "    INNER JOIN Publications T3 ON T1.bookId = T3.bookId\n" +
            "    INNER JOIN EditionTopics T4 ON T1.editionId = T4.editionId";

    public static String GetSeriesQuery = "SELECT T1.seriesId, dateOfPublication, T1.title as SeriesTitle, T3.title as PublicationTitle, numberOfBooks, hasElectronicCopy\n" +
            "from Series T1\n" +
            "INNER JOIN Editions T2 ON T1.editionId = T2.editionId\n" +
            "INNER JOIN Publications T3 ON T1.seriesId = T3.seriesId\n" +
            "INNER JOIN EditionTopics T4 ON T1.editionId = T4.editionId\n";

    public static String GetAuthorsQuery = "SELECT * FROM Authors";

    public static String GetAuthorsOfPublication = "SELECT name FROM " +
            "Authors INNER JOIN PublicationAuthors ON Authors.AuthorId = PublicationAuthors.AuthorId ";

    public static String GetPublicationOfBook = "SELECT publicationId FROM Publications ";

    public static String GetPublications = "SELECT * FROM Publications ";

    public static String GetKeywords = "SELECT keyWord FROM PublicationKeyWords ";
}
