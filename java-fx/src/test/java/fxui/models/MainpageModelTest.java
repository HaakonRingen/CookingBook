package fxui.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import core.Book;
import core.Breakfast;
import core.Ingredient;
import core.Recipe;
import core.Student;
import fxui.UserSession;
import fxui.mainpage.Model;
import fxui.remote.CookingBookRemoteAccess;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;




/**
 * Test class for the Model class used in the main page of the application.
 * This class provides unit tests to verify the behavior of various methods
 * in the Model class, including adding, removing, and saving recipes.
 */
public class MainpageModelTest {

    @Mock
    private CookingBookRemoteAccess mockAccess;

    @Mock
    private UserSession mockSession;

    @Mock
    private Student mockStudent;

    @Mock
    private Book mockCookingBook;

    @InjectMocks
    private Model model;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and sets up common behavior for interactions.
     *
     * @throws Exception if an error occurs during setup.
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock the UserSession behavior
        when(mockSession.getId()).thenReturn(1);

        when(mockStudent.getCookingbook()).thenReturn(mockCookingBook);

        // Mock interactions with the CookingBook
        doNothing().when(mockCookingBook).addRecipe(any(Recipe.class));
        doNothing().when(mockCookingBook).removeRecipe(any(Recipe.class));
        // Arrange
        List<Recipe> mockRecipes = new ArrayList<>();
        Recipe recipe1 = new Recipe("Recipe 1");
        Recipe recipe2 = new Recipe("Recipe 2");
        mockRecipes.add(recipe1);
        mockRecipes.add(recipe2);
        when(mockCookingBook.getRecipes()).thenReturn((ArrayList<Recipe>) mockRecipes);

        // Configure the access mock to return the student
        when(mockAccess.getStudentById(1)).thenReturn(mockStudent);

        // Create the Model instance with the mocked dependencies
        model = new Model(mockSession, mockAccess);
        
    }

    /**
     * Tests saving a valid ingredient to the temporary list.
     */
    @Test
    public void testSaveToTemp_validInput() {
        this.model.saveToTemp("Flour", "100", "g");
        List<Ingredient> tempIngredients = model.getTemp();
        assertEquals(1, tempIngredients.size());
        assertEquals("Flour", tempIngredients.get(0).getIngredientName());
        assertEquals(100, tempIngredients.get(0).getAmount());
        assertEquals("g", tempIngredients.get(0).getUnit());
    }

    /**
     * Tests saving an ingredient with an invalid amount.
     * Ensures that an IllegalArgumentException is thrown with the appropriate message.
     */
    @Test
    public void testSaveToTemp_invalidAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.saveToTemp("Sugar", "invalid", "kg");
        });
        assertEquals("Amount must be a valid number.", exception.getMessage());
    }

    /**
     * Tests adding a recipe without any ingredients.
     * Ensures that an IllegalArgumentException is thrown.
     */
    @Test
    public void testAddRecipe_noIngredients() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.addRecipe("Plain Dish", "Mix nothing", "1", "Dinner");
        });
        assertEquals("Ingredients must be set.", exception.getMessage());
    }

    /**
     * Tests adding a recipe with an invalid type.
     * Ensures that an IllegalArgumentException is thrown.
     */
    @Test
    public void testAddRecipe_invalidType() {
        model.saveToTemp("Pepper", "5", "g");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.addRecipe("Pepper Dish", "Mix pepper", "2", "InvalidType");
        });
        assertEquals("Invalid recipe type: InvalidType", exception.getMessage());
    }


    /**
     * Tests removing a recipe that does not exist.
     * Ensures that an IllegalArgumentException is thrown.
     */
    @Test
    public void testRemoveRecipe_notFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.removeRecipe("Nonexistent Recipe");
        });
        assertEquals("Recipe not found: Nonexistent Recipe", exception.getMessage());
    }

    /**
     * Tests adding a breakfast recipe and verifies interactions with the mock objects.
     *
     * @throws Exception if an error occurs during the operation.
     */
    @Test
    public void testAddRecipe_Breakfast() throws Exception {
        model.saveToTemp("Egg", "2", "stk");
        model.addRecipe("Eggs Benedict", "Cook eggs", "1", "Breakfast");

        verify(mockCookingBook).addRecipe(any(Recipe.class));
    }

    /**
     * Tests that adding a recipe triggers an update to the student record.
     *
     * @throws Exception if an error occurs during the operation.
     */
    @Test
    public void testAddRecipe_VerifiesUpdateStudentCall() throws Exception {
        model.saveToTemp("Salt", "10", "g");
        model.addRecipe("Salty Dish", "Mix salt", "1", "Lunch");

        verify(mockAccess).updateStudent(1, model.getStudent());
    }

    /**
     * Tests that adding a breakfast recipe creates the correct recipe type.
     *
     * @throws Exception if an error occurs during the operation.
     */
    @Test
    public void testAddRecipe_CreatesCorrectRecipeType() throws Exception {
        model.saveToTemp("Flour", "200", "g");
        model.addRecipe("Pancake", "Mix and cook", "2", "Breakfast");

        // Capture the recipe added to the mockCookingBook
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(mockCookingBook).addRecipe(recipeCaptor.capture());

        Recipe capturedRecipe = recipeCaptor.getValue();
        assertNotNull(capturedRecipe);
        assertTrue(capturedRecipe instanceof Breakfast);
        assertEquals("Pancake", capturedRecipe.getNavn());
        assertEquals("Mix and cook", capturedRecipe.getTutorial());
        assertEquals(2, capturedRecipe.getPortions());
        assertEquals(1, capturedRecipe.getIngredients().size());
        assertEquals("Flour", capturedRecipe.getIngredients().get(0).getIngredientName());
    }

    /**
     * Tests that the copy constructor correctly copies the session.
     */
    @Test
    public void testCopyConstructor_CopiesSession() {

        Model copiedModel = new Model(model);

        assertEquals(model.getSession(), copiedModel.getSession());
    }

    @Test
    public void testRemoveRecipe() throws Exception {
        // Arrange
        String recipeName = "Test Recipe";
        Recipe mockRecipe = mock(Recipe.class);
        when(mockStudent.getCookingbook().getRecipe(recipeName)).thenReturn(mockRecipe);

        // Act
        model.removeRecipe(recipeName);

        // Assert
        // Verify that the recipe was removed from the student's cookbook
        verify(mockStudent.getCookingbook()).removeRecipe(mockRecipe);
        // Verify that the student was updated via access
        verify(mockAccess).updateStudent(mockStudent.getId(), mockStudent);
    }

    @Test
    public void testRemoveRecipe_ThrowsExceptionForNonexistentRecipe() {
        // Arrange
        String recipeName = "Nonexistent Recipe";
        when(mockStudent.getCookingbook().getRecipe(recipeName)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.removeRecipe(recipeName);
        });
        assertEquals("Recipe not found: " + recipeName, exception.getMessage());
    }

    @Test
    public void testGetRecipesForStudent() throws Exception {

        // Act
        List<Recipe> recipes = model.getRecipesForStudent();

        // Assert
        assertEquals(2, recipes.size());
        assertEquals("Recipe 1", recipes.get(0).getNavn());
        assertEquals("Recipe 2", recipes.get(1).getNavn());
    }

    /**
     * Tests adding a dinner recipe and verifies interactions with the mock objects.
     *
     * @throws Exception if an error occurs during the operation.
     */
    @Test
    public void testAddRecipe_Dinner() throws Exception {
        // Arrange
        model.saveToTemp("Potato", "3", "stk");

        // Act
        model.addRecipe("Mashed Potatoes", "Boil and mash", "4", "Dinner");

        // Assert
        verify(mockCookingBook).addRecipe(any(Recipe.class));
    }

    /**
     * Tests adding a dessert recipe and verifies interactions with the mock objects.
     *
     * @throws Exception if an error occurs during the operation.
     */
    @Test
    public void testAddRecipe_Dessert() throws Exception {
        // Arrange
        model.saveToTemp("Sugar", "100", "g");

        // Act
        model.addRecipe("Sugar Cake", "Mix and bake", "8", "Dessert");

        // Assert
        verify(mockCookingBook).addRecipe(any(Recipe.class));
    }

    /**
     * Tests setting the CookingBookRemoteAccess using the setAccess method.
     */
    @Test
    public void testSetAccess() {
        // Arrange
        CookingBookRemoteAccess newAccess = mock(CookingBookRemoteAccess.class);

        // Act
        model.setAccess(newAccess);

        // Assert
        assertEquals(newAccess, model.getAccess(), 
            "The access field should be updated to the new value.");
    }

    /**
     * Tests setting the UserSession using the setUserSession method.
     */
    @Test
    public void testSetUserSession() {
        // Arrange
        UserSession newSession = mock(UserSession.class);

        // Act
        model.setUserSession(newSession);

        // Assert
        assertEquals(newSession, model.getSession(), 
            "The session field should be updated to the new value.");
    }

    /**
     * Tests the getRecipes method to ensure it retrieves recipes from the student's Cookingbook.
     */
    @Test
    public void testGetRecipes() {

        // Act
        List<Recipe> recipes = model.getRecipes();

        // Assert
        assertNotNull(recipes, "The returned list of recipes should not be null.");
        assertEquals(2, recipes.size(), "The list should contain exactly 2 recipes.");
        assertEquals("Recipe 1", recipes.get(0).getNavn(),
              "The first recipe should be 'Recipe 1'.");
        assertEquals("Recipe 2", recipes.get(1).getNavn(),
             "The second recipe should be 'Recipe 2'.");
    }


    



}
