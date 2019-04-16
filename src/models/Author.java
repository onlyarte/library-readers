package models;

public class Author {
    private int authorId;
    private String surname;

    public Author(int authorId, String surname) {
        this.authorId = authorId;
        this.surname = surname;
    }

    public int getAuthorId() { return authorId; }
    public String getSurname() { return surname; }
}
