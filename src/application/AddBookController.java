package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Services.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class AddBookController implements Initializable {
    @FXML
    private TextField MEField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField copiesField;
    @FXML
    private Button coverBtn;
    @FXML
    private TextField pagesField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField titleField;
    
    String path = null;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	//Retourner vers la page des livres
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/Books.fxml");
    }
    
    @FXML
    void CoverChooser(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	//Définir le filtre d'extension JPG, PNG
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fc.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Afficher la boîte de dialogue d'ouverture de fichier
        File file = fc.showOpenDialog(null);
        if(file != null)
        {
        	coverBtn.setText(file.getAbsolutePath());
        	path = file.getAbsolutePath();
        }
    }
    
    //Ajouter un Livre
    @FXML
    void Add(MouseEvent event) throws SQLException, FileNotFoundException{
    	if(path != null)
    		InsertBookDetails();
    	else { //Si l'utilisateur n'a pas spécifié la couverture
    		Alert alert = new Alert(AlertType.ERROR, "Le champ de couverture est VIDE !"
        			, ButtonType.OK);
    		alert.showAndWait();
    	}
    }
    //Ajouter ce livre a la base de donnée
    private void InsertBookDetails() throws SQLException, FileNotFoundException {
    	//Obtenir les informations saisies
    	String title = titleField.getText();
    	String author = authorField.getText();
    	String maisonED = MEField.getText();
    	int pages = Integer.parseInt(pagesField.getText());
    	float price = Float.parseFloat(priceField.getText());
    	int copies = Integer.parseInt(copiesField.getText());
    	InputStream cover = new FileInputStream(new File(path));
    	
    	Connection con = DBConnection.getConnection();
    	String querie = "INSERT INTO livres(cover, titre, author, maisonED, n_pages, prix, copies) values (?,?,?,?,?,?, ?)";//Ajouter le livre a la table Livres
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setBlob(1, cover);
    	ps.setString(2, title);
    	ps.setString(3, author);
    	ps.setString(4, maisonED);
    	ps.setInt(5, pages);
    	ps.setFloat(6, price);
    	ps.setInt(7, copies);
    	
    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Le livre " + title
        			+ "est ajouté avec success ", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "Le livre " + title
        			+ "n'est pas ajouté correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

}
