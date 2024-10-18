package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Services.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.CD;

public class EditCDController implements Initializable {
    @FXML
    private TextField copiesField;
    @FXML
    private TextField editorField;
    @FXML
    private TextField interpreterField;
    @FXML
    private TextField nameField;
    
    CD cd = CDController.selectedCD; //Accéder à la variable CD statique
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// ce bloc utilisé pour définir toutes les informations de CD dans les champs de texte 
		nameField.setText(cd.getAlbumName());
		editorField.setText(cd.getEditorName());
		interpreterField.setText(cd.getInterpreterName());
		copiesField.setText(Integer.toString(cd.getCopies()));
		//************************************************************
	}
	//Retouner vers la page des CDs
	@FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/CDs.fxml");
    }
	//Sauvgarder les changements
    @FXML
    void Save(MouseEvent event) throws FileNotFoundException, SQLException {
    	InsertBookDetails();
    }
    
    private void InsertBookDetails() throws SQLException, FileNotFoundException {
    	//Obtenir les informations saisies
    	String name = nameField.getText();
    	String editor = editorField.getText();
    	String interpreter = interpreterField.getText();
    	int copies = Integer.parseInt(copiesField.getText());
    	//***************************************************
    	
    	//Mis a jour de données et affichage d'alerte
    	Connection con = DBConnection.getConnection();
    	String querie = "UPDATE cds SET albumName = ?, editorName = ?, interpreterName = ?, copies = ?"
    			+ " WHERE id = "+ cd.getId()+"";;
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, name);
    	ps.setString(2, editor);
    	ps.setString(3, interpreter);
    	ps.setInt(4, copies);

    	
    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Le CD " + name
        			+ " a été mis a jour", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "Le CD " + name
        			+ "n'est pas modifié correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    	
    	//mettre à jour le titre de CD dans la table des emprunts s'il existe 
    	String querie2 = "UPDATE emprunts SET titre = ? WHERE id_livre = "+ cd.getId() + "";
    	PreparedStatement ps2 = con.prepareStatement(querie2);
    	ps2.setString(1, name);
    	ps2.executeUpdate();
    }


}
