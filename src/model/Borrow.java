package model;

import java.sql.Date;

public class Borrow {
	int id;
	String type;
	int id_livre;
	String titre;
	int id_adherent;
	String adherent;
	Date borrowDate;
	Date dueDate;
	String status;
	long sanction;
	
	public Borrow(int id, String type, int id_livre, String titre, int id_adherent, String adherent, Date borrowDate,
			Date dueDate, String status) {
		this.id = id;
		this.type = type;
		this.id_livre = id_livre;
		this.titre = titre;
		this.id_adherent = id_adherent;
		this.adherent = adherent;
		this.borrowDate = borrowDate;
		this.dueDate = dueDate;
		this.status = status;	
	}
	
	public Borrow(int id, String type, int id_livre, String titre, int id_adherent, String adherent, Date borrowDate,
			Date dueDate, String status, long sanction) {
		this(id,type, id_livre, titre, id_adherent, adherent, borrowDate, dueDate, status);
		this.sanction = sanction;
	}

	public long getSanction() {
		return sanction;
	}

	public void setSanction(long sanction) {
		this.sanction = sanction;
	}

	public int getId() {
		return id;
	}


	public String getType() {
		return type;
	}


	public int getId_livre() {
		return id_livre;
	}


	public String getTitre() {
		return titre;
	}

	public int getId_adherent() {
		return id_adherent;
	}

	
	public String getAdherent() {
		return adherent;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public String getStatus() {
		return status;
	}


	
	
	
}
