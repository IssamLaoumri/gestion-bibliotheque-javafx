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
import model.DVD;

public class DVDController {

    @FXML
    private VBox box;

    @FXML
    private Label directorName;

    @FXML
    private ImageView dvdCover;

    @FXML
    private Label dvdName;
    @FXML
    private Label id;
    
    DVD dvdConfig;
    public static DVD selectedDVD;

    //Fonction permet de faire apparaitre certaine données dans l'interface graphique (voir ressources/view/DVD.fxml)
    public void setData(DVD dvd) throws SQLException, IOException {
    	dvdConfig = dvd;
    	InputStream in = dvd.getImageSrc().getBinaryStream();  
        BufferedImage imagen = ImageIO.read(in);
        Image image = SwingFXUtils.toFXImage(imagen, null );
    	dvdCover.setImage(image);
    	dvdName.setText(dvd.getFilmName());
    	directorName.setText(dvd.getDirectorName());
    	id.setText(Integer.toString(dvd.getId()));
    	box.setStyle("-fx-background-radius:15;"+
    			"-fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");
    }
    //Supprimer le DVD
    @FXML
    void deleteDVD(MouseEvent event) throws SQLException, IOException {
    	//afficher une alerte pour vérifier si l'utilisateur est sure
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Vous "
    			+ "etes sure de supprimer " + dvdConfig.getFilmName() + "?", ButtonType.YES, ButtonType.CANCEL);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.YES) {// Si l'utilisateur choisit le boutton "YES"
    		int dvdId = dvdConfig.getId(); //Obtenir le ID de DVD qui est UNIQUE
        	String querie = "DELETE FROM dvds WHERE id = "+dvdId;//Requete SQL qui supprime Le DVD
        	Connection con = DBConnection.getConnection();//Connection au Base de Donnée
    		PreparedStatement pst = con.prepareStatement(querie);
    		pst.executeUpdate(); //Execution de la Requete
    		
    		//Actualiser les DVDs
    		HomeController.GetInstance().moveToCenter("/resources/view/DVDs.fxml");
			
    	}
    }
    //Modifier le DVD
    @FXML
    void EditDVD(MouseEvent event) throws IOException {
    	selectedDVD = dvdConfig;
    	HomeController.GetInstance().moveToCenter("/resources/view/EditDVD.fxml");
    }

    
}
