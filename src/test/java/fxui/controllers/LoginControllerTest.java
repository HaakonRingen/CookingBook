package fxui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fxui.loginpage.LoginController;
import fxui.remote.CookingBookRemoteAccess;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;





/**
 * Test class for the LoginController.
 * Uses TestFX and Mockito to simulate and test GUI interactions and business logic.
 */
public class LoginControllerTest extends ApplicationTest {

    @Mock
    private CookingBookRemoteAccess mockAccess;

    @InjectMocks
    private LoginController controller;

    private TextField idField;
    private PasswordField passwordField;
    private Text errorMessage;

    /**
     * Initializes the JavaFX application stage and loads the FXML for testing.
     *
     * @param stage the primary stage for testing
     * @throws Exception if loading the FXML fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxui/loginpage/Login.fxml"));
        controller = new LoginController(mockAccess);
        loader.setController(controller);
        // CHECKSTYLE.SUPPRESS: VariableDeclarationUsageDistance
        Parent root = loader.load();

        idField = (TextField) loader.getNamespace().get("idField");
        passwordField = (PasswordField) loader.getNamespace().get("passwordField");
        errorMessage = (Text) loader.getNamespace().get("errorMessage");
     

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Tests the behavior of the homeScreen method when both input fields are empty.
     * Verifies that the correct error message is displayed.
     *
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    public void testHomeScreen_InvalidInput_EmptyFields() throws IOException, InterruptedException {
        interact(() -> {
            idField.setText("");
            passwordField.setText("");
        });

        interact(() -> {
            try {
                controller.homeScreen(null);
            } catch (Exception e) {
                // Expected exception handling in controller, so no need to propagate further
            }
        });

        assertEquals(
            "Please fill in all fields", errorMessage.getText());
    }

    /**
     * Tests the behavior of the homeScreen method when a non-numeric ID is entered.
     * Verifies that the correct error message is displayed.
     */
    @Test
    public void testHomeScreen_InvalidInput_NonNumericId() {
        interact(() -> {
            idField.setText("abc");
            passwordField.setText("password");
        });

        interact(() -> {
            try {
                controller.homeScreen(null);
            } catch (Exception e) {
                // Expected exception handling in controller, so no need to propagate further
            }
        });

        assertEquals(
            "ID must be a valid number", errorMessage.getText());
    }

    /**
     * Tests the behavior of the homeScreen method when a
     *  password shorter than 4 characters is entered.
     * Verifies that the correct error message is displayed.
     */
    @Test
    public void testHomeScreen_InvalidInput_ShortPassword() {
        interact(() -> {
            idField.setText("1234");
            passwordField.setText("abc");
        });

        interact(() -> {
            try {
                controller.homeScreen(null);
            } catch (Exception e) {
                // Expected exception handling in controller, so no need to propagate further
            }
        });

        assertEquals("ID is 6 digits long, idiot",
             errorMessage.getText());
    }

    /**
     * Tests the behavior of the homeScreen method with a valid ID and password.
     * Verifies that the login is successful and no error messages are displayed.
     *
     * @throws Exception if an unexpected exception occurs
     */
    @Test
    public void testHomeScreen_ValidLogin() throws Exception {
        interact(() -> {
            idField.setText("123456");
            passwordField.setText("validpassword");
        });

        // Create a spy of the controller to partially mock it
        LoginController spyController = spy(controller);

        // Mock the backend call to validate the student
        when(mockAccess.validateStudent(123456, "validpassword")).thenReturn(true);

        // Simulate the navigateToHomeScreen() method without actually performing the navigation
        doNothing().when(spyController).navigateToHomeScreen();

        // Execute the homeScreen method using the spy
        interact(() -> {
            try {
                spyController.homeScreen(null);
            } catch (Exception e) {
                fail("homeScreen method threw an exception: " + e.getMessage());
            }
        });

        verify(mockAccess).validateStudent(123456, "validpassword");
        assertEquals("", errorMessage.getText()); // Assuming error message is cleared on success
    }

    /**
     * Tests the behavior of the homeScreen method with an invalid login attempt.
     * Verifies that the correct error message is displayed.
     *
     * @throws Exception if an unexpected exception occurs
     */
    @Test
    public void testHomeScreen_InvalidLogin() throws Exception {
        interact(() -> {
            idField.setText("123456");
            passwordField.setText("wrongpassword");
        });

        when(mockAccess.validateStudent(123456, "wrongpassword")).thenReturn(false);

        interact(() -> {
            try {
                controller.homeScreen(null);
            } catch (Exception e) {
                fail("homeScreen method threw an exception: " + e.getMessage());
            }
        });

        assertEquals("Invalid ID or password", errorMessage.getText());
    }

}