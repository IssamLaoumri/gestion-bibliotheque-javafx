package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Services.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Book;

public class DashboardController implements Initializable{
	@FXML
    private GridPane bookContainer;
    @FXML
    private Label BooksNumber;
    @FXML
    private Label adherentsNumber;
    @FXML
    private VBox contentArea;
    @FXML
    private Label books;
    @FXML
    private Label cds;
    @FXML
    private Label dvds;
    @FXML
    private Label borrowsNumber;
    @FXML
    private Label sanctionsNumber;
    
    Connection con = null;
    Statement st = null;
	ResultSet rs = null;
    
    private List<Book> recentlyAdded = new ArrayList<>();
    DisplayOnGrid displayOnGrid = new DisplayOnGrid();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SetDataToCards();
		//Afficher les livres récemments ajoutée
		displayOnGrid.Display(bookContainer, "SELECT * FROM livres ORDER BY n_Livre DESC LIMIT 5", recentlyAdded, "/resources/view/DashBook.fxml");
	}
	//Afficher le Nombre qui correspond à chaque carte
	private void SetDataToCards() {
		try {
			con = DBConnection.getConnection();//Connection au base de données
			st = con.createStatement();
			//Afficher le nombre d'adhérents sur la carte du tableau de bord
			rs = st.executeQuery("SELECT count(*) FROM adherents");
			rs.next();
			adherentsNumber.setText(Integer.toString(rs.getInt(1)));
			
			//Afficher le nombre de livres sur la carte du tableau de bord
			rs = st.executeQuery("SELECT count(*) FROM livres");
			rs.next();
			int book = rs.getInt(1);
			books.setText(Integer.toString(book));
			//Afficher le nombre de CD sur la carte du tableau de bord
			rs = st.executeQuery("SELECT count(*) FROM cds");
			rs.next();
			int cd = rs.getInt(1);
			cds.setText(Integer.toString(cd));
			//Afficher le nombre de DVD sur la carte du tableau de bord
			rs = st.executeQuery("SELECT count(*) FROM dvds");
			rs.next();
			int dvd = rs.getInt(1);
			dvds.setText(Integer.toString(dvd));
			
			//Afficher le nombre de ressources sur la carte du tableau de bord
			BooksNumber.setText(Integer.toString(book + cd + dvd));
			//Afficher le nombre d'emprunts sur la carte du tableau de bord
			rs = st.executeQuery("SELECT count(*) FROM emprunts WHERE status = 'En cours' OR status = 'Sanctionne'");
			rs.next();
			borrowsNumber.setText(Integer.toString(rs.getInt(1)));
			//Afficher le nombre de sanctions sur la carte du tableau de bord
			String qr = "SELECT count(*) FROM emprunts WHERE status = 'Sanctionne'";
			rs = st.executeQuery(qr);
			rs.next();
			sanctionsNumber.setText(Integer.toString(rs.getInt(1)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
