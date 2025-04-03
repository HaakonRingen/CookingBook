package fxui.loginpage;


import fxui.UserSession;
import fxui.mainpage.Controller;
import fxui.newuserpage.NewUserController;
import fxui.remote.CookingBookRemoteAccess;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;



/**
 * Controller class for handling login-related actions.
 */
public class LoginController {

    CookingBookRemoteAccess access;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorMessage;

    @FXML
    private Button loginBtn;

    @FXML
    private Button newUserBtn;


    /**
     * Constructs a new LoginController and initializes the remote access.
     */
    public LoginController() {
        try {
            access = new CookingBookRemoteAccess();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading remote access");
        }
    }

    public LoginController(CookingBookRemoteAccess access) {
        this.access = access;
    }

    /**
     * Initializes the controller class. Sets up animations for buttons.
     */
    @FXML
    public void initialize() {
        typewriterEffect("Cooking book");
        setupButtonAnimations(loginBtn);
        setupButtonAnimations(newUserBtn);
    }


    /**
     * Handles the action event for navigating to the home screen.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void homeScreen(ActionEvent event) throws IOException {
        try {
            validateLoginInput(); // Validate input fields

            int id = Integer.parseInt(idField.getText().trim());
            String password = passwordField.getText();

            if (access.validateStudent(id, password)) {
                UserSession.getInstance(id);
                navigateToHomeScreen();
            } else {
                errorMessage.setText("Invalid ID or password");
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("ID must be a valid number");
        } catch (IOException e) {
            errorMessage.setText("Failed to proceed. Is the server up and running?");
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
        }
    }

    /**
     * Validates input fields for login.
     *
     * @throws IllegalArgumentException if any validation fails
     */
    private void validateLoginInput() {
        if (idField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Please fill in all fields");
        }

        try {
            Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a valid number");
        }

        if (idField.getText().length() != 6) {
            throw new IllegalArgumentException("ID is 6 digits long, idiot");
        }

        if (passwordField.getText().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }
    }

    /**
     * Navigates to the home screen.
     *
     * @throws IOException if an I/O error occurs
     */
    public void navigateToHomeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/fxui/mainpage/HomeScreen.fxml"));
        loader.setController(new Controller());
        Parent root = loader.load();
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    

    /**
     * Handles the action event for navigating to the new user screen.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void newUserScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/fxui/newuserpage/NewUser.fxml"));
        loader.setController(new NewUserController());
        Parent root = loader.load();

        // Get the current stage (window) from any component (via the scene)
        Stage stage = (Stage) (idField).getScene().getWindow();

        // Set the new scene on the current stage
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void typewriterEffect(String fullText) {
        Timeline timeline = new Timeline();
        double baseDelay = 200;
        double cumulativeDelay = 1000;
    
        for (int i = 0; i < fullText.length(); i++) {
            final int index = i;
            double delay = (fullText.charAt(i) == ' ') ? baseDelay * 2 : baseDelay;
            
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(cumulativeDelay), 
                    e -> headerLabel.setText(fullText.substring(0, index + 1)))
            );
            
            cumulativeDelay += delay;
        }
        timeline.play();
    }

    private void setupButtonAnimations(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.3);
        scaleUp.setToY(1.3);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    public void setAccess(CookingBookRemoteAccess access) {
        this.access = access;
    }

}