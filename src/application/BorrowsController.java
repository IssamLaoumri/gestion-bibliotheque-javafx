package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Services.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Borrow;

public class BorrowsController implements Initializable{
    @FXML
    private TableView<Borrow> borrowTable;
    @FXML
    private TableColumn<Borrow, Date> dueDateCol;
    @FXML
    private TableColumn<Borrow, Integer> idCol;
    @FXML
    private TableColumn<Borrow, Date> issueDateCol;
    @FXML
    private TableColumn<Borrow, String> nameCol;
    @FXML
    private TableColumn<Borrow, String> statusCol;
    @FXML
    private TableColumn<Borrow, String> titleCol;
    @FXML
    private TableColumn<Borrow, String> typeCol;
    @FXML
    private TextField keywordTextField;
    
    Connection con = null;
    ObservableList<Borrow> borrowsObservableList = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		con = DBConnection.getConnection();//Connection au base de donnée
		String querie = "SELECT * FROM emprunts ORDER BY type";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(querie);
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				String type = rs.getString("type");
				Integer id_book = rs.getInt("id_livre");
				String title = rs.getString("titre");
				Integer id_adherent = rs.getInt("id_adherent");
				String adherent = rs.getString("adherent");
				Date borrowDate = rs.getDate("dateEmprunt");
				Date dueDate = rs.getDate("delai");
				String status = rs.getString("status");
				
				borrowsObservableList.add(new Borrow(id,type, id_book, title, id_adherent, adherent, borrowDate, dueDate, status));
			}
			idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
			titleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
			nameCol.setCellValueFactory(new PropertyValueFactory<>("adherent"));
			issueDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
			dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
			statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
			
			borrowTable.setItems(borrowsObservableList);
			FilteredList<Borrow> filtredData = new FilteredList<>(borrowsObservableList, b -> true);
			
			//Chercher par nom de l'adhérent, le titre, le type ou le ID dans la bare de recherche les mots identiques dans la table
			keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				filtredData.setPredicate(borrow -> {
					if(newValue.isBlank() || newValue.isEmpty() || newValue==null)
						return true;
					
					String searchKeyWord = newValue.toLowerCase();//Obtenir le mot a chercher
					if(borrow.getAdherent().toLowerCase().indexOf(searchKeyWord) > -1)//S'il correspond au nom de l'adhérent
						return true;
					else if(borrow.getTitre().toLowerCase().indexOf(searchKeyWord) > -1)//S'il correspond au Titre
						return true;
					else if(borrow.getType().toLowerCase().indexOf(searchKeyWord) > -1)//S'il correspond au Type
						return true;
					else if(Integer.toString(borrow.getId()).indexOf(searchKeyWord) > -1)//S'il correspond au ID
						return true;
					else //Rien de ce qui précede
						return false;
				});
			});

			
			SortedList<Borrow> sortedData = new SortedList<>(filtredData);
			sortedData.comparatorProperty().bind(borrowTable.comparatorProperty());
			borrowTable.setItems(sortedData);
			
			//Personnaliser l'apparence de status
			statusCol.setCellFactory(new Callback<TableColumn<Borrow,String>, TableCell<Borrow,String>>() {
				@Override
				public TableCell<Borrow, String> call(TableColumn<Borrow, String> arg0) {
					return new TableCell<Borrow, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if(!isEmpty() && item.equals("En cours")) {
								this.setStyle("-fx-text-fill : red; -fx-background-color : white; -fx-alignment: center;");
								setText(item);
							}else if(!isEmpty() && item.equals("Sanctionne")) {
								this.setStyle("-fx-text-fill : white; -fx-background-color : red; -fx-alignment: center;");
								setText(item);
							}else {
								this.setStyle("-fx-text-fill : green; -fx-background-color : white; -fx-alignment: center;");
								setText(item);
							}
						};
					};	
				}
			});
		} catch (Exception e) {
			Logger.getLogger(BorrowsController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
	}
	
	//Emprunter un livre
    @FXML
    void BorrowBook(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/BorrowBook.fxml");
    }
    
    //Emprunter un CD
    @FXML
    void BorrowCD(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/BorrowCD.fxml");
    }
    
    //Emprunter un DVD
    @FXML
    void BorrowDVD(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/BorrowDVD.fxml");
    }

}
