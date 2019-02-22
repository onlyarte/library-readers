
public class Book {
	private int bookId;
	private String dateOfPublication;
	private String title;
	private String type;
	private int size;
	private int hasElectronicCopy;
	
	Book(int bookId, String dateOfPublication, String title, String type, int size, int hasElectronicCopy) {
		this.bookId = bookId;
		this.dateOfPublication = dateOfPublication;
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
}
