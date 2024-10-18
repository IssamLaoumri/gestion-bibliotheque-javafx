package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Adherent;

public class AdherentsController implements Initializable{
    @FXML
    private TableView<Adherent> adherentTable;
    @FXML
    private TableColumn<Adherent, String> adresseCol;
    @FXML
    private TableColumn<Adherent, Integer> bookCol;
    @FXML
    private TableColumn<Adherent, Integer> cdCol;
    @FXML
    private TableColumn<Adherent, Integer> dvdCol;
    @FXML
    private TableColumn<Adherent, Integer> idCol;
    @FXML
    private TableColumn<Adherent, String> lastNameCol;
    @FXML
    private TableColumn<Adherent, String> firstNameCol;
    @FXML
    private TextField keywordTextField;

    @FXML
    private Button searchBtn;
    
    ObservableList<Adherent> adherentObservableList = FXCollections.observableArrayList();
    
    Connection con = null;
    
    Adherent adherent = null;
    
    public static Adherent selectedAdherent;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		con = DBConnection.getConnection(); //Connection au base de donnée
		String querie = "SELECT * FROM adherents ORDER BY firstName"; //Respecter l'ordre alphabétique
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(querie);
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String adresse = rs.getString("adresse");
				Integer books = rs.getInt("books");
				Integer cds = rs.getInt("cds");
				Integer dvds = rs.getInt("dvds");
				
				adherentObservableList.add(new Adherent(id,firstName, lastName, adresse, books, cds, dvds));
			}
			
			idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
			lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
			adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
			bookCol.setCellValueFactory(new PropertyValueFactory<>("books"));
			cdCol.setCellValueFactory(new PropertyValueFactory<>("cds"));
			dvdCol.setCellValueFactory(new PropertyValueFactory<>("dvds"));
			
			adherentTable.setItems(adherentObservableList);
			
			FilteredList<Adherent> filtredData = new FilteredList<>(adherentObservableList, b -> true);
			
			//Chercher par id, nom et prénom dans la bare de recherche les mots identiques dans la table
			keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				filtredData.setPredicate(adherent -> {
					if(newValue.isBlank() || newValue.isEmpty() || newValue==null)
						return true;
					
					String searchKeyWord = newValue.toLowerCase();//Obtenir le mot a chercher
					if(adherent.getFirstName().toLowerCase().indexOf(searchKeyWord) > -1)//S'il correspond au prénom
						return true;
					else if(adherent.getLastName().toLowerCase().indexOf(searchKeyWord) > -1)//S'il correspond au nom
						return true;
					else if(Integer.toString(adherent.getId()).indexOf(searchKeyWord) > -1)//S'il correspond au ID
						return true;
					else //Rien de ce qui précede
						return false;
				});
			});

			//Recherche Dynamique
			SortedList<Adherent> sortedData = new SortedList<>(filtredData);
			sortedData.comparatorProperty().bind(adherentTable.comparatorProperty());
			adherentTable.setItems(sortedData);
				
			
		} catch (Exception e) {
			Logger.getLogger(AdherentsController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
		
	}
	
	//Ajouter un adherent
    @FXML
    void Add(MouseEvent event) throws IOException {
    	//Charger la scene de l'ajout
    	HomeController.GetInstance().moveToCenter("/resources/view/AddAdherent.fxml");
    }
    
    //Modifier un adherent
    @FXML
    void Edit(MouseEvent event) throws IOException {
    	selectedAdherent = adherentTable.getSelectionModel().getSelectedItem();
    	if(selectedAdherent == null) { //si l'utilisateur n'a pas selectionné une ligne de la table
    		Alert alert = new Alert(AlertType.INFORMATION, "Aucun Adherent n'est selectionne.",ButtonType.CANCEL);// Afficher une alerte
    		alert.showAndWait();
    	}else {//sinon, charger la scene de modification
    		HomeController.GetInstance().moveToCenter("/resources/view/EditAdherent.fxml");    		
    	}
    }
    
    //Supprimer un adherent
    @FXML
    void Delete(MouseEvent event) throws SQLException {
    	adherent = adherentTable.getSelectionModel().getSelectedItem();
    	if(adherent == null) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Aucun Adherent n'est selectionne.",ButtonType.CANCEL);
    		alert.showAndWait();
    	}else {
    		//afficher une alerte pour vérifier si l'utilisateur est sure
    		Alert alert = new Alert(AlertType.CONFIRMATION, "Vous "
    				+ "etes sure de supprimer " + adherent.getFirstName() + " " + adherent.getLastName() + "?", ButtonType.YES, ButtonType.CANCEL);
    		alert.showAndWait();
    		if (alert.getResult() == ButtonType.YES) {// Si l'utilisateur choisit le boutton "YES"
    			if(!HasBorrowedMaterial(adherent)) {//Vérifier si l'adherent n'a aucun emprunts
    				String query = "DELETE FROM adherents WHERE id  ="+adherent.getId();//Si oui, Supprime-le
    				PreparedStatement preparedStatement = con.prepareStatement(query);
    				preparedStatement.execute();
    				refreshTable();
    			}else {
    				Alert alert2 = new Alert(AlertType.ERROR, "l'adherent n'a pas encore retourner ces empruntes.",ButtonType.OK);//sinon, afficher une alerte
    				alert2.showAndWait();
    			}
    		}
    	}
    }
	
    //Mis a jours de la table 
    private void refreshTable() throws SQLException {
    	adherentObservableList.clear();
    	String query = "SELECT * FROM adherents ORDER BY firstName";
    	PreparedStatement pst = con.prepareStatement(query);
    	ResultSet rs = pst.executeQuery();
    	while(rs.next()) {
			Integer id = rs.getInt("id");
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("lastName");
			String adresse = rs.getString("adresse");
			Integer books = rs.getInt("books");
			Integer cds = rs.getInt("cds");
			Integer dvds = rs.getInt("dvds");
			
			adherentObservableList.add(new Adherent(id,firstName, lastName, adresse, books, cds, dvds));
			adherentTable.setItems(adherentObservableList);
    	}
    	pst.close();
    	rs.close();
    }
    
    //Vérifier si l'adherent a des emprunts ou non
    private boolean HasBorrowedMaterial(Adherent adherent) throws SQLException {
    	boolean hasBorrowedMaterial = false;
    	int id = adherent.getId();
    	con = DBConnection.getConnection();
		String querie = "SELECT * FROM emprunts WHERE id_adherent = ? AND status = 'en cours'";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			hasBorrowedMaterial = true;
		}
		else {
			hasBorrowedMaterial = false;
		}
		return hasBorrowedMaterial;
    }
	
}
