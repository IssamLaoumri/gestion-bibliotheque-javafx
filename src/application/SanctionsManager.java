package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import application.Services.DBConnection;
import model.Borrow;

public class SanctionsManager {
	//Changer le status de chaque Adhérent en retard
	public void UpdateSanctionedAdherents() {
		long l = System.currentTimeMillis();
		Date todayDate = new Date(l); 
		Connection con = DBConnection.getConnection();
		String query = "Update emprunts SET status = 'Sanctionne' WHERE delai  < '"+todayDate+"' AND status = 'En cours'"; //change status to "Sanctionne"
		PreparedStatement preparedStatement;
		try {
			preparedStatement = con.prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//Spécifier l'amende totale des adhérents sanctionnés
	public void UpdateSanctions(Borrow borrow) {
		long l = System.currentTimeMillis();
		Date todayDate = new Date(l); 
		int id = borrow.getId();
		long montant = daysBetween(borrow.getDueDate(), todayDate) * 20;
		Connection con = DBConnection.getConnection();
		String query = "Update emprunts SET sanction = "+montant+" WHERE delai  < '"+todayDate+"' AND id = "+id+""; 
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//Calculer l'amende totale des adhérents sanctionnés
	public long UpdateSanctions(Date dueDate) {
		long l = System.currentTimeMillis();
		Date todayDate = new Date(l); 
		long montant = daysBetween(dueDate, todayDate) * 20;
		if (montant > 0)
			return montant;
		return 0;
	}
	//Résiliation de l'adhésion des personnes qui ont dépassé un ans sans régler leur sanctions
	public void AdherentExpiration() throws SQLException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1); 
	    java.util.Date pyear = cal.getTime();
	    java.sql.Date sqlDate = new java.sql.Date(pyear.getTime());
	    
		Connection con = DBConnection.getConnection();
		Statement s = con.createStatement();
		String query1 = "DELETE FROM adherents WHERE id = (SELECT id_adherent FROM emprunts WHERE delai < '"+sqlDate+"' AND status='Sanctionne')"; 
		String query2 = "DELETE FROM emprunts WHERE delai < '"+sqlDate+"'";
		s.addBatch(query1);
		s.addBatch(query2);
		s.executeBatch();
	}
	//Une fonction privée qui retourne le nombres des jours entre deux dates
	private long daysBetween(Date one, Date two) {
		long difference = (two.getTime() - one.getTime())/86400000;
		return difference;
	}
}
