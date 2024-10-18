package model;

public class Adherent {
	private int id;
	private String firstName;
	private String lastName;
	private String adresse;
	private int books;
	private int cds;
	private int dvds;
	
	public Adherent(int id, String firstName, String lastName, String adresse, int books, int cds,
			int dvds) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.adresse = adresse;
		this.books = books;
		this.cds = cds;
		this.dvds = dvds;
	}
	
	//getters && setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public int getBooks() {
		return books;
	}
	public void setBooks(int books) {
		this.books = books;
	}
	public int getCds() {
		return cds;
	}
	public void setCds(int cds) {
		this.cds = cds;
	}
	public int getDvds() {
		return dvds;
	}
	public void setDvds(int dvds) {
		this.dvds = dvds;
	}

	
	

}
