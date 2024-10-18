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

import model.Book;

public class BooksController implements Initializable{
    @FXML
    private GridPane booksContainer;
    @FXML
    private TextField keywordTextField;
    @FXML
    private Button searchBtn;
    
    private List<Book> allBooks = new ArrayList<>();
    DisplayOnGrid displayOnGrid = new DisplayOnGrid();

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	//Ajouter un délai pour éviter les collisions
    	WaitForSeconds(300);
    	//Afficher les livres dans la GRIDPANE
    	displayOnGrid.Display(booksContainer, "SELECT * FROM Livres ORDER BY titre", allBooks, "/resources/view/Book.fxml");
	}
    //Rechercher un Livre
    @FXML
    void Search(MouseEvent event) throws IOException {
    	String keyword = keywordTextField.getText();
    	//Ajouter un délai pour éviter les collisions
    	WaitForSeconds(1000);
    	//Afficher les livres cherchés dans la GRIDPANE
    	displayOnGrid.Display(booksContainer,"SELECT * FROM Livres WHERE titre LIKE '%"+keyword+"%'"
    			+ "OR author LIKE '%"+keyword+"%' ORDER BY titre", allBooks, "/resources/view/Book.fxml");
    }
    
    //Ajouter un Livre
    @FXML
    void AddBook(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/AddBook.fxml");
    }
    //Pause
    private void WaitForSeconds(int miliSec) {
    	booksContainer.getChildren().clear();
    	try {
			Thread.sleep(miliSec);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
}
