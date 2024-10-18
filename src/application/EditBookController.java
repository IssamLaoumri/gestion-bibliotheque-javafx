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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Book;

public class EditBookController implements Initializable {
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
    Book book = BookController.selectedBook; //Accéder à la variable Book statique
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// ce bloc utilisé pour définir toutes les informations de Livre dans les champs de texte 
		titleField.setText(book.getName());
		authorField.setText(book.getAuthor());
		MEField.setText(book.getMaisonED());
		pagesField.setText(Integer.toString(book.getN_pages()));
		priceField.setText(Float.toString(book.getPrix()));
		copiesField.setText(Integer.toString(book.getCopies()));
		//-------------------------------------------------------------------------------------
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
	
	//Sauvgarder les changements
    @FXML
    void Save(MouseEvent event) throws FileNotFoundException, SQLException {
    	InsertBookDetails();
    }

    private void InsertBookDetails() throws SQLException, FileNotFoundException {
    	//Obtenir les informations saisies
    	String title = titleField.getText();
    	String author = authorField.getText();
    	String maisonED = MEField.getText();
    	int pages = Integer.parseInt(pagesField.getText());
    	float price = Float.parseFloat(priceField.getText());
    	int copies = Integer.parseInt(copiesField.getText());
    	//--------------------------------------------------------
    	
    	Connection con = DBConnection.getConnection();
    	if(path != null) //Si l'utilisateur a selectionné une nouvelle couverture
    	{
    		InputStream cover = new FileInputStream(new File(path));//Considerer la nouvelle couverture
    		String querie = "UPDATE livres SET cover = ?, titre = ?, author = ?, maisonED = ?, n_pages = ?, prix = ?, copies = ?"
    				+ " WHERE n_Livre = "+ book.getN_livre()+"";
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
            			+ " a été mis a jour", ButtonType.OK);
        		alert.showAndWait();
        	}
        	else {
        		Alert alert = new Alert(AlertType.ERROR, "Le livre " + title
            			+ "n'est pas modifié correctement", ButtonType.OK);
        		alert.showAndWait();
        	}
    	}else {  //sinon, ne pas mettre à jour la couverture
    		String querie = "UPDATE livres SET titre = ?, author = ?, maisonED = ?, n_pages = ?, prix = ?, copies = ?"
    				+ " WHERE n_Livre = "+ book.getN_livre()+"";
    		PreparedStatement ps = con.prepareStatement(querie);
    		ps.setString(1, title);
    		ps.setString(2, author);
    		ps.setString(3, maisonED);
    		ps.setInt(4, pages);
    		ps.setFloat(5, price);
    		ps.setInt(6, copies);
    		int updateRowCount = ps.executeUpdate();

        	if(updateRowCount > 0) {
        		Alert alert = new Alert(AlertType.INFORMATION, "Le livre " + title
            			+ " a été mis a jour", ButtonType.OK);
        		alert.showAndWait();
        	}
        	else {
        		Alert alert = new Alert(AlertType.ERROR, "Le livre " + title
            			+ "n'est pas modifié correctement", ButtonType.OK);
        		alert.showAndWait();
        	}
    	}
    	//mettre à jour le titre de livre dans la table des emprunts s'il existe 
    	String querie = "UPDATE emprunts SET titre = ? WHERE id_livre = "+ book.getN_livre() + "";
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, title);
    	ps.executeUpdate();
    	
    }


}
