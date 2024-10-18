package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import model.DVD;

public class DVDsController implements Initializable{
    @FXML
    private GridPane dvdsContainer;
    @FXML
    private TextField keywordTextField;
    @FXML
    private Button searchBtn;
    
    private List<DVD> allDVDs = new ArrayList<>();
    DisplayOnGrid displayOnGrid = new DisplayOnGrid();

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	//Ajouter un délai pour éviter les collisions
    	WaitForSeconds(300);
    	//Afficher les DVDs dans la GRIDPANE
    	displayOnGrid.DisplayDVD(dvdsContainer, "SELECT * FROM dvds ORDER BY name", allDVDs, "/resources/view/DVD.fxml");
	}
  //Rechercher un DVD
    @FXML
    void Search(MouseEvent event) throws IOException {
    	String keyword = keywordTextField.getText();
    	WaitForSeconds(1000);
    	displayOnGrid.DisplayDVD(dvdsContainer,"SELECT * FROM dvds WHERE name LIKE '%"+keyword+"%'"
    			+ "OR director LIKE '%"+keyword+"%' ORDER BY name", allDVDs, "/resources/view/DVD.fxml");
    }
    //Ajouter un DVD
    @FXML
    void AddDVD(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/AddDVD.fxml");
    }
    //Pause
    private void WaitForSeconds(int miliSec) {
    	dvdsContainer.getChildren().clear();
    	try {
			Thread.sleep(miliSec);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
}
