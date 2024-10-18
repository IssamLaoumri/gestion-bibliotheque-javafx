package application;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;

public class AddCDController implements Initializable {
    @FXML
    private TextField copiesField;
    @FXML
    private TextField editorField;
    @FXML
    private TextField interpreterField;
    @FXML
    private TextField nameField;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	//Retourner vers la pages des CDs
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/CDs.fxml");
    }
    //Ajouter un CD
    @FXML
    void Add(MouseEvent event) throws SQLException{
    	InsertCDDetails();
    }
    //Ajouter ce CD a la base de donnée
    private void InsertCDDetails() throws SQLException {
    	//Obtenir les informations saisies
    	String name = nameField.getText();
    	String editor = editorField.getText();
    	String interpreter = interpreterField.getText();
    	int copies = Integer.parseInt(copiesField.getText());
    	
    	Connection con = DBConnection.getConnection();
    	String querie = "INSERT INTO cds(albumName, editorName, interpreterName, copies) values (?,?,?,?)";//Ajouter le CD a la table CDs
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, name);
    	ps.setString(2, editor);
    	ps.setString(3, interpreter);
    	ps.setInt(4, copies);

    	
    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Le CD " + name
        			+ "est ajouté avec success ", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "Le CD " + name
        			+ "n'est pas ajouté correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

}
