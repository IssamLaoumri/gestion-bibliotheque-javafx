package application;

import application.Services.RefreshData;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/resources/view/Home.fxml"));
			Scene scene = new Scene(root,1315,800);
			
			//Charger le tableau de bord au début
			Parent dash = FXMLLoader.load(getClass().getResource("/resources/view/Dashboard.fxml"));
            root.setCenter(dash);
            //Certains interfaces utilisent des styles dans "/resources/CSS/application.css"
			scene.getStylesheets().add(getClass().getResource("/resources/CSS/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true); //plein écran
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) { 
		new RefreshData(86400);//Actualiser les sanctions et vérification de l'expiration d'adhésion chaque jour
		launch(args);
	}
}
