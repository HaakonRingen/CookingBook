package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Book;
import core.Ingredient;
import core.Recipe;
import core.Student;
import core.Users;
import fxui.Cookingbook;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;


/**
 * Integration test suite for the CookingBook application. This class tests various functionalities
 * including user creation, recipe addition, data persistence, and validation in a backend REST
 * server and JavaFX frontend.
 *
 * <p>The tests involve: </p>
 * <ul>
 * <li>Setting up a server and backing up data</li>
 * <li>Creating a user with HTTP requests to the REST server</li>
 * <li>Validating correct recipe retrieval and update</li>
 * <li>Testing the frontend's behavior with the TestFX library</li>
 * <li>Cleaning up server and restoring original data</li>
 * </ul>
 */
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest extends ApplicationTest {

    private static final String SERVER_URL = "http://localhost:8080/cookingbook/users";
    private static final Path ORIGINAL_FILE = Path.of("../restserver/SavedUsers.json");
    private static final Path BACKUP_FILE = Path.of("../restserver/SavedUsers_backup.json");
    private HttpClient client;
    private ObjectMapper objectMapper;
    private Book cookingbook;
    private Recipe recipe;


    /**
     * Sets up the initial state before running tests. Starts the server and prepares backup data
     * to restore after tests.
     * @throws IOException if there is an error copying files
     * @throws InterruptedException if thread sleep is interrupted
     */
    @BeforeAll
    public void setup() throws IOException, InterruptedException {
        if (Files.exists(ORIGINAL_FILE)) {
            Files.copy(ORIGINAL_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
        }

        if (!Files.exists(BACKUP_FILE)) {
            throw new IOException("Backup file does not exist.");
        }

        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        cookingbook = new Book();

        Ingredient kjott = new Ingredient("Pinnekjott", 1, "kg");
        Ingredient potet = new Ingredient("potet", 2, "stk");
        Ingredient stappe = new Ingredient("Stappe", 200, "g");

        ArrayList<Ingredient> julemiddag = new ArrayList<>();
        julemiddag.add(kjott);
        julemiddag.add(potet);
        julemiddag.add(stappe);

        recipe = new Recipe("Julemiddag", 
                "Pinnekjott må vannes ut, så dampes, poteter må kokes og stappa må lages", 
                "Dinner", julemiddag);

        cookingbook.addRecipe(recipe);
    }

    /**
     * Cleans up by stopping the server and restoring the original data file after all tests.
     * @throws IOException if file restoration fails
     */
    @AfterAll
    public void cleanup() throws IOException {
        if (Files.exists(BACKUP_FILE)) {
            Files.copy(BACKUP_FILE, ORIGINAL_FILE, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(BACKUP_FILE);
        }
    }

    /**
     * Tests adding a new student to the server.
     * @throws IOException if request fails
     * @throws InterruptedException if response is interrupted
     */    
    @Test
    @Order(1)
    public void addStudent() throws IOException, InterruptedException {
        Student student = new Student("Ole Nordmann", 123456, "password", cookingbook);
        String studentJson = objectMapper.writeValueAsString(student);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(studentJson))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected successful response (200)");
    }

    /**
     * Tests retrieving all users from the server and validating the initial test user.
     * @throws IOException if request fails
     * @throws InterruptedException if response is interrupted
     */
    @Test
    @Order(2)
    public void testGetAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected successful response (200)");

        Users users = objectMapper.readValue(response.body(), Users.class);
        assertTrue(users.getUsers().size() > 0, "Server should return a list of users");
        assertTrue(users.getUser(123456).getCookingbook().getRecipes().size() > 0,
                "Server should return the test user");
    }

    /**
     * Tests retrieving a specific student by ID.
     * @throws IOException if request fails
     * @throws InterruptedException if response is interrupted
     */
    @Test
    @Order(3)
    public void testGetStudentById() throws IOException, InterruptedException {
        String requestUrl = SERVER_URL + "/123456";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(requestUrl))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected successful response (200)");

        Student student = objectMapper.readValue(response.body(), Student.class);
        assertEquals("Ole Nordmann", student.getFullName(),
                "Returned student should match the requested ID");
    }

    /**
     * Tests updating a student's data.
     * @throws IOException if request fails
     * @throws InterruptedException if response is interrupted
     */    
    @Test
    @Order(4)
    public void testUpdateStudent() throws IOException, InterruptedException {
        Student updatedStudent = new Student("Ole Nordmann", 123456, "nyttpassword", cookingbook);
        String studentJson = objectMapper.writeValueAsString(updatedStudent);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL + "/" + updatedStudent.getId()))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(studentJson))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected successful response (200)");

        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL + "/" + updatedStudent.getId()))
            .GET()
            .build();

        HttpResponse<String> getResponse = client
            .send(getRequest, HttpResponse.BodyHandlers.ofString());
        Student fetchedStudent = objectMapper.readValue(getResponse.body(), Student.class);
        assertEquals("Julemiddag\tDinner\tPinnekjott må vannes ut, så dampes, poteter må kokes og "
                + "stappa må lages\tPinnekjott 1.0 kg, potet 2.0 stk, Stappe 200.0 g", 
                fetchedStudent.getCookingbook().getRecipe("Julemiddag").toString(),
                "Recipe should match the expected format");
    }

    /**
     * Launches the JavaFX application for frontend testing.
     * @param stage the primary stage for this application
     * @throws Exception if application fails to launch
     */
    @Override
    public void start(Stage stage) throws Exception {
        Cookingbook mainApp = new Cookingbook();
        mainApp.start(stage);
    }

    /**
     * Tests the frontend interaction to create a user, add a recipe, and verify display updates.
     * @throws IOException if setup fails
     * @throws InterruptedException if response is interrupted
     */
    @Test
    @Order(5)
    public void testFrontend() throws IOException, InterruptedException {
        FxRobot robot = new FxRobot();
    
        // 1. Opprett ny bruker
        robot.clickOn("#newUserBtn");
        robot.clickOn("#firstName").write("Ny");
        robot.clickOn("#lastName").write("Bruker");
        robot.clickOn("#id").write("111111");
        robot.clickOn("#password").write("passord");
        robot.clickOn("#confirmPassword").write("passord");
        waitForFxEvents();
    
        // Opprett brukeren og gå tilbake til pålogging
        robot.clickOn("#createBtn");
        robot.clickOn("#backBtn");
        waitForFxEvents();
    
        // 2. Logg inn med ny bruker
        robot.clickOn("#idField").write("111111");
        robot.clickOn("#passwordField").write("passord");
        robot.clickOn("#loginBtn");
        waitForFxEvents();
    
        // 3. Naviger til "Add recipe" og legg til ny rett
        TabPane tabPane = robot.lookup("#AnchorMain").lookup(".tab-pane").queryAs(TabPane.class);
        Tab addRecipeTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getText().equals("Add recipe"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Fane 'Add recipe' ikke funnet"));
        robot.interact(() -> tabPane.getSelectionModel().select(addRecipeTab));
    
        // Fyll inn opplysninger om "Julemiddag"
        robot.clickOn("#inputMatrett").write("Julemiddag");
        robot.clickOn("#typeRecipe").clickOn("Dinner");
        robot.clickOn("#inputBeskrivelse").write("Damp pinnekjøttet, kok poteter og lag stappa");
        waitForFxEvents();
    
        // Legg til ingredienser
        robot.clickOn("#txtIngrediesToAdd").write("pinnekjøtt");
        robot.clickOn("#txtMengdeToAdd").write("1");
        robot.clickOn("#choiceBoxMasure").clickOn("kg");
        robot.clickOn("#btnLeggTil");
    
        robot.clickOn("#txtIngrediesToAdd").write("potet");
        robot.clickOn("#txtMengdeToAdd").write("2");
        robot.clickOn("#choiceBoxMasure").clickOn("stk");
        robot.clickOn("#btnLeggTil");
    
        robot.clickOn("#txtIngrediesToAdd").write("stappe");
        robot.clickOn("#txtMengdeToAdd").write("100");
        robot.clickOn("#choiceBoxMasure").clickOn("g");
        robot.clickOn("#btnLeggTil");
        waitForFxEvents();
    
        // Sjekk at ingrediensene er lagt til i #lstIngredienser
        ListView<String> ingredientList = robot.lookup("#lstIngredienser").queryListView();
        assertTrue(ingredientList.getItems().toString().contains("pinnekjøtt 1.0 kg"));
        assertTrue(ingredientList.getItems().toString().contains("potet 2.0 stk"));
        assertTrue(ingredientList.getItems().toString().contains("stappe 100.0 g"));
    
        // Lagre retten
        robot.clickOn("#btnComplete");
        waitForFxEvents();
    
        // 4. Naviger til "Recipes" og verifiser lagret rett
        Tab recipesTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getText().equals("Recipes"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Fane 'Recipes' ikke funnet"));
        robot.interact(() -> tabPane.getSelectionModel().select(recipesTab));
    
        // Filtrer etter "Dinner" og velg "Julemiddag" i listen
        robot.clickOn("#typeRecipeView").clickOn("Dinner");
        robot.clickOn("#listViewMatretter").clickOn("Julemiddag");
    
        // Verifiser at oppskriften vises riktig
        String ingredientsText = robot.lookup("#listViewMatrett")
            .queryListView().getItems().toString();
        assertTrue(ingredientsText.contains("pinnekjøtt 1.0 kg"));
        assertTrue(ingredientsText.contains("potet 2.0 stk"));
        assertTrue(ingredientsText.contains("stappe 100.0 g"));
    
        // 5. Juster porsjon til 2 og verifiser at ingrediensene dobles
        robot.clickOn("#choiceBoxPorsjon").clickOn("2");
        ingredientsText = robot.lookup("#listViewMatrett").queryListView().getItems().toString();
        assertTrue(ingredientsText.contains("pinnekjøtt 2.0 kg"));
        assertTrue(ingredientsText.contains("potet 4.0 stk"));
        assertTrue(ingredientsText.contains("stappe 200.0 g"));
    
        // 6. Slett retten og verifiser at den er fjernet fra listen
        robot.clickOn("#btnSlett");
        ListView<String> recipeList = robot.lookup("#listViewMatretter").queryListView();
        assertFalse(recipeList.getItems().contains("Julemiddag"));
    }

    /**
     * Tests the deletefunction to the server.
     * @throws IOException if request fails
     * @throws InterruptedException if response is interrupted
     */
    @Test
    @Order(6)
    public void testDeleteRecipe() throws IOException, InterruptedException {
        cookingbook.removeRecipe(recipe);
        Student updatedStudent = new Student("Ole Nordmann", 123456, "password", cookingbook);
    
        String studentJson = objectMapper.writeValueAsString(updatedStudent);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL + "/" + updatedStudent.getId()))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(studentJson))
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected successful response (200)");
    
        // Verifiser at oppskriften er slettet
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL + "/" + updatedStudent.getId()))
            .GET()
            .build();
    
        HttpResponse<String> getResponse = client
            .send(getRequest, HttpResponse.BodyHandlers.ofString());
        Student fetchedStudent = objectMapper.readValue(getResponse.body(), Student.class);
    
        assertEquals(
            null,
            fetchedStudent.getCookingbook().getRecipe("Julemiddag"),
            "Recipe should be 'null'"
        );
    }
}