package fxui.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import core.Book;
import core.Ingredient;
import core.Recipe;
import core.Student;
import fxui.UserSession;
import fxui.mainpage.Controller;
import fxui.mainpage.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;






/**
 * Test class for {@link MainPageController}.
 * This class tests the functionality and UI interactions
 * for the main page of the application using the TestFX framework.
 * The tests include interaction with UI elements such as buttons, choice boxes, 
 * list views, and other components.
 * It also verifies the integration with the underlying model using mocks.
 */
public class MainPageControllerTest extends ApplicationTest {

    @Mock
    private Model mockModel;

    @InjectMocks
    private Controller controllerInstance;

    private Book cookingBook = new Book();


    private Student setupStudent() {
        Recipe recipe1 = new Recipe("Pasta", "Kok pasta", "Dinner",
            new ArrayList<>(List.of(new Ingredient("Pasta", 100, "g"))));
        Recipe recipe2 = new Recipe("Salad", "Kok salat", "Lunch",
            new ArrayList<>(List.of(new Ingredient("Salad", 100, "g"))));
        cookingBook.addRecipe(recipe1);
        cookingBook.addRecipe(recipe2);
        Student student = new Student("Ola Nordmann", 123456, "passord", cookingBook);
        return student;
    }


    /**
     * Initializes the JavaFX application context and loads the main page UI for testing.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws Exception if there is any issue loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockModel.getRecipeByStudentId())
            .thenReturn(setupStudent().getCookingbook().getRecipes());
        when(mockModel.getStudent()).thenReturn(setupStudent());

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxui/mainpage/HomeScreen.fxml"));

        controllerInstance = new Controller(mockModel);
        loader.setController(controllerInstance);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.show();
    }

    
    /**
     * Sets up the test environment by initializing mocks 
     * and configuring mock behaviors before each test.
     * @throws Exception if any error occurs during setup.
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(mockModel.getRecipe("Test Recipe")).thenReturn(new Recipe("Test Recipe"));


        // Initialize UI elements (simulate initialization if needed)
        Platform.runLater(() -> controllerInstance.initialize());
        waitForFxEvents();  // Make sure all FX events are processed


    }
    

    /**
     * Tests the initialization of UI elements, such as choice boxes, 
     * to ensure that they are properly set up.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInitializeUiElements() {

        // Validate choice boxes and default selections
        // Lookup elements using TestFX's lookup mechanism
        ChoiceBox<Integer> choiceBoxPorsjon = lookup("#choiceBoxPorsjon").queryAs(ChoiceBox.class);
        ChoiceBox<String> choiceBoxMasure = lookup("#choiceBoxMasure").queryAs(ChoiceBox.class);
        ChoiceBox<String> typeRecipe = lookup("#typeRecipe").queryAs(ChoiceBox.class);

        // Validate that the elements are not null
        assertNotNull(choiceBoxPorsjon);
        assertNotNull(choiceBoxMasure);
        assertNotNull(typeRecipe);

        // Validate default values
        assertEquals(1, choiceBoxPorsjon.getValue());
        assertEquals("g", choiceBoxMasure.getValue());
    }

    /**
     * Tests the functionality of adding an ingredient through 
     * the UI and verifies interaction with the model.
     *
     * @throws Exception if any error occurs during the test.
     */
    @Test
    public void testAddIngredient() throws Exception {
        // Populate input fields
        TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
        Tab addRecipeTab = tabPane.getTabs().stream()
                                  .filter(tab -> "Add recipe".equals(tab.getText()))
                                  .findFirst()
                                  .orElseThrow(() -> new Exception("Add recipe tab not found"));
    
        tabPane.getSelectionModel().select(addRecipeTab);
        TextField txtIngrediesToAdd = lookup("#txtIngrediesToAdd").queryAs(TextField.class);
        TextField txtMengdeToAdd = lookup("#txtMengdeToAdd").queryAs(TextField.class);
        @SuppressWarnings("unchecked")
        ChoiceBox<String> choiceBoxMasure = lookup("#choiceBoxMasure").queryAs(ChoiceBox.class);

        // Simulate user input
        interact(() -> {
            txtIngrediesToAdd.setText("Flour");
            txtMengdeToAdd.setText("100");
            choiceBoxMasure.setValue("g");
        });

        // Click the "Add" button
        Button btnLeggTil = lookup("#btnLeggTil").queryAs(Button.class);
        clickOn(btnLeggTil);

        // Verify the model interaction
        verify(mockModel).saveToTemp("Flour", "100", "g");
    }


    /**
     * Tests whether the FXML file loads correctly and 
     * verifies that essential UI components are present.
     */
    @Test
    public void testFxmlLoad() {
        assertThat(lookup("#listViewMatretter").queryListView()).isNotNull();
        assertThat(lookup("#choiceBoxPorsjon").queryAs(ChoiceBox.class)).isNotNull();
        assertThat(lookup("#btnComplete").queryButton()).isNotNull();
        assertThat(lookup("#choiceBoxMasure").queryAs(ChoiceBox.class)).isNotNull();
        assertThat(lookup("#typeRecipe").queryAs(ChoiceBox.class)).isNotNull();
    }



    /**
     * Tests completing a recipe by interacting with the 
     * "Complete" button and verifies model interaction.
     *
     * @throws Exception if any error occurs during the test.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCompleteButtonAction() throws Exception {
        // Simulate user input
        TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
        Tab addRecipeTab = tabPane.getTabs().stream()
                                  .filter(tab -> "Add recipe".equals(tab.getText()))
                                  .findFirst()
                                  .orElseThrow(() -> new Exception("Add recipe tab not found"));
    
        tabPane.getSelectionModel().select(addRecipeTab);
        


        TextField inputMatrett = lookup("#inputMatrett").queryAs(TextField.class);
        TextArea inputBeskrivelse = lookup("#inputBeskrivelse").queryAs(TextArea.class);
        ChoiceBox<String> typeRecipe = lookup("#typeRecipe").queryAs(ChoiceBox.class);
        

        interact(() -> {
            inputMatrett.setText("Pizza");
            inputBeskrivelse.setText("Delicious homemade pizza");
            typeRecipe.setValue("Dinner");
            ((ChoiceBox<String>) lookup("#choiceBoxMasure").query()).setValue("g");

        });
        clickOn("#txtIngrediesToAdd");
        write("Tomat");
        clickOn("#txtMengdeToAdd");
        write("2"); 
        clickOn("#btnLeggTil");
        waitForFxEvents();

        // Click the "Complete" button
        Button btnComplete = lookup("#btnComplete").queryAs(Button.class);
        clickOn(btnComplete);

        // Verify the interaction with the mock model
        verify(mockModel).addRecipe("Pizza", "Delicious homemade pizza", "1", "Dinner");
    }

    /**
     * Tests the deletion of a recipe and verifies that the correct model interaction occurs.
     *
     * @throws Exception if any error occurs during the test.
     */
    @Test
    public void testDeleteRecipe() throws Exception {
        // Simulate a selected recipe in the list view
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatretter = lookup("#listViewMatretter").queryAs(ListView.class);
        interact(() -> {
            listViewMatretter.getItems().add("Test Recipe");
            listViewMatretter.getSelectionModel().select("Test Recipe");
        });

        Button btnSlett = lookup("#btnSlett").queryAs(Button.class);
        clickOn(btnSlett);

        // Verify the model interaction for deletion
        verify(mockModel).removeRecipe("Test Recipe");
    }

    /**
     * Tests the initialization method to ensure UI components are correctly linked and accessible.
     */
    @Test
    public void testInitializeMethod() {
        // Sett opp mock på forhånd
        var recipeMock = mock(Recipe.class);
        when(mockModel.getRecipe("Test Recipe")).thenReturn(recipeMock);
        when(recipeMock.getIngredients()).thenReturn(new ArrayList<>(
            List.of(new Ingredient("Flour", 100, "g"))));

        // Valider UI-elementene
        assertNotNull(lookup("#listViewMatretter").queryAs(ListView.class));
        assertNotNull(lookup("#choiceBoxPorsjon").queryAs(ChoiceBox.class));
        assertNotNull(lookup("#txtOutputBeskrivelse").queryAs(TextArea.class));
        assertNotNull(lookup("#listViewMatrett").queryAs(ListView.class));
        
    }

    /**
     * Tests the ListView mouse click event, which updates displayed recipe data in the UI.
     */
    @Test
    public void testListViewMatretterOnMouseClicked() {
        // Mock a recipe to be returned by the model
        Recipe mockRecipe = new Recipe("Test Recipe");
        mockRecipe.setTutorial("Test Tutorial");
        mockRecipe.setIngredients(new ArrayList<>(
            List.of(new Ingredient("Ingredient1", 100, "g"))));
        when(mockModel.getRecipe("Test Recipe")).thenReturn(mockRecipe);

        // Simulate adding an item to listViewMatretter
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatretter = lookup("#listViewMatretter").queryAs(ListView.class);
        Platform.runLater(() -> {
            listViewMatretter.getItems().add("Test Recipe");
            listViewMatretter.getSelectionModel().select("Test Recipe");
        });
        waitForFxEvents();

        // Simulate mouse click on the item
        clickOn(listViewMatretter, MouseButton.PRIMARY);

        // Use TestFX lookup to get the Text nodes
        TextArea txtOutputBeskrivelse = lookup("#txtOutputBeskrivelse").queryAs(TextArea.class);


        // Verify the expected interactions
        verify(mockModel).getRecipe("Test Recipe");
        assertEquals("Test Tutorial", txtOutputBeskrivelse.getText());
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatrett = lookup("#listViewMatrett").queryAs(ListView.class);
        assertTrue(listViewMatrett.getItems().contains("1. Ingredient1 100.0 g"));
    }

    /**
     * Tests the change in portion size through a choice box and
     *  verifies that the ingredient quantities update correctly.
     */
    @Test
    public void testChoiceBoxPorsjonOnChange() {
        // Mock a recipe returned by the model
        Recipe mockRecipe = new Recipe("Test Recipe");
        mockRecipe.setIngredients(new ArrayList<>(
            List.of(new Ingredient("Ingredient1", 100, "g"))));
        when(mockModel.getRecipe("Test Recipe")).thenReturn(mockRecipe);
    
        // Simulate adding an item to listViewMatretter and selecting it
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatretter = lookup("#listViewMatretter").queryAs(ListView.class);
        Platform.runLater(() -> {
            listViewMatretter.getItems().add("Test Recipe");
            listViewMatretter.getSelectionModel().select("Test Recipe");
        });
        waitForFxEvents();
    
        // Simulate a click event on the listView to load the recipe data
        clickOn(listViewMatretter, MouseButton.PRIMARY);
        waitForFxEvents();  // Ensure FX events are processed
    
        // Change the portion count
        @SuppressWarnings("unchecked")
        ChoiceBox<Integer> choiceBoxPorsjon = lookup("#choiceBoxPorsjon").queryAs(ChoiceBox.class);
        Platform.runLater(() -> choiceBoxPorsjon
            .getSelectionModel().select(1));
        waitForFxEvents();
    
        // Verify updated ingredients are displayed with adjusted amounts
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatrett = lookup("#listViewMatrett").queryAs(ListView.class);
        assertEquals(List.of("1. Ingredient1 200.0 g"), listViewMatrett.getItems());

    }
    

    /**
     * Tests the hover effects for the "Complete" button by simulating mouse events.
     */
    @Test
    public void testBtnCompleteHoverEffects() {
        Button btnComplete = lookup("#btnComplete").queryAs(Button.class);
        assertNotNull(btnComplete);

        // Simulate mouse enter
        moveTo(btnComplete);

        // Simulate mouse exit
        moveTo(0, 0);
    }

    /**
     * Tests filtering of recipes based on type selection in the UI.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTypeRecipeViewOnChange() {
        // Create mock recipes with types set via constructor
        Recipe breakfastRecipe = new Recipe(
            "Breakfast Recipe", "Breakfast Tutorial", "Breakfast", new ArrayList<>());
        Recipe lunchRecipe = new Recipe(
            "Lunch Recipe", "Lunch Tutorial", "Lunch", new ArrayList<>());
        Recipe dinnerRecipe = new Recipe(
            "Dinner Recipe", "Dinner Tutorial", "Dinner", new ArrayList<>());
    
        // Mock the model to return a list of recipes
        List<Recipe> mockRecipes = List.of(breakfastRecipe, lunchRecipe, dinnerRecipe);
        when(mockModel.getRecipes()).thenReturn(new ArrayList<>(mockRecipes));
    
        // Simulate selecting "Lunch" in the typeRecipeView ChoiceBox
        ChoiceBox<String> typeRecipeView = lookup("#typeRecipeView").queryAs(ChoiceBox.class);
        Platform.runLater(() -> typeRecipeView.getSelectionModel().select("Lunch"));
        waitForFxEvents();
    
        // Verify that the filtered list in listViewMatretter is updated correctly
        ListView<String> listViewMatretter = lookup("#listViewMatretter").queryAs(ListView.class);
        assertEquals(1, listViewMatretter.getItems().size());
        assertTrue(listViewMatretter.getItems().contains("Lunch Recipe"));
    
        
    }

    /**
     * Tests updating of recipe display when a recipe is selected from the ListView.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testListViewMatretterOnClickUpdatesOutput() {
        // Create a mock recipe
        Recipe mockRecipe = new Recipe("Test Recipe");
        mockRecipe.setTutorial("Test Tutorial");
        mockRecipe.setIngredients(new ArrayList<>(
            List.of(new Ingredient("Ingredient1", 100, "g"))));
        when(mockModel.getRecipe("Test Recipe")).thenReturn(mockRecipe);
    
        // Simulate adding an item to listViewMatretter
        Platform.runLater(() -> {
            ListView<String> listViewMatretter = lookup(
                "#listViewMatretter").queryAs(ListView.class);
            listViewMatretter.getItems().add("Test Recipe");
            listViewMatretter.getSelectionModel().select("Test Recipe");
            listViewMatretter.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1, true, true, true, true, 
                true, true, true, true, true, true, null));
        });
        waitForFxEvents();
    
        // Check the state of listViewMatrett and txtOutputBeskrivelse
        ListView<String> listViewMatrett = lookup("#listViewMatrett").queryAs(ListView.class);
        assertEquals(1, listViewMatrett.getItems().size());
        assertEquals("1. Ingredient1 100.0 g", listViewMatrett.getItems().get(0));
    
        // Check the state of txtOutputBeskrivelse if it is accessible or mock accordingly
        TextArea txtOutputBeskrivelse = lookup("#txtOutputBeskrivelse").queryAs(TextArea.class);
        assertEquals("Test Tutorial", txtOutputBeskrivelse.getText());
    }

    /**
     * Tests the constructor of the controller class
     *  with a model argument to ensure proper initialization.
     */
    @Test
    public void testControllerConstructorWithModel() {
        // Create a mock Model object
        Model mockModel = mock(Model.class);

        // Instantiate the Controller with the mock Model
        Controller controller = new Controller(mockModel);

        // Verify that the model was set correctly 
        assertNotNull(controller);
        assertEquals(mockModel, controller.getModel());
    }

    /**
     * Tests the behavior of attempting to delete a recipe
     *  with no selection and verifies the error message display.
     */
    @Test
    public void testDeleteWithNoSelectionShowsError() {
        // Ensure no item is selected in listViewMatretter
        Platform.runLater(() -> {
            @SuppressWarnings("unchecked")
            ListView<String> listViewMatretter = lookup(
                "#listViewMatretter").queryAs(ListView.class);
            listViewMatretter.getSelectionModel().clearSelection(); // Ensure nothing is selected
            controllerInstance.delete(new ActionEvent()); // Trigger the delete method
        });
        waitForFxEvents();

        // Verify that the error is shown
        Platform.runLater(() -> {
            AnchorPane errorPane = lookup("#errorPane").queryAs(AnchorPane.class);
            assertTrue(errorPane.isVisible(), 
                "The errorPane should be visible after trying to delete with no selection");

            // Optionally verify the error text if needed
            Text errorText = lookup("#errorText").queryAs(Text.class);
            assertEquals(
                "En feil oppstod: No dinner selected for deletion.", errorText.getText(), 
                "Expected error message did not match");
        });
        waitForFxEvents();
    }

    
    
    /**
     * Tests the logout functionality to verify the session 
     * is properly cleared and UI state changes occur.
     *
     * @throws Exception if any error occurs during the test.
     */
    @Test
    public void testLogOut() throws Exception {
        // Ensure UserSession is initialized before testing log out
        UserSession.getInstance(123); // Create a session instance with a test ID

        // Simulate a logout action
        MenuButton optionMenu = lookup("#optionMenu").queryAs(MenuButton.class);
        Platform.runLater(() -> {
            try {
                controllerInstance.logOut(new ActionEvent(optionMenu, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        waitForFxEvents();

        // Verify that the session has been logged off
        assertNull(UserSession.getCurrentInstance(), 
            "Expected UserSession instance to be null after logout");
    }

    /**
     * Tests the default constructor of the Controller class,
     * verifying that the model is initialized correctly.
     */
    @Test
    public void testDefaultConstructor() {
        try {
            Controller controller = new Controller(mockModel); // Calls the constructor

            // Set the mock model through the setter to simulate behavior
            controller.setModel(mockModel);

            // Check that the model is not null after initialization
            assertNotNull(controller.getModel(), "Expected the model to be initialized");

        } catch (Exception e) {
            // If an exception occurs, the test should fail with the exception message
            fail("Initialization of the Controller failed: " + e.getMessage());
        }
    }


    /**
     * Tests changing the portion size using the choiceBoxPorsjon
     * and verifies that the displayed ingredients are updated accordingly.
     */
    @Test
    public void testChangePortionSizeUpdatesIngredients() {
        // Mock a recipe returned by the model with ingredients
        Recipe mockRecipe = new Recipe("Test Recipe");
        mockRecipe.setIngredients(new ArrayList<>(
            List.of(new Ingredient("Sugar", 100, "g"))
        ));
        when(mockModel.getRecipe("Test Recipe")).thenReturn(mockRecipe);

        // Simulate adding an item to listViewMatretter and selecting it
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatretter = lookup("#listViewMatretter").queryAs(ListView.class);
        Platform.runLater(() -> {
            listViewMatretter.getItems().add("Test Recipe");
            listViewMatretter.getSelectionModel().select("Test Recipe");
        });
        waitForFxEvents();  // Ensure FX events are processed

        // Simulate a click event on the listView to load the recipe data
        clickOn(listViewMatretter, MouseButton.PRIMARY);
        waitForFxEvents();  // Ensure FX events are processed

        // Change the portion count using the ChoiceBox
        @SuppressWarnings("unchecked")
        ChoiceBox<Integer> choiceBoxPorsjon = lookup("#choiceBoxPorsjon").queryAs(ChoiceBox.class);
        Platform.runLater(() -> choiceBoxPorsjon.getSelectionModel()
            .select(1)); // Select portion size 2
        waitForFxEvents();  // Ensure FX events are processed

        // Verify updated ingredients are displayed with adjusted amounts
        @SuppressWarnings("unchecked")
        ListView<String> listViewMatrett = lookup("#listViewMatrett").queryAs(ListView.class);
        assertEquals(1, listViewMatrett.getItems().size(),
             "Expected one ingredient in the list view");
        assertEquals("1. Sugar 200.0 g", listViewMatrett.getItems().get(0),
            "Expected ingredient amount to be doubled for portion size 2");
    }



}
    




    