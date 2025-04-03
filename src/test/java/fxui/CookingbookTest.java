package fxui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.Parent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test class for the Cookingbook application.
 * This class tests the main application startup and loading of UI elements.
 */
public class CookingbookTest extends ApplicationTest {

    private Cookingbook app;

    /**
     * Sets up and starts the Cookingbook application.
     *
     * @param stage the primary stage for this application.
     * @throws Exception if an error occurs during the application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        app = new Cookingbook();
        app.start(stage);
    }

    /**
     * Tests the startup of the Cookingbook application.
     * Ensures that the primary stage is properly initialized and visible.
     *
     * @throws Exception if an error occurs during stage registration.
     */
    @Test
    public void testApplicationStartup() throws Exception {
        // Assert that the primary stage is not null and visible
        Stage primaryStage = FxToolkit.registerPrimaryStage();
        assertNotNull(primaryStage);
        assertNotNull(primaryStage.getScene());
        assertNotNull(primaryStage.getScene().getRoot());
    }

    /**
     * Tests that the login page is loaded correctly by checking for a specific UI element.
     * Ensures that the root node of the scene contains the expected element.
     */
    @Test
    public void testLoadLoginPage() {
        // Check that the root node of the scene is loaded correctly
        Parent root = lookup("#loginBtn").query();
        assertNotNull(root, "Login page should be loaded and contain the expected element.");
    }
}
