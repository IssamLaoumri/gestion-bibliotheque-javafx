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
import model.Adherent;
import javafx.scene.control.Alert.AlertType;

public class EditAdherentController implements Initializable {
    @FXML
    private TextField adresseField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    
    Adherent adherent = AdherentsController.selectedAdherent; //Accéder à la variable Adhérent statique
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// ce bloc utilisé pour définir toutes les informations de l'Adhérent dans les champs de texte 
				firstNameField.setText(adherent.getFirstName());
				lastNameField.setText(adherent.getLastName());
				adresseField.setText(adherent.getAdresse());
		//-------------------------------------------------------------------------------------
		
	}
	
	//Retouner vers la page des Adhérents
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/Adherents.fxml");
    }
    
    //Changer les détailles de l'adhérent
    @FXML
    void Add(MouseEvent event) throws SQLException, FileNotFoundException{
    	InsertAdherentDetails();
    }
    
    private void InsertAdherentDetails() throws SQLException, FileNotFoundException {
    	String firstName = firstNameField.getText();//Obtenir le prénom
    	String lastName = lastNameField.getText();//Obtenir le nom
    	String adresse = adresseField.getText();//Obtenir l'adresse

    	Connection con = DBConnection.getConnection();
    	String querie = "UPDATE adherents SET firstName = ?, lastName = ?, adresse = ? WHERE id = "+ adherent.getId() + ""; //mettre à jour l'adhérent
    	PreparedStatement ps = con.prepareStatement(querie);
    	ps.setString(1, firstName);
    	ps.setString(2, lastName);
    	ps.setString(3, adresse);

    	int updateRowCount = ps.executeUpdate();
    	if(updateRowCount > 0) {
    		Alert alert = new Alert(AlertType.INFORMATION, "L'adherent " + firstName+" "+ lastName
        			+ "est modifie avec success ", ButtonType.OK);
    		alert.showAndWait();
    	}
    	else {
    		Alert alert = new Alert(AlertType.ERROR, "L'adherent " + firstName+" "+ lastName
        			+ "n'est pas modifie correctement", ButtonType.OK);
    		alert.showAndWait();
    	}
    	//mettre à jour l'adhérent dans la table des emprunts s'il existe 
    	String querie2 = "UPDATE emprunts SET adherent = ? WHERE id_adherent = "+ adherent.getId() + "";
    	PreparedStatement ps2 = con.prepareStatement(querie2);
    	ps2.setString(1, firstName +" "+ lastName);
    	ps2.executeUpdate();
    }

}
