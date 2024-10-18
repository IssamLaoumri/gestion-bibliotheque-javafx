package model;

public class CD {
	private int id;
	private String albumName;
	private String editorName;
	private String interpreterName;
	private int copies;
	
	public CD() {
		super();
	}
	
	public CD(int id, String album, String editor, String interpreter, int copies) {
		this.id = id;
		this.albumName = album;
		this.editorName = editor;
		this.interpreterName = interpreter;
		this.copies = copies;
	}
	
	//Getters & Setters
	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	public String getInterpreterName() {
		return interpreterName;
	}
	public void setInterpreterName(String interpreterName) {
		this.interpreterName = interpreterName;
	}
	
	
}
