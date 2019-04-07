
public class Reader {
	private int readerId;
	private String lastName;
	private String phoneNumber;
	private int bonusPoints;
	
	Reader(int readerId, String lastName, String phoneNumber, int bonusPoints) {
		this.readerId = readerId;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.bonusPoints = bonusPoints;
	}
	
	public int getReaderId() { return readerId; }
	public String getLastName() { return lastName; }
	public String getPhoneNumber() { return phoneNumber; }
	public int getBonusPoints() { return bonusPoints; }
}
