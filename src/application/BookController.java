package application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import application.Services.DBConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Book;

public class BookController {
	@FXML
    private Label authorName;
    @FXML
    private ImageView bookCover;
    @FXML
    private Label bookName;
    @FXML
    private VBox box;
    @FXML
    private Label id;
    
    Book bookConfig;
    public static Book selectedBook;
    
    //Fonction permet de faire apparaitre certaine données dans l'interface graphique (voir ressources/view/book.fxml)
    public void setData(Book book) throws SQLException, IOException {
    	bookConfig = book;
    	InputStream in = book.getImageSrc().getBinaryStream();  
        BufferedImage imagen = ImageIO.read(in);
        Image image = SwingFXUtils.toFXImage(imagen, null );
    	bookCover.setImage(image);
    	bookName.setText(book.getName());
    	authorName.setText(book.getAuthor());
    	id.setText(Integer.toString(book.getN_livre()));
    	box.setStyle("-fx-background-radius:15;"+
    			"-fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");
    }
    //Supprimer le Livre
    @FXML
    void deleteBook(MouseEvent event) throws SQLException, IOException {
    	//afficher une alerte pour vérifier si l'utilisateur est sure
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Vous "
    			+ "etes sure de supprimer " + bookConfig.getName() + "?", ButtonType.YES, ButtonType.CANCEL);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.YES) {// Si l'utilisateur choisit le boutton "YES"
    		int bookId = bookConfig.getN_livre(); //Obtenir le ID de livre qui est UNIQUE
        	String querie = "DELETE FROM Livres WHERE n_Livre = "+bookId;//Requete SQL qui supprime Le livre
        	Connection con = DBConnection.getConnection();//Connection au Base de Donnée
    		PreparedStatement pst = con.prepareStatement(querie);
    		pst.executeUpdate(); //Execution de la Requete
    		
    		//Actualiser les livres
    		HomeController.GetInstance().moveToCenter("/resources/view/Books.fxml");
			
    	}
    }
    //Modifier le livres
    @FXML
    void EditBook(MouseEvent event) throws IOException {
    	selectedBook = bookConfig;
    	HomeController.GetInstance().moveToCenter("/resources/view/EditBook.fxml");
    }

    
}
