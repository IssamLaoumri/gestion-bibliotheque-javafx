package application;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Services.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Book;
import model.CD;
import model.DVD;

public class DisplayOnGrid {
	//Afficher les livres
	public void Display(GridPane container, String querie, List<Book> BooksList, String url) {
    	int columns = 0;
		int rows = 1;
		boolean checkIfExist = false;

		try {
			BooksList = BooksList(querie);
			// Visualiser la liste dans booksContainer
			for(Book book : BooksList) 
			{
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource(url));
				VBox bookBox = fxmlLoader.load();
				BookController bookController = fxmlLoader.getController();
				bookController.setData(book);
				
				if(columns == 5)
				{
					columns = 0;
					++rows;
				}
				
				container.add(bookBox, columns++, rows);
				GridPane.setMargin(bookBox, new Insets(10));
				checkIfExist = true;
			}
			if(!checkIfExist) {
				Alert alert = new Alert(AlertType.ERROR, "Aucun livre trouvé", ButtonType.OK);
				alert.showAndWait();
			}
			
		}
		catch(IOException | SQLException e){
			e.printStackTrace();
		}
		
    }
	//Afficher les CDs
	public void DisplayCDs(GridPane container, String querie, List<CD> cdsList, String url) {
    	int columns = 0;
		int rows = 1;
		boolean checkIfExist = false;

		try {
			cdsList = CDsList(querie);
			// Visualiser la liste dans CDsContainer
			for(CD cd : cdsList) 
			{
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource(url));
				VBox cdBox = fxmlLoader.load();
				CDController cdController = fxmlLoader.getController();
				cdController.setData(cd);
				
				if(columns == 5)
				{
					columns = 0;
					++rows;
				}
				
				container.add(cdBox, columns++, rows);
				GridPane.setMargin(cdBox, new Insets(10));
				checkIfExist = true;
			}
			if(!checkIfExist) {
				Alert alert = new Alert(AlertType.ERROR, "Aucun CD trouvé", ButtonType.OK);
				alert.showAndWait();
			}
			
		}
		catch(IOException | SQLException e){
			e.printStackTrace();
		}
		
    }
	//Afficher les DVDs
	public void DisplayDVD(GridPane container, String querie, List<DVD> dvdsList, String url) {
    	int columns = 0;
		int rows = 1;
		boolean checkIfExist = false;

		try {
			dvdsList = DVDsList(querie);
			// Visualiser la liste dans booksContainer
			for(DVD dvd : dvdsList) 
			{
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource(url));
				VBox dvdBox = fxmlLoader.load();
				DVDController dvdController = fxmlLoader.getController();
				dvdController.setData(dvd);
				
				if(columns == 5)
				{
					columns = 0;
					++rows;
				}
				
				container.add(dvdBox, columns++, rows);
				GridPane.setMargin(dvdBox, new Insets(10));
				checkIfExist = true;
			}
			if(!checkIfExist) {
				Alert alert = new Alert(AlertType.ERROR, "Aucun dvd trouvé", ButtonType.OK);
				alert.showAndWait();
			}
			
		}
		catch(IOException | SQLException e){
			e.printStackTrace();
		}
		
    }
    //Collecter les livres de la base de données dans une liste
    private List<Book> BooksList(String sqlQuerie) throws SQLException{	
    	List<Book> ls = new ArrayList<>();
		Connection con = DBConnection.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sqlQuerie);
		while(rs.next())
		{
			//Book book = new Book();
			Blob cover = rs.getBlob("cover");
			int n_livre = rs.getInt("n_Livre");
			String titre = rs.getString("titre");
			String author = rs.getString("author");
			String maisonED = rs.getString("maisonED");
			int n_pages = rs.getInt("n_pages");
			float prix = rs.getFloat("prix");
			int copies = rs.getInt("copies");
			
			Book book = new Book(cover, n_livre, titre, author, maisonED, n_pages, prix, copies);
			ls.add(book);
			
		}
    	return ls;
    }
    //Collecter les CDs de la base de données dans une liste
    private List<CD> CDsList(String sqlQuerie) throws SQLException{
    	
    	List<CD> ls = new ArrayList<>();
		Connection con = DBConnection.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sqlQuerie);
		while(rs.next())
		{ 
			int id = rs.getInt("id");
			String albumName = rs.getString("albumName");
			String editorName = rs.getString("editorName");
			String interpreterName = rs.getString("interpreterName");
			int copies = rs.getInt("copies");
			
			CD cd = new CD(id, albumName, editorName, interpreterName, copies);
			ls.add(cd);
			
		}
    	return ls;
    }
    //Collecter les DVDs de la base de données dans une liste
    private List<DVD> DVDsList(String sqlQuerie) throws SQLException{
    	
    	List<DVD> ls = new ArrayList<>();
		Connection con = DBConnection.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sqlQuerie);
		while(rs.next())
		{
			//Book book = new Book();
			Blob cover = rs.getBlob("cover");
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String director = rs.getString("director");
			String producer = rs.getString("producer");
			String actors = rs.getString("actors");
			String editor = rs.getString("editor");
			int copies = rs.getInt("copies");
			
			DVD dvd = new DVD(cover, id, name, director, producer, actors, editor, copies);
			ls.add(dvd);
			
		}
    	return ls;
    }
    

}
