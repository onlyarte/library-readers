package models;

import java.util.ArrayList;

public class Series {
    private int seriesId;
    private String dateOfPublication;
    private String title;
    private int numberOfPublications;
    private String publicationNames;
    private int hasElectronicCopy;

    public Series(int seriesId, String dateOfPublication, String title, int numberOfPublications, String publicationNames, int hasElectronicCopy) {
        this.seriesId = seriesId;
        this.dateOfPublication = dateOfPublication;
        this.title = title;
        this.numberOfPublications = numberOfPublications;
        this.publicationNames = publicationNames;
        this.hasElectronicCopy = hasElectronicCopy;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfPublications() {
        return numberOfPublications;
    }

    public void setNumberOfPublications(int numberOfPublications) {
        this.numberOfPublications = numberOfPublications;
    }

    public String getPublicationNames() {
        return publicationNames;
    }


    public int getHasElectronicCopy() {
        return hasElectronicCopy;
    }

    public void setHasElectronicCopy(int hasElectronicCopy) {
        this.hasElectronicCopy = hasElectronicCopy;
    }
}
