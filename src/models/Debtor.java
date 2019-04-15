package models;

public class Debtor {
	private int readingId;
	private int readerId;
	private String readerLastName;
	private String editionTitle;
	private int editionCopyId;
	private String dateReceived;
	
	public Debtor(int readingId, int readerId, String readerLastName, String editionTitle, int editionCopyId, String dateReceived) {
		this.readingId = readingId;
		this.readerId = readerId;
		this.readerLastName = readerLastName;
		this.editionTitle = editionTitle;
		this.editionCopyId = editionCopyId;
		this.dateReceived = dateReceived;
	}
	
	public int getReadingId() { return readingId; }
	public int getReaderId() { return readerId; }
	public String getReaderLastName() { return readerLastName; }
	public String getEditionTitle() { return editionTitle; }
	public int getEditionCopyId() { return editionCopyId; }
	public String getDateReceived() { return dateReceived; }
}
