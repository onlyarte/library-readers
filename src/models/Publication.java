package models;

public class Publication {
    private int publicationId;
    private String title;
    private boolean isBookSeries;

    public Publication(int publicationId, String title, boolean isBookSeries) {
        this.publicationId = publicationId;
        this.title = title;
        this.isBookSeries = isBookSeries;
    }

    public int getPublicationId() { return publicationId; }
    public String getTitle() { return title; }
    public boolean isBookSeries() { return isBookSeries; }
}
