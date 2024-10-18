package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.Services.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.CD;

public class CDController {
    @FXML
    private Label albumName;
    @FXML
    private VBox box;
    @FXML
    private Label editorName;
    @FXML
    private Label interpreterName;
    @FXML
    private Label id;
    
    public static CD selectedCD;
    CD cdConfig;
    //Fonction permet de faire apparaitre certaine données dans l'interface graphique (ressources/view/CD.fxml)
    public void setData(CD cd) throws SQLException, IOException {
    	cdConfig = cd;
    	albumName.setText(cd.getAlbumName());
    	editorName.setText(cd.getEditorName());
    	interpreterName.setText(cd.getInterpreterName());
    	id.setText(Integer.toString(cd.getId()));
    	box.setStyle("-fx-background-radius:15;"+
    			"-fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");
    }
    //Supprimer le CD
    @FXML
    void DeleteCD(MouseEvent event) throws SQLException, IOException {
    	//afficher une alerte pour vérifier si l'utilisateur est sure
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Vous "
    			+ "êtes sure de supprimer " + cdConfig.getAlbumName() + "?", ButtonType.YES, ButtonType.CANCEL);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.YES) {// Si l'utilisateur choisit le boutton "YES"
    		int cdId = cdConfig.getId(); //Obtenir le ID de CD qui est UNIQUE
        	String querie = "DELETE FROM cds WHERE id = "+cdId;//Requete SQL qui supprime Le CD
        	Connection con = DBConnection.getConnection();//Connection au Base de Donnée
    		PreparedStatement pst = con.prepareStatement(querie);
    		pst.executeUpdate(); //Execution de la Requete
    		
    		//Actualiser les CDs
    		HomeController.GetInstance().moveToCenter("/resources/view/CDs.fxml");
			
    	}
    }
  	//Modifier le livres
    @FXML
    void EditCD(MouseEvent event) throws IOException {
    	selectedCD = cdConfig;
    	HomeController.GetInstance().moveToCenter("/resources/view/EditCD.fxml");
    }
    
}
