package model;

import java.sql.Blob;

public class Book {
	private Blob ImageSrc;
	private int n_livre;
	private String name;
	private String author;
	private String maisonED;
	private int n_pages;
	private float prix;
	private int copies;
	
	public Book() {
		super();
	}
	
	public Book(Blob imageSrc, int n_livre, String name, String author, String maisonED, int n_pages, float prix,
		int copies) {
		super();
		ImageSrc = imageSrc;
		this.n_livre = n_livre;
		this.name = name;
		this.author = author;
		this.maisonED = maisonED;
		this.n_pages = n_pages;
		this.prix = prix;
		this.copies = copies;
	}
	
	//Getters & Setters
	public Blob getImageSrc() {
		return ImageSrc;
	}
	public void setImageSrc(Blob imageSrc) {
		ImageSrc = imageSrc;
	}
	public int getN_livre() {
		return n_livre;
	}
	public void setN_livre(int n_livre) {
		this.n_livre = n_livre;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getMaisonED() {
		return maisonED;
	}
	public void setMaisonED(String maisonED) {
		this.maisonED = maisonED;
	}
	public int getN_pages() {
		return n_pages;
	}
	public void setN_pages(int n_pages) {
		this.n_pages = n_pages;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
		
}
