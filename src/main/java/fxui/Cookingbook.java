package fxui;

import fxui.loginpage.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Cookingbook application.
 */
public class Cookingbook extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginpage/Login.fxml"));

        // Create and set your custom controller (e.g., LoginController)
        LoginController controller = new LoginController();
        loader.setController(controller);

        // Load the FXML file with the custom controller set
        Parent parent = loader.load();

        // Set up the scene and show the stage
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}