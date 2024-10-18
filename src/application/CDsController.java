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
import model.CD;

public class CDsController implements Initializable{
    @FXML
    private GridPane cdsContainer;
    @FXML
    private TextField keywordTextField;
    @FXML
    private Button searchBtn;
    
    private List<CD> allCDs = new ArrayList<>();
    DisplayOnGrid displayOnGrid = new DisplayOnGrid();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	//Ajouter un délai pour éviter les collisions
    	WaitForSeconds(300);
    	//Afficher les CDs dans la GRIDPANE
    	displayOnGrid.DisplayCDs(cdsContainer, "SELECT * FROM cds ORDER BY albumName", allCDs, "/resources/view/CD.fxml");
    }
    //Ajouter un CD
    @FXML
    void AddCD(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/AddCD.fxml");
    }
    //Rechercher un CD
    @FXML
    void Search(MouseEvent event) {
    	String keyword = keywordTextField.getText();
    	//Ajouter un délai pour éviter les collisions
    	WaitForSeconds(1000);
    	//Afficher les CDs cherchés dans la GRIDPANE
    	displayOnGrid.DisplayCDs(cdsContainer,"SELECT * FROM cds WHERE albumName LIKE '%"+keyword+"%'"
    			, allCDs, "/resources/view/CD.fxml");
    }
    //Pause
    private void WaitForSeconds(int miliSec) {
    	cdsContainer.getChildren().clear();
    	try {
			Thread.sleep(miliSec);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }

}
