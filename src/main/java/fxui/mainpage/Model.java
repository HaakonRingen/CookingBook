package fxui.mainpage;

import core.Breakfast;
import core.Dessert;
import core.Dinner;
import core.Ingredient;
import core.Lunch;
import core.Recipe;
import core.Student;
import fxui.UserSession;
import fxui.remote.CookingBookRemoteAccess;
import java.util.ArrayList;


/**
 * Model for the Cookbook application, managing dinners and ingredients.
 */
public final class Model {

    private UserSession session;
    private Student student; // The logged-in student (cookbook owner)
    private ArrayList<Ingredient> tempIngredients; // Temporary ingredient list for new dinner
    private CookingBookRemoteAccess access;

    /**
     * Constructs a new Model object using the default session and access implementations.
     * Initializes a temporary list of ingredients and sets up the student's cookbook.
     * This constructor is typically used in production scenarios.
     *
     * @throws Exception if an error occurs during initialization, 
        such as retrieving the student data.
     */
    public Model() throws Exception {
        this.session = UserSession.getInstance(0);
        this.tempIngredients = new ArrayList<>();
        this.access = new CookingBookRemoteAccess();
        this.student = getStudent();
    }

    

    /**
     * Constructs a new Model object by copying data from another Model object.
     * This constructor is useful for creating a copy of an existing model, including its session
     * and temporary ingredients list.
     *
     * @param model the Model object to copy from
     */
    public Model(Model model) {
        this.session = model.session;
        this.tempIngredients = (model.tempIngredients != null) ? new ArrayList<>(
            model.tempIngredients) : new ArrayList<>();
    }

    /**
     * Constructs a new Model object using a provided UserSession and CookingBookRemoteAccess.
     * This constructor is primarily intended for testing purposes, allowing custom sessions
     * and remote access objects to be injected for controlled behavior during tests.
     * Initializes a temporary list of ingredients and retrieves the associated student data.
     *
     * @param session the UserSession to use for retrieving the student data
     * @param access the CookingBookRemoteAccess for interacting with remote data
     * @throws Exception if an error occurs while retrieving the student data
     */
    public Model(UserSession session, CookingBookRemoteAccess access) throws Exception {
        this.session = session;
        this.tempIngredients = new ArrayList<>();
        this.access = access;
        this.student = getStudent(); // Calls access.getStudentById() using the provided access
    }

    public Student getStudent() throws Exception {
        return access.getStudentById(session.getId());
    }

    public ArrayList<Recipe> getRecipesForStudent() throws Exception {
        return this.access.getStudentById(session.getId()).getCookingbook().getRecipes();
    }

    /**
     * Saves an ingredient to the temporary list.
     *
     * @param name   the name of the ingredient
     * @param amount the amount of the ingredient
     * @param unit   the measurement unit of the ingredient
     */
    public void saveToTemp(String name, String amount, String unit) {
        validateIngredient(name, amount, unit); // Verify input parameters
        if (amount.equals("")) {
            amount = "1"; // Default amount to 1 if empty
        }
        Ingredient ingredient = new Ingredient(name, Float.parseFloat(amount), unit);
        tempIngredients.add(ingredient);
    }

    public ArrayList<Ingredient> getTemp() {
        return new ArrayList<Ingredient>(tempIngredients);
    }



    /**
     * Adds a new Recipe to the cookbook and resets the temporary ingredients.
     *
     * @param name     the name of the recipe
     * @param tutorial the tutorial for preparing the recipe
     * @param portions the number of portions for the recipe
     * @throws Exception if the recipe cannot be added
     */
    public void addRecipe(String name, String tutorial, String portions, String type) 
        throws Exception {
        validateRecipeInput(name, tutorial, portions, type); // Verify input parameters
        if (tempIngredients.isEmpty()) {
            throw new IllegalArgumentException("Ingredients must be set.");
        }
        Recipe recipe = null;

        if (type.equals("Breakfast")) {
            recipe = new Breakfast(name);
        } else if (type.equals("Lunch")) {
            recipe = new Lunch(name);
        } else if (type.equals("Dinner")) {
            recipe = new Dinner(name);
        } else if (type.equals("Dessert")) {
            recipe = new Dessert(name);
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        recipe.setPortions(Integer.parseInt(portions));
        recipe.setTutorial(tutorial);
        recipe.setIngredients(tempIngredients);
        student.getCookingbook().addRecipe(recipe);
        access.updateStudent(student.getId(), student);

        tempIngredients = new ArrayList<>();


    }
        
    
    /**
     * Removes a recipe from the cookbook using its name.
     *
     * @param recipeName the name of the recipe to be removed
     * @throws Exception if the recipe cannot be found or removed
     */
    public void removeRecipe(String recipeName) throws Exception {
        Recipe recipe = getRecipe(recipeName);

        student.getCookingbook().removeRecipe(recipe);

        access.updateStudent(student.getId(), student);

    }

    /**
     * Returns a list of all recipes in the cookbook.
     *
     * @return a new ArrayList containing all recipes
     */
    public ArrayList<Recipe> getRecipes() {
        return new ArrayList<Recipe>(student.getCookingbook().getRecipes());
    }

    /**
     * Gets a recips from the cookbook using its name.
     *
     * @param name the name of the recips to be retrieved
     * @return the matching Recipe object
     * @throws IllegalArgumentException if the recipe is not found
     */
    public Recipe getRecipe(String name) {
        Recipe recipe = student.getCookingbook().getRecipe(name);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found: " + name);
        }
        return recipe;
    }

    public ArrayList<Recipe> getRecipeByStudentId() throws Exception {
        return access.getStudentById(session.getId())
            .getCookingbook().getRecipes();
    }

    /**
     * Validates the input parameters for saving an ingredient.
     *
     * @param name   the name of the ingredient
     * @param amount the amount of the ingredient
     * @param unit   the measurement unit of the ingredient
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateIngredient(String name, String amount, String unit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be null or empty.");
        }
        if (amount == null || !amount.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Amount must be a valid number.");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
    }

    /**
     * Validates the input parameters for adding a recipe.
     *
     * @param name     the name of the recipe
     * @param tutorial the tutorial for preparing the recipe
     * @param portions the number of portions
     * @param type     the type of the recipe
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateRecipeInput(String name, String tutorial, String portions, String type) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or empty.");
        }
        if (tutorial == null || tutorial.trim().isEmpty()) {
            throw new IllegalArgumentException("Tutorial cannot be null or empty.");
        }
        if (portions == null || !portions.matches("\\d+")) {
            throw new IllegalArgumentException("Portions must be a valid integer.");
        }
        if (type == null || (!type.equals("Breakfast") && !type.equals("Lunch")
            && !type.equals("Dinner") && !type.equals("Dessert"))) {
            throw new IllegalArgumentException("Invalid recipe type: " + type);
        }
    }

    public void setAccess(CookingBookRemoteAccess access) {
        this.access = access;
    }

    public void setUserSession(UserSession session) {
        this.session = session;
    }

    public UserSession getSession() {
        return this.session;
    }

    public CookingBookRemoteAccess getAccess() {
        return this.access;
    }

}
