package model;

import java.sql.Blob;

public class DVD {
	private Blob imageSrc;
	private int id;
	private String filmName;
	private String directorName;
	private String producerName;
	private String actorsNames;
	private String editorName;
	private int copies;
	
	public DVD(Blob imageSrc, int id, String filmName, String directorName, String producerName, String actorsNames,
			String editorName, int copies) {
		super();
		this.imageSrc = imageSrc;
		this.id = id;
		this.filmName = filmName;
		this.directorName = directorName;
		this.producerName = producerName;
		this.actorsNames = actorsNames;
		this.editorName = editorName;
		this.copies = copies;
	}
	
	
	public int getCopies() {
		return copies;
	}


	public void setCopies(int copies) {
		this.copies = copies;
	}


	public Blob getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(Blob imageSrc) {
		this.imageSrc = imageSrc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilmName() {
		return filmName;
	}
	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}
	public String getDirectorName() {
		return directorName;
	}
	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	public String getProducerName() {
		return producerName;
	}
	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}
	public String getActorsNames() {
		return actorsNames;
	}
	public void setActorsNames(String actorsNames) {
		this.actorsNames = actorsNames;
	}
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	
	
	
	}
