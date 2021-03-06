package models;

public class Book {
	private int bookId;
	private String dateOfPublication;
	private String topic;
	private String title;
	private String type;
	private int size;
	private int hasElectronicCopy;
	
	public Book(int bookId, String dateOfPublication, String topic, String title, String type, int size, int hasElectronicCopy) {
		this.bookId = bookId;
		this.dateOfPublication = dateOfPublication;
		this.topic = topic;
		this.title = title;
		this.type = type;
		this.size = size;
		this.hasElectronicCopy = hasElectronicCopy;
	}
	
	public int getBookId() { return bookId; }
	public String getDateOfPublication() { return dateOfPublication; }
	public String getTitle() { return title; }
	public String getType() { return type; }
	public int getSize() { return size; }
	public int getHasElectronicCopy() { return hasElectronicCopy; }
	public String getTopic() {return topic;}
}
