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

public class AddDVDController implements Initializable {
    @FXML
    private TextField actorsField;
    @FXML
    private TextField copiesField;
    @FXML
    private Button coverBtn;
    @FXML
    private TextField directorField;
    @FXML
    private TextField editorField;
    @FXML
    private TextField producerField;
    @FXML
    private TextField titleField;
    
    String path = null;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	//Retourner vers la page des DVDs
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/DVDs.fxml");
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
  //Ajouter un DVDs
    @FXML
    void Add(MouseEvent event) throws SQLException, FileNotFoundException{
    	if(path != null)
    		InsertDVDDetails();
    	else {//Si l'utilisateur n'a pas spécifié la couverture
    		Alert alert = new Alert(AlertType.ERROR, "Le champ de couverture est VIDE !"
        			, ButtonType.OK);
    		alert.showAndWait();
    	}
    }
  //Ajouter ce DVD a la base de donnée
    private void InsertDVDDetails() throws SQLException, FileNotFoundException {
    	String title = titleField.getText();
    	String director = directorField.getText();
    	String producer = producerField.getText();
    	String actors = actorsField.getText();
    	String editor = editorField.getText();
    	int copies = Integer.parseInt(copiesField.getText());
    	InputStream cover = new FileInputStream(new File(path));
    	
    	Connection con = DBConnection.getConnection();
    	String querie = "INSERT INTO dvds(cover, name, director, producer, actors, editor, copies) values (?,?,?,?,?,?,?)";//Ajouter le DVD a la table DVDs
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setBlob(1, cover);
    	ps.setString(2, title);
    	ps.setString(3, director);
    	ps.setString(4, producer);
    	ps.setString(5, actors);
    	ps.setString(6, editor);
    	ps.setInt(7, copies);
    	
    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Le DVD " + title
        			+ "est ajouté avec success ", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "Le dvd " + title
        			+ "n'est pas ajouté correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

}
