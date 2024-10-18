package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Services.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class BorrowDVDController implements Initializable{
    @FXML
    private Label adresse;
    @FXML
    private Label books;
    @FXML
    private VBox box;
    @FXML
    private Label cds;
    @FXML
    private Label directorName;
    @FXML
    private ImageView dvdCover;
    @FXML
    private Label dvdName;
    @FXML
    private Label dvds;
    @FXML
    private Label firstName;
    @FXML
    private Label id;
    @FXML
    private TextField idAdherent;
    @FXML
    private TextField idDVD;
    @FXML
    private Label lastName;
    
    Connection con = null;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Nous voulons créer un écouteur dans le champ de texte DvdID afin que lorsque nous quittons, les données se mettent à jour automatiquement
		idDVD.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(!arg2) {
					try {
						if(!idDVD.getText().equals(""))
							getDVDDetails();//Afficher les détails du CD à l'écran
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		//Nous voulons créer un écouteur dans le champ de texte AdherentID afin que lorsque nous quittons, les données se mettent à jour automatiquement
		idAdherent.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(!arg2) {
					try {
						if(!idAdherent.getText().equals(""))
							getAdherentDetails();//Afficher les détails de l'Adhérent à l'écran
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
	}
	//Retour à la section Emprunter
    @FXML
    void Return(MouseEvent event) throws IOException {
    	HomeController.GetInstance().moveToCenter("/resources/view/Issues.fxml");
    }
  //Emprunter Un Livre
    @FXML
    void Borrow(MouseEvent event) throws SQLException {
    	if(!idDVD.getText().equals("") && !idAdherent.getText().equals("")) {
    		if(!IsSanctioned()) {//Si l'adherent n'est pas sanctionnée
	    		if(CanBorrow()) { //Si le nombre total de CDs/DVDs empruntés n'a pas encore atteint le maximum (3)
	    			if(!IsAlreadyBorrowed()) {//Si le DVD est déjà Emprunté par l'adhérent
	    				if(IsAvailable()) {//vérifier si le DVD est disponible, cela signifie qu'il y a des copies de celui-ci
	    					if(DVDIsBorrowed()) {//Si les détails de l'emprunteur ont été transmis avec succès
	    						//Mettre à jour les exemplaires de DVDs et le nombre de DVDs empruntés de l'adhérent
	    						UpdateDVDCount();
	    						UpdateAdherentDetails();
	    						Alert alert = new Alert(AlertType.INFORMATION, "Le DVD est emprunte avec success.",ButtonType.OK);
	    						alert.showAndWait();
	    						getAdherentDetails();
	    					}else {
	    						Alert alert = new Alert(AlertType.INFORMATION, "Le DVD n'est pas emprunte correctement.",ButtonType.OK);
	    						alert.showAndWait();
	    					}
	    				}else {
	    					Alert alert = new Alert(AlertType.ERROR, "Le DVD n'est pas disponible pour le moment",ButtonType.OK);
	    					alert.showAndWait();
	    				}
	    			}else {
	    				Alert alert = new Alert(AlertType.ERROR, "Le DVD est deja emprunte par l'adherent.",ButtonType.OK);
	    				alert.showAndWait();
	    			}
	    			
	    		}else {
	    			Alert alert = new Alert(AlertType.ERROR, "L'adherent ne peut pas emprunter plus de 3 DVD/CD.",ButtonType.OK);
	    			alert.showAndWait();
	    		}
    		}else {
    			Alert alert = new Alert(AlertType.ERROR, "L'adherent est SANCTIONNE. Il faut payer la sanction.",ButtonType.OK);
    			alert.showAndWait();
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR, "Veuiller remplir les champs.",ButtonType.OK);
			alert.showAndWait();
		}
    		
    }
    
    //Afficher les détails du DVD à l'écran
	public void getDVDDetails() throws SQLException {
		int dvdId = Integer.parseInt(idDVD.getText());
		con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM dvds WHERE id = ?");
		ps.setInt(1, dvdId);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			dvdName.setText(rs.getString("name"));
			directorName.setText(rs.getString("director"));
			InputStream input = rs.getBinaryStream("cover");
			Image img = new Image(input);
			dvdCover.setImage(img);
		}else {
    		Alert alert = new Alert(AlertType.INFORMATION, "Aucun DVD n'est trouve.",ButtonType.OK);
    		alert.showAndWait();
    		idDVD.setText("");
		}
	}
	
	//Afficher les détails de l'adhérent à l'écran
	public void getAdherentDetails() throws SQLException {
		int idAdhe = Integer.parseInt(idAdherent.getText());
		con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM adherents WHERE id = ?");
		ps.setInt(1, idAdhe);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			id.setText(rs.getString("id"));
			firstName.setText(rs.getString("firstName"));
			lastName.setText(rs.getString("lastName"));
			adresse.setText(rs.getString("adresse"));
			books.setText(rs.getString("books"));
			cds.setText(rs.getString("cds"));
			dvds.setText(rs.getString("dvds"));
		}else {
    		Alert alert = new Alert(AlertType.INFORMATION, "Aucun Adherent n'est trouve.",ButtonType.OK);
    		alert.showAndWait();
    		idAdherent.setText("");
		}
	}
	
	//Récupérez le DVD et les détails des adhérents dans la base de données et empruntez le DVD s'il existe
	public boolean DVDIsBorrowed() throws SQLException {
		Boolean isBorrowed = false;
		int dvdId = Integer.parseInt(idDVD.getText());
		int adherentId = Integer.parseInt(idAdherent.getText());
		String title = dvdName.getText();
		String adherent = firstName.getText() +" "+ lastName.getText();
		Date borrowDate = new Date(System.currentTimeMillis());
		Date dueDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14));
		
		con = DBConnection.getConnection();
		String querie = "INSERT INTO emprunts(type, id_livre, titre, id_adherent, adherent, dateEmprunt, delai, status) values(?,?,?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setString(1, "DVD");
		ps.setInt(2, dvdId);
		ps.setString(3, title);
		ps.setInt(4, adherentId);
		ps.setString(5, adherent);
		ps.setDate(6, borrowDate);
		ps.setDate(7, dueDate);
		ps.setString(8, "En cours");
		
		int rowCount = ps.executeUpdate();
		if(rowCount>0) {
			isBorrowed = true;
		}else {
			isBorrowed = false;
		}
		return isBorrowed;
		
	}
	//Mettre à jour le nombre d'exemplaires de DVDs
	public void UpdateDVDCount() throws SQLException {
		int dvdId = Integer.parseInt(idDVD.getText());
		con = DBConnection.getConnection();
		String querie = "UPDATE dvds SET copies = copies-1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, dvdId);
		ps.executeUpdate();
	}
	//Mettre à jour le nombre de DVDs empruntés par l'adhérent
	public void UpdateAdherentDetails() throws SQLException {
		int adherentId = Integer.parseInt(idAdherent.getText());
		con = DBConnection.getConnection();
		String querie = "UPDATE adherents SET dvds = dvds+1 WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, adherentId);
		ps.executeUpdate();
	}
	//Vérifiez si le DVD est déjà emprunté par l'adhérent. Si oui, il ne peut pas en emprunter un autre
	public boolean IsAlreadyBorrowed() throws SQLException {
		boolean isAlreadyBorrowed = false;
		int dvdId = Integer.parseInt(idDVD.getText());
		int adherentId = Integer.parseInt(idAdherent.getText());
		String title = dvdName.getText();
		con = DBConnection.getConnection();
		String querie = "SELECT * FROM emprunts WHERE id_livre = ? AND id_adherent = ? AND titre = ? AND status = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, dvdId);
		ps.setInt(2, adherentId);
		ps.setString(3, title);
		ps.setString(4, "En cours");
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			isAlreadyBorrowed = true;
		}
		else {
			isAlreadyBorrowed = false;
		}
		return isAlreadyBorrowed;
	}
	//Si le nombre de CDs/DVDs empruntés n'a pas atteint le maximum
	public boolean CanBorrow() throws SQLException {
		boolean canBorrow = false;
		int adherentId = Integer.parseInt(idAdherent.getText());
		con = DBConnection.getConnection();
		String querie = "SELECT * FROM adherents WHERE id = ? AND (dvds + cds < 3)";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, adherentId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			canBorrow = true;
		}
		else {
			canBorrow = false;
		}
		return canBorrow;
	}
	//Si l'adhérent a une sanction
	public boolean IsSanctioned() throws SQLException {
		boolean isSanctioned = false;
		
		int adherentId = Integer.parseInt(idAdherent.getText());
		con = DBConnection.getConnection();
		String querie = "SELECT * FROM emprunts WHERE status = ? AND id_adherent = ?";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setString(1, "Sanctionne");
		ps.setInt(2, adherentId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			isSanctioned = true;
		}
		else {
			isSanctioned = false;
		}
		return isSanctioned;
	}
	//Vérifier si les exemplaires du DVD sont disponibles
	public boolean IsAvailable() throws SQLException {
		boolean isAvailable = false;
		int dvdId = Integer.parseInt(idDVD.getText());
		con = DBConnection.getConnection();
		String querie = "SELECT * FROM dvds WHERE id = ? AND copies > 0";
		PreparedStatement ps = con.prepareStatement(querie);
		ps.setInt(1, dvdId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			isAvailable = true;
		}
		else {
			isAvailable = false;
		}
		return isAvailable;
	}


}
