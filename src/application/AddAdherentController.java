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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;

public class AddAdherentController implements Initializable {
    @FXML
    private TextField adresseField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
	
  //Retouner vers la page des Adhérents
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/Adherents.fxml");
    }
    
    //Ajouter un Adhérent
    @FXML
    void Add(MouseEvent event) throws SQLException, FileNotFoundException{
    	InsertAdherentDetails();
    }
    
    private void InsertAdherentDetails() throws SQLException, FileNotFoundException {
    	String firstName = firstNameField.getText();//Obtenir le prénom
    	String lastName = lastNameField.getText();//Obtenir le nom
    	String adresse = adresseField.getText();//Obtenir l'adresse

    	Connection con = DBConnection.getConnection();
    	String querie = "INSERT INTO adherents(firstName, lastName, adresse) values (?,?,?)"; //Ajouter une ligne dans la tables des adhérents
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, firstName);
    	ps.setString(2, lastName);
    	ps.setString(3, adresse);

    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "L'adherent " + firstName+" "+ lastName
        			+ "est ajoute avec success ", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "L'adherent " + firstName+" "+ lastName
        			+ "n'est pas ajoute correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
