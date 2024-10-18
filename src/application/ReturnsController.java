package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Borrow;

public class ReturnsController implements Initializable{
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
    Borrow adherent = null;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		con = DBConnection.getConnection();
		String querie = "SELECT * FROM emprunts WHERE status='En cours' ORDER BY type";
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
			
			keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				filtredData.setPredicate(borrow -> {
					if(newValue.isBlank() || newValue.isEmpty() || newValue==null)
						return true;
					
					String searchKeyWord = newValue.toLowerCase();
					if(borrow.getAdherent().toLowerCase().indexOf(searchKeyWord) > -1)
						return true;
					else if(borrow.getTitre().toLowerCase().indexOf(searchKeyWord) > -1)
						return true;
					else if(borrow.getType().toLowerCase().indexOf(searchKeyWord) > -1)
						return true;
					else if(Integer.toString(borrow.getId()).indexOf(searchKeyWord) > -1)
						return true;
					else
						return false;
				});
			});

			
			SortedList<Borrow> sortedData = new SortedList<>(filtredData);
			sortedData.comparatorProperty().bind(borrowTable.comparatorProperty());
			borrowTable.setItems(sortedData);
			
			statusCol.setCellFactory(new Callback<TableColumn<Borrow,String>, TableCell<Borrow,String>>() {
				
				@Override
				public TableCell<Borrow, String> call(TableColumn<Borrow, String> arg0) {
					return new TableCell<Borrow, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if(!isEmpty()) {
								this.setStyle("-fx-text-fill : red; -fx-alignment: center; -fx-background-color : white;");
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
			Logger.getLogger(ReturnsController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
	}
	//Retourner un Livre, CD ou DVD
    @FXML
    void ReturnMaterial(MouseEvent event) throws SQLException, IOException {
    	adherent = borrowTable.getSelectionModel().getSelectedItem();
    	if(adherent == null) {
    		Alert alert = new Alert(AlertType.INFORMATION, "Aucun Adherent n'est selectionne.",ButtonType.OK);
    		alert.showAndWait();
    	}else {
    		String type = adherent.getType();
    		if(IsReturned(adherent)) {
    			if(type.equals("Livre"))
				{
    				UpdateBookCount(adherent);
    				UpdateAdherentBooksDetails(adherent);
				}
    			if (type.equals("CD"))
				{
    				UpdateCDCount(adherent);
    				UpdateAdherentCDsDetails(adherent);
				}
    			if (type.equals("DVD"))
				{
    				UpdateDVDCount(adherent);
    				UpdateAdherentDVDsDetails(adherent);
				}
    			Alert alert = new Alert(AlertType.INFORMATION, "Le "+adherent.getType()+" est retourné avec success",ButtonType.OK);
    			HomeController.GetInstance().moveToCenter("/resources/view/Returns.fxml");
    			alert.showAndWait();
    		}
    	}
    }
    //Vérifier s'il est retourné avec success
    private boolean IsReturned(Borrow adherent) throws SQLException {
    	boolean isReturned = false;
		Alert alert = new Alert(AlertType.CONFIRMATION, " Est ce que vous etes sure ?", ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
				String query = "Update emprunts SET status = 'Retourne' WHERE id  ="+adherent.getId(); //change status to "returned"
				PreparedStatement preparedStatement = con.prepareStatement(query);
				int rowCount = preparedStatement.executeUpdate();
				refreshTable();  
				if(rowCount > 0)
					isReturned = true;
				else
					isReturned = false;
		}else {
			isReturned = false;
		}
		return isReturned;
    }
    
	//Mettre à jour le nombre d'exemplaires de livres
	public void UpdateBookCount(Borrow adherent) throws SQLException {
		int bookId = adherent.getId_livre();
		con = DBConnection.getConnection();
		String querie = "UPDATE livres SET copies = copies+1 WHERE n_livre = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, bookId);
		ps.executeUpdate();
	}
	//Mettre à jour le nombre de copies de cd
	public void UpdateCDCount(Borrow adherent) throws SQLException {
		int bookId = adherent.getId_livre();
		con = DBConnection.getConnection();
		String querie = "UPDATE cds SET copies = copies+1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, bookId);
		ps.executeUpdate();
	}
	//Mettre à jour le nombre d'exemplaires de DVD
	public void UpdateDVDCount(Borrow adherent) throws SQLException {
		int bookId = adherent.getId_livre();
		con = DBConnection.getConnection();
		String querie = "UPDATE dvds SET copies = copies+1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, bookId);
		ps.executeUpdate();
	}
	
	//Mettre à jour le nombre de livres empruntés par l'adhérent
	public void UpdateAdherentBooksDetails(Borrow adherent) throws SQLException {
		int adherentId = adherent.getId_adherent();
		con = DBConnection.getConnection();
		String querie = "UPDATE adherents SET books = books-1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, adherentId);
		ps.executeUpdate();
	}
	//Mettre à jour le nombre de cd empruntés par l'adhérent
	public void UpdateAdherentCDsDetails(Borrow adherent) throws SQLException {
		int adherentId = adherent.getId_adherent();
		con = DBConnection.getConnection();
		String querie = "UPDATE adherents SET cds = cds-1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, adherentId);
		ps.executeUpdate();
	}
	//Mettre à jour le nombre de dvd empruntés par l'adhérent
	public void UpdateAdherentDVDsDetails(Borrow adherent) throws SQLException {
		int adherentId = adherent.getId_adherent();
		con = DBConnection.getConnection();
		String querie = "UPDATE adherents SET dvds = dvds-1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, adherentId);
		ps.executeUpdate();
	}

    //Actualiser la table
    private void refreshTable() throws SQLException {
    	borrowsObservableList.clear();
    	String query = "SELECT * FROM emprunts WHERE status='en cours' ORDER BY type DESC";
    	PreparedStatement pst = con.prepareStatement(query);
    	ResultSet rs = pst.executeQuery();
    	while(rs.next()) {
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
			borrowTable.setItems(borrowsObservableList);
    	}
    	pst.close();
    	rs.close();
    }

}
