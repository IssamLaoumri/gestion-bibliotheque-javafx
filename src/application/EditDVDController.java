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

import model.DVD;

public class EditDVDController implements Initializable {
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
    DVD dvd = DVDController.selectedDVD; //Accéder à la variable DVD statique
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// ce bloc utilisé pour définir toutes les informations de DVD dans les champs de texte 
		titleField.setText(dvd.getFilmName());
		directorField.setText(dvd.getDirectorName());
		editorField.setText(dvd.getEditorName());
		actorsField.setText(dvd.getActorsNames());
		producerField.setText(dvd.getProducerName());
		copiesField.setText(Integer.toString(dvd.getCopies()));
		//-------------------------------------------------------------------------------------
	}
	//Retourner vers la page des DVD
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
	//Sauvgarder les changements
    @FXML
    void Save(MouseEvent event) throws FileNotFoundException, SQLException {
    	InsertDVDDetails();
    }
    
    private void InsertDVDDetails() throws SQLException, FileNotFoundException {
    	//Obtenir les informations saisies
    	String title = titleField.getText();
    	String director = directorField.getText();
    	String producer = producerField.getText();
    	String actors = actorsField.getText();
    	String editor = editorField.getText();
    	int copies = Integer.parseInt(copiesField.getText());
    	//--------------------------------------------------------
    	
    	Connection con = DBConnection.getConnection();
    	if(path != null) //Si l'utilisateur a selectionné une nouvelle couverture
    	{
    		InputStream cover = new FileInputStream(new File(path));//Considerer la nouvelle couverture
    		String querie = "UPDATE dvds SET cover = ?, name = ?, director = ?, producer = ?, actors = ?, editor = ?, copies = ?"
    				+ " WHERE id = "+ dvd.getId()+"";
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
            			+ " a ete mis a jour", ButtonType.OK);
        		alert.showAndWait();
        	}
        	else {
        		Alert alert = new Alert(AlertType.ERROR, "Le DVD " + title
            			+ "n'est pas modifie correctement", ButtonType.OK);
        		alert.showAndWait();
        	}
    	}else {  //sinon, ne pas mettre à jour la couverture
    		String querie = "UPDATE dvds SET name = ?, director = ?, producer = ?, actors = ?, editor = ?, copies = ?"
    				+ " WHERE id = "+ dvd.getId()+"";
    		PreparedStatement ps = con.prepareStatement(querie);
    		ps.setString(1, title);
    		ps.setString(2, director);
    		ps.setString(3, producer);
    		ps.setString(4, actors);
    		ps.setString(5, editor);
    		ps.setInt(6, copies);
    		int updateRowCount = ps.executeUpdate();

    		if(updateRowCount > 0) {
        		Alert alert = new Alert(AlertType.INFORMATION, "Le DVD " + title
            			+ " a ete mis a jour", ButtonType.OK);
        		alert.showAndWait();
        	}
        	else {
        		Alert alert = new Alert(AlertType.ERROR, "Le DVD " + title
            			+ "n'est pas modifie correctement", ButtonType.OK);
        		alert.showAndWait();
        	}
    	}
    	//mettre à jour le titre de DVD dans la table des emprunts s'il existe 
    	String querie = "UPDATE emprunts SET titre = ? WHERE id_livre = "+ dvd.getId() + "";
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, title);
    	ps.executeUpdate();
    	
    }


}
