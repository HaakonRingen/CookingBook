package fxui.newuserpage;

import fxui.loginpage.LoginController;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;



/**
 * Controller class for handling new user creation and navigation to the login screen.
 */
public class NewUserController {

    @FXML
    private Button backBtn;

    @FXML
    private Button createBtn;

    @FXML
    private TextField id;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField password;

    NewUserModel model;

    /**
     * Constructs a new NewUserController.
     */
    public NewUserController() {
    }

    /**
     * Constructs a new NewUserController with the given model.
     * 
     * @param model the model for creating new users
     */
    public NewUserController(NewUserModel model) {
        this.model = model;
    }

    /**
     * Initializes the controller and sets up the button animations.
     * 
     * @throws Exception if an error occurs during initialization.
     */
    @FXML
    public void initialize() throws Exception {
        if (model == null) {
            model = new NewUserModel();
        }

        ScaleTransition scaleUpLogin = new ScaleTransition(Duration.millis(200), createBtn);
        scaleUpLogin.setToX(1.1);
        scaleUpLogin.setToY(1.1);
    
        ScaleTransition scaleDownLogin = new ScaleTransition(Duration.millis(200), createBtn);
        scaleDownLogin.setToX(1.0);
        scaleDownLogin.setToY(1.0);
    
        // On mouse enter, scale up the button
        createBtn.setOnMouseEntered(e -> scaleUpLogin.playFromStart());
    
        // On mouse exit, scale down the button
        createBtn.setOnMouseExited(e -> scaleDownLogin.playFromStart());

    }

    @FXML
    void createUser(ActionEvent event) {
        try {
            validateUserInput(); // Validate input fields before creating the user

            String newId = id.getText().trim();
            String password = this.password.getText().trim();
            String confirmPassword = this.confirmPassword.getText().trim();
            String firstName = this.firstName.getText().trim();
            String lastName = this.lastName.getText().trim();

            model.createUser(firstName, lastName, newId, password, confirmPassword);

            if (model.getSuccess()) {
                errorMessage.setText("User created successfully");
            } else {
                errorMessage.setText("User creation failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
        }
    }

    /**
     * Navigates to the login screen.
     * 
     * @throws Exception if an error occurs during loading the FXML file.
     */
    @FXML
    public void loginScreen() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/fxui/loginpage/Login.fxml"));
        loader.setController(new LoginController());
        Parent root = loader.load();

        // Get the current stage (window) from any component (via the scene)
        Stage stage = (Stage) (id).getScene().getWindow();

        // Set the new scene on the current stage
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Validates input fields for new user creation.
     *
     * @throws IllegalArgumentException if any validation fails.
     */
    private void validateUserInput() {
        if (id.getText().trim().isEmpty() || password.getText().trim().isEmpty()
            || confirmPassword.getText().trim().isEmpty()
            || firstName.getText().trim().isEmpty()
            || lastName.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled out.");
        }

        if (!password.getText().equals(confirmPassword.getText())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (password.getText().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }

        try {
            Integer.parseInt(id.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a valid number.");
        }

        if (firstName.getText().length() < 2 || lastName.getText().length() < 2) {
            throw new IllegalArgumentException(
                "First and last name must be at least 2 characters long.");
        }
    }

    public void setModel(NewUserModel model) {
        this.model = model;
    }
}
