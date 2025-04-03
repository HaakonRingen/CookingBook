package fxui.mainpage;

import core.Ingredient;
import core.Recipe;
import fxui.UserSession;
import fxui.loginpage.LoginController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;



/**
 * Controller for the Cookbook application, managing user interactions and model updates.
 */
public class Controller {
    
    private Model model;

    ObservableList<Integer> porsjoner = FXCollections.observableArrayList(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    );
    ObservableList<String> measure = FXCollections.observableArrayList(
        "L", "dl", "cl", "ml", "kg", "g", "ts", "ss", "stk"
    );
    ObservableList<String> lstTypeRecipe = FXCollections.observableArrayList(
        "Breakfast", "Lunch", "Dinner", "Dessert"
    );
    ObservableList<String> lstTypeRecipeView = FXCollections.observableArrayList(
        "Breakfast", "Lunch", "Dinner", "Dessert", "All"
    );

    @FXML
    private MenuButton optionMenu;

    @FXML
    private AnchorPane errorPane;

    @FXML
    private Button btnOkError;

    @FXML
    private Button btnComplete;

    @FXML
    private Button btnLeggTil;

    @FXML
    private Button btnSlett;

    @FXML
    private ListView<String> lstIngredienser;

    @FXML
    private ChoiceBox<String> choiceBoxMasure;

    @FXML
    private ChoiceBox<Integer> choiceBoxPorsjon;

    @FXML
    private TextArea inputBeskrivelse;

    @FXML
    private TextField inputMatrett;

    @FXML
    private ListView<String> listViewMatrett;

    @FXML
    private ListView<String> listViewMatretter;

    @FXML
    private TextField txtIngrediesToAdd;

    @FXML
    private TextField txtMengdeToAdd;

    @FXML
    private TextArea txtOutputBeskrivelse;

    @FXML
    private Text txtOutputMatrett;

    @FXML
    private Text errorText;

    @FXML
    private ChoiceBox<String> typeRecipe;


    @FXML
    private ChoiceBox<String> typeRecipeView;

    /**
     * Constructs a new Controller and initializes the model and remote access.
     */
    public Controller() {
        try {
            if (this.model == null) {
                model = new Model(); // Initialize the model
            }
        } catch (Exception e) {
            showError("Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    
    }
    
    /**
     * Constructs a new Controller with the specified model.
     *
     * @param model the model to use for the controller
     */
    public Controller(Model model) {
        this.model = model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Initializes the controller and sets up the UI elements.
     */
    @FXML
    public void initialize() {
        try {
            if (model == null) {
                model = new Model();
            }


            initializeUiElements();
            updateDinners(); // Update recipe list
            updateIngredients(); // Reset temporary ingredients

            listViewMatretter.setOnMouseClicked(event -> {
                String selectedItem = listViewMatretter.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    var recipe = model.getRecipe(selectedItem);
                    String name = recipe.getNavn();
                    String tutorial = recipe.getTutorial();
                
                    var ingredients = recipe.getIngredients();
                    List<String> numberedIngredients = new ArrayList<>();
                
                    for (int i = 0; i < ingredients.size(); i++) {
                        numberedIngredients.add((i + 1) + ". " + ingredients.get(i));
                    }
    
                    var ingredientsObservable = FXCollections.observableArrayList(
                        numberedIngredients);
    
                    txtOutputMatrett.setText(name);
                    txtOutputBeskrivelse.setText(tutorial);
                    listViewMatrett.setItems(ingredientsObservable);
                    listViewMatrett.setItems(
                        FXCollections.observableArrayList(numberedIngredients));
                }
            });

            choiceBoxPorsjon.setOnAction(event -> {
                int porsjoner = choiceBoxPorsjon.getSelectionModel().getSelectedItem();
    
                var recipe = model.getRecipe(txtOutputMatrett.getText());
                List<Ingredient> ingredients = recipe.getIngredients();
    
                List<String> updatedIngredients = new ArrayList<>();
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
    
                    double adjustedAmount = ingredient.getAmount() * porsjoner;
                
                    updatedIngredients.add((i + 1) + ". " + ingredient.getIngredientName() + " " 
                        + adjustedAmount + " " + ingredient.getUnit());
                }
    
                listViewMatrett.getItems().clear();
                listViewMatrett.getItems().addAll(updatedIngredients);
            });
            typeRecipeView.setOnAction(event -> {
                updateFilteredDinners(); // Update the filtered list when the type changes
            });

            choiceBoxPorsjon.setOnAction(event -> {
                int porsjoner = choiceBoxPorsjon.getSelectionModel().getSelectedItem();
    
                var recipe = model.getRecipe(txtOutputMatrett.getText());
                List<Ingredient> ingredients = recipe.getIngredients();
    
                List<String> updatedIngredients = new ArrayList<>();
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
    
                    double adjustedAmount = ingredient.getAmount() * porsjoner;
                
                    updatedIngredients.add((i + 1) + ". " + ingredient.getIngredientName() + " " 
                        + adjustedAmount + " " + ingredient.getUnit());
                }
                listViewMatrett.getItems().clear();
                listViewMatrett.getItems().addAll(updatedIngredients);
            });

            ScaleTransition scaleUpLoginComplete = new ScaleTransition(
                Duration.millis(200), btnComplete);
            scaleUpLoginComplete.setToX(1.2);
            scaleUpLoginComplete.setToY(1.2);
    
            ScaleTransition scaleDownLoginComplete = new ScaleTransition(
                Duration.millis(200), btnComplete);
            scaleDownLoginComplete.setToX(1.0);
            scaleDownLoginComplete.setToY(1.0);
    
            // On mouse enter, scale up the button
            btnComplete.setOnMouseEntered(e -> scaleUpLoginComplete.playFromStart());
    
            // On mouse exit, scale down the button
            btnComplete.setOnMouseExited(e -> scaleDownLoginComplete.playFromStart());


        } catch (Exception e) {
            showError("Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeUiElements() {
        choiceBoxPorsjon.setItems(porsjoner);
        choiceBoxMasure.setItems(measure);
        typeRecipe.setItems(lstTypeRecipe);
        typeRecipeView.setItems(lstTypeRecipeView);

        choiceBoxPorsjon.setValue(1);
        choiceBoxMasure.setValue("g");
        typeRecipeView.setValue("All");
        txtOutputBeskrivelse.setEditable(false);
    }

    private void updateDinners() throws Exception {
        ArrayList<Recipe> recipes = model.getRecipeByStudentId();
        listViewMatretter.setItems(
            FXCollections.observableArrayList(
                recipes.stream().map(recipe -> recipe.getNavn()).collect(Collectors.toList()))
        );
    }

    /**
     * Updates the ingredient list view with the current temporary ingredients.
     */
    private void updateIngredients() {
        var ingredients = model.getTemp();
        lstIngredienser.setItems(
            FXCollections.observableArrayList(
            ingredients.stream()
                       .map(ingredient -> ingredient.toString())
                       .collect(Collectors.toList()))
        );
    }

    /**
     * Handles the action of completing the addition of a dinner.
     * Retrieves the dinner details from the input fields and adds the dinner to the model.
     * Updates the dinner list view after adding the dinner.
     *
     * @throws Exception if there is an error during the addition of the dinner
     */
    @FXML
    public void complete() throws Exception { 
        try {
            validateCompleteInput(); // Validate input before processing
            String name = inputMatrett.getText();
            String tutorial = inputBeskrivelse.getText();
            String portion = "1";
            String type = typeRecipe.getValue();
            model.addRecipe(name, tutorial, portion, type);
            updateDinners();
            inputMatrett.setText("");
            inputMatrett.setPromptText("matrett");
            inputBeskrivelse.setText("");
            inputBeskrivelse.setPromptText("beskrivelse av retten");
            lstIngredienser.getItems().clear();
        } catch (IllegalArgumentException e) {
            showError("An error occurred while adding the recipe: " + e.getMessage());
        }
    }

    /**
     * Handles the action of adding an ingredient.
    * Retrieves the ingredient details from the input fields, validates them, 
    * and saves them temporarily. Updates the ingredient list view after adding the ingredient.
     *
     * @throws IllegalArgumentException if any of the input fields are empty
     */
    @FXML
    public void leggTilKlikk() {
        try {
            validateIngredientInput(); // Validate input before adding
            String name = txtIngrediesToAdd.getText();
            String amount = txtMengdeToAdd.getText();
            String unit = choiceBoxMasure.getValue();
            if (name.isEmpty() || amount.isEmpty() || unit == null) {
                throw new IllegalArgumentException("empty String");
            }
            model.saveToTemp(name, amount, unit);
            txtIngrediesToAdd.setText("");
            txtMengdeToAdd.setText("");
            updateIngredients();
        } catch (IllegalArgumentException e) {
            this.showError(e.getMessage());
        }
    }

    private void showError(String message) {
        if (errorText != null) {
            if ("empty String".equals(message)) {
                errorText.setText("Vennligst fyll ut alle feltene før du går videre.");
            } else {
                errorText.setText("En feil oppstod: " + message);
            }
            errorPane.setVisible(true);
        } else {
            System.err.println("Error occurred: " + message); // Fallback log for debugging
        }
    }

    /**
     * Deletes the selected dinner from the list.
     *
     * @param event the ActionEvent triggered by the delete action
     */
    public void delete(ActionEvent event)  {
        try {
            // Get the selected dinner from the ListView
            String currentRecipe = listViewMatretter.getSelectionModel().getSelectedItem();
            if (currentRecipe == null) {
                showError("No dinner selected for deletion.");
                return;
            }
    
            // Remove the dinner from the model
            model.removeRecipe(currentRecipe);
    
            // Update the ListView to reflect the deletion
            updateDinners();
            txtOutputMatrett.setText("");
            txtOutputBeskrivelse.setText("");
            listViewMatrett.getItems().clear();
    
            
        } catch (Exception e) {
            showError("Error occurred while deleting dinner: " + e.getMessage());
        }

    }

    /**
     * Handles a logout event that takes the user to login screen.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void logOut(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/fxui/loginpage/Login.fxml"));
        loader.setController(new LoginController());
        Parent root = loader.load();

        Stage stage = (Stage) (optionMenu).getScene().getWindow();

        UserSession.logOff();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void hideError(ActionEvent event) {
        errorPane.setVisible(false);
    }


    /**
     * Updates the dinner list view with the filtered recipes based on the selected type.
     */
    private void updateFilteredDinners() {
        String selectedType = typeRecipeView.getSelectionModel().getSelectedItem();

        // Get all recipes
        ArrayList<Recipe> recipes = model.getRecipes();

        // Filter recipes if a specific type is selected
        List<Recipe> filteredDinners;
        if ("All".equals(selectedType)) {
            filteredDinners = recipes; // No filtering, show all recipes
        } else {
            filteredDinners = recipes.stream()
                .filter(recipe -> recipe.getType().equals(selectedType))
                .collect(Collectors.toList());
        }

        // Update the ListView with the filtered recipes
        listViewMatretter.setItems(FXCollections.observableArrayList(
            filteredDinners.stream().map(Recipe::getNavn).collect(Collectors.toList())));
    }

    private void validateCompleteInput() {
        if (inputMatrett.getText().isEmpty()
            || inputBeskrivelse.getText().isEmpty()
            || typeRecipe.getValue() == null) {
            throw new IllegalArgumentException("All fields must be filled out to add a recipe.");
        }
    }

    private void validateIngredientInput() {
        if (txtIngrediesToAdd.getText().isEmpty()
            || txtMengdeToAdd.getText().isEmpty()
            || choiceBoxMasure.getValue() == null) {
            throw new IllegalArgumentException("All fields must be filled out for an ingredient.");
        }
        try {
            Double.parseDouble(txtMengdeToAdd.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number.");
        }
    }

    public Model getModel() {
        return this.model;
    } 
}

