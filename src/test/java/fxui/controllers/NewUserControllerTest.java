package fxui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fxui.newuserpage.NewUserController;
import fxui.newuserpage.NewUserModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test class for {@link NewUserController}.
 * This class tests the behavior and interactions of the New User creation form.
 */
public class NewUserControllerTest extends ApplicationTest {

    @Mock
    private NewUserModel mockModel;

    @InjectMocks
    private NewUserController controller;

    private TextField id;
    private TextField confirmPassword;
    private TextField firstName;
    private TextField lastName;
    private TextField password;
    private Text errorMessage;
    private Button createBtn;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxui/newuserpage/NewUser.fxml"));
        controller = new NewUserController(mockModel);
        loader.setController(controller);
        // CHECKSTYLE.SUPPRESS: VariableDeclarationUsageDistance
        Parent root = loader.load();
        controller = loader.getController();
        controller.setModel(mockModel); // Inject mock model

        id = (TextField) loader.getNamespace().get("id");
        confirmPassword = (TextField) loader.getNamespace().get("confirmPassword");
        firstName = (TextField) loader.getNamespace().get("firstName");
        lastName = (TextField) loader.getNamespace().get("lastName");
        password = (TextField) loader.getNamespace().get("password");
        errorMessage = (Text) loader.getNamespace().get("errorMessage");
        createBtn = (Button) loader.getNamespace().get("createBtn");

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Sets up the Mockito annotations before each test is run.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that attempting to create a new user with all empty
     *  fields results in an appropriate error message.
     */
    @Test
    public void testCreateUser_AllFieldsEmpty() {
        interact(() -> {
            id.setText("");
            password.setText("");
            confirmPassword.setText("");
            firstName.setText("");
            lastName.setText("");
        });

        // Simulate clicking the "Create" button to trigger user creation
        interact(() -> createBtn.fire());

        // Assert error message displayed
        assertEquals("All fields must be filled out.", errorMessage.getText());
    }

    /**
     * Tests that a password mismatch between the entered 
     * password and confirmation password results in an error message.
     */
    @Test
    public void testCreateUser_PasswordMismatch() {
        interact(() -> {
            id.setText("1234");
            password.setText("password");
            confirmPassword.setText("different");
            firstName.setText("John");
            lastName.setText("Doe");
        });

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        // Assert error message displayed
        assertEquals("Passwords do not match.", errorMessage.getText());
    }

    /**
     * Tests that entering a short password
     *  (less than the required length) results in an error message.
     */
    @Test
    public void testCreateUser_ShortPassword() {
        interact(() -> {
            id.setText("1234");
            password.setText("123");
            confirmPassword.setText("123");
            firstName.setText("John");
            lastName.setText("Doe");
        });

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        // Assert error message displayed
        assertEquals("Password must be at least 6 characters long.", errorMessage.getText());
    }

    /**
     * Tests that entering a non-numeric ID results in an error message.
     */
    @Test
    public void testCreateUser_InvalidId() {
        interact(() -> {
            id.setText("abc");
            password.setText("password");
            confirmPassword.setText("password");
            firstName.setText("John");
            lastName.setText("Doe");
        });

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        // Assert error message displayed
        assertEquals("ID must be a valid number.", errorMessage.getText());
    }

    /**
     * Tests that entering a short first name 
     * (less than the required length) results in an error message.
     */
    @Test
    public void testCreateUser_ShortFirstName() {
        interact(() -> {
            id.setText("1234");
            password.setText("password");
            confirmPassword.setText("password");
            firstName.setText("J");
            lastName.setText("Doe");
        });

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        // Assert error message displayed
        assertEquals("First and last name must be at least 2 characters long.", 
            errorMessage.getText());
    }

    /**
     * Tests that entering valid user details and 
     * triggering user creation results in successful user creation.
     */
    @Test
    public void testCreateUser_SuccessfulCreation() {
        interact(() -> {
            id.setText("1234");
            password.setText("password123");
            confirmPassword.setText("password123");
            firstName.setText("John");
            lastName.setText("Doe");
        });

        when(mockModel.getSuccess()).thenReturn(true);

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        verify(mockModel).createUser("John", "Doe", "1234", "password123", "password123");
        assertEquals("User created successfully", errorMessage.getText());
    }

    /**
     * Tests that a failed user creation 
     * (based on model interaction) displays an appropriate error message.
     */
    @Test
    public void testCreateUser_FailedCreation() {
        interact(() -> {
            id.setText("1234");
            password.setText("password123");
            confirmPassword.setText("password123");
            firstName.setText("John");
            lastName.setText("Doe");
        });

        when(mockModel.getSuccess()).thenReturn(false);

        // Simulate clicking the "Create" button
        interact(() -> createBtn.fire());

        verify(mockModel).createUser("John", "Doe", "1234", "password123", "password123");
        assertEquals("User creation failed. Please try again.", errorMessage.getText());
    }

    /**
     * Tests that navigating to the login screen works without throwing an exception.
     */
    @Test
    public void testLoginScreenNavigation() {
        interact(() -> {
            try {
                controller.loginScreen();
            } catch (Exception e) {
                fail("loginScreen method threw an exception: " + e.getMessage());
            }
        });

       
    }
}
