package models;

public class Author {
    private int authorId;
    private String fullName;

    public Author(int authorId, String fullName) {
        this.authorId = authorId;
        this.fullName = fullName;
    }

    public int getAuthorId() { return authorId; }
    public String getFullName() { return fullName; }
}
