package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class SanctionsController implements Initializable{
    @FXML
    private TableView<Borrow> borrowTable;
    @FXML
    private TableColumn<Borrow, Integer> idCol;
    @FXML
    private TableColumn<Borrow, String> nameCol;
    @FXML
    private TableColumn<Borrow, Long> mttCol;
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
		SanctionsManager start = new SanctionsManager();
		long l = System.currentTimeMillis();
		Date todayDate = new Date(l); 
		
		con = DBConnection.getConnection();
		
		String querie = "SELECT * FROM emprunts WHERE delai < ? AND status = ?";
		try {
			PreparedStatement pst = con.prepareStatement(querie);
			pst.setDate(1, todayDate);
			pst.setString(2, "Sanctionne");
			ResultSet rs = pst.executeQuery();
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
				
				Borrow newRow = new Borrow(id,type, id_book, title, id_adherent, adherent, borrowDate, dueDate, status, start.UpdateSanctions(dueDate));
				start.UpdateSanctions(newRow);
				borrowsObservableList.add(newRow);
			}
			idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
			titleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
			nameCol.setCellValueFactory(new PropertyValueFactory<>("adherent"));
			mttCol.setCellValueFactory(new PropertyValueFactory<>("sanction"));
			
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
			
			mttCol.setCellFactory(new Callback<TableColumn<Borrow,Long>, TableCell<Borrow,Long>>() {
				
				@Override
				public TableCell<Borrow, Long> call(TableColumn<Borrow, Long> arg0) {
					return new TableCell<Borrow, Long>(){
						@Override
						public void updateItem(Long item, boolean empty) {
							super.updateItem(item, empty);
							if(!isEmpty() ) {
								this.setStyle("-fx-text-fill : red; -fx-alignment: center; -fx-background-color : white;");
								setText(item.toString() + " MAD");
							}else {
								this.setStyle("-fx-background-color : white; -fx-text-fill : white;");
							}
						};
					};	
				}
			});
		} catch (Exception e) {
			Logger.getLogger(SanctionsController.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
	}
	//Retourner un Livre, CD ou DVD aprés Réglement de sanction
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
        			Alert alert2 = new Alert(AlertType.INFORMATION, "Le "+adherent.getType()+" est retourne avec success",ButtonType.OK);
        			HomeController.GetInstance().moveToCenter("/resources/view/Sanctions.fxml");
        			alert2.showAndWait();
        		}
        	
    		}
    }
    //Vérifier s'il est retourné avec success
    private boolean IsReturned(Borrow adherent) throws SQLException {
    	boolean isReturned = false;
    	Alert alert = new Alert(AlertType.CONFIRMATION, " Est ce que vous etes sure ?", ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			String query = "Update emprunts SET status = 'Retourne' , sanction = 0 WHERE id  ="+adherent.getId();
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
	//Mettre à jour le nombre de dvd empruntés par l'adhérentt
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
    	long l = System.currentTimeMillis();
		Date todayDate = new Date(l); 
		
    	String querie = "SELECT * FROM emprunts WHERE delai < ? AND status = ?";
    	PreparedStatement pst = con.prepareStatement(querie);
		pst.setDate(1, todayDate);
		pst.setString(2, "En cours");
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
