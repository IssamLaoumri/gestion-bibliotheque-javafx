package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeController implements Initializable{
    @FXML
    private HBox cardLayout;
    @FXML
    private GridPane bookContainer;
    @FXML
    private HBox gestionLivresTab;
    @FXML
    private VBox contentArea;
    @FXML
    private BorderPane borderPane;

    private static HomeController instance; 
	
    /*
     	Utilisation d'une instance unique (Singleton) pour cette classe pour que la navigation et l'entête de l'application
     	ne sera pas charger a chaque fois qu'on charge une scene dans le centre de BORDERPANE
     */
	
	public HomeController() {
		instance = this;
	}
	
	public static HomeController GetInstance() {
		return instance;
	}
	
	public void moveToCenter(String fxml) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		borderPane.setCenter(root);
	}
	
    /*********************** NAVIGATION *************************************/
	@FXML
    public void GestionLivres(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/Books.fxml");
    }
	@FXML
    public void Dashboard(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/Dashboard.fxml");
    }
	@FXML
    public void GestionCDs(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/CDs.fxml");
    }
	@FXML
    public void GestionDVDs(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/DVDs.fxml");
    }
	@FXML
    public void GestionAdherents(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/Adherents.fxml");
    }
	@FXML
    public void GestionEmprunts(MouseEvent event) throws IOException{
		moveToCenter("/resources/view/Issues.fxml");
    }
    @FXML
    void GestionReturns(MouseEvent event) throws IOException {
		moveToCenter("/resources/view/Returns.fxml");
    }
    @FXML
    void GestionSanctions(MouseEvent event) throws IOException {
		moveToCenter("/resources/view/Sanctions.fxml");
    }
	
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    }
}
