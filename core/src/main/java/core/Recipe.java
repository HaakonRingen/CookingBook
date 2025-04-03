package core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Represents a recipe with a name, tutorial, type, and a list of ingredients.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Recipe implements Iterable<Ingredient> {
    
    protected String navn; // Name of the dinner
    protected String tutorial = ""; // Instructions for the dinner
    protected int portions = 1; // Number of portions
    protected String type = ""; // Type of recipe

    protected ArrayList<Ingredient> ingredients; // List of ingredients

    /**
     * Constructs a new Recipe object with the specified name, tutorial, type, and ingredients.
     *
     * @param navn the name of the recipe
     * @param tutorial the tutorial text for the recipe
     * @param type the type of the recipe
     * @param ingredients the list of ingredients for the recipe
     */
    @JsonCreator
    public Recipe(@JsonProperty("name") String navn,
        @JsonProperty("tutorial") String tutorial,
        @JsonProperty("type") String type,
        @JsonProperty("ingredients") ArrayList<Ingredient> ingredients) {
        this.navn = navn;
        this.tutorial = tutorial;
        this.type = type;
        if (ingredients == null) {
            this.ingredients = new ArrayList<Ingredient>();
        } else {
            this.ingredients = ingredients;
        }
    }

    /**
     * Constructs a new Recipe object with the specified name.
     * Initializes the list of ingredients.
     *
     * @param name the name of the recipe
     * @throws IllegalArgumentException if the name is null
     */
    public Recipe(String name) {
        if (name != null) {
            this.navn = name;
        }

        ingredients = new ArrayList<>();
    }

    /**
     * Sets the number of portions for the dinner.
     *
     * @param portions the number of portions
     * @throws IllegalArgumentException if the number of portions is less than or equal to zero
     */
    public void setPortions(int portions) {
        if (portions <= 0) {
            throw new IllegalArgumentException("Portions can't be negative or zero");
        }
        this.portions = portions;
    }

    /**
     * Returns the number of portions for the dinner.
     *
     * @return the number of portions
     */
    public int getPortions() {
        return portions;
    }

    /**
     * Sets the name of the dinner.
     *
     * @param navn the name of the dinner
     * @throws IllegalArgumentException if the name is null
     */
    public void setNavn(String navn) {
        if (navn == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        this.navn = navn;
    }

    /**
     * Sets the tutorial text for the dinner.
     *
     * @param tutorial the tutorial text
     * @throws IllegalArgumentException if the tutorial text is null
     */
    public void setTutorial(String tutorial) {
        if (tutorial == null) {
            throw new IllegalArgumentException("Tutorial text can't be null");
        }
        this.tutorial = tutorial;
    }

    /**
     * Adds a new ingredient to the list.
     *
     * @param ingredient the Ingredient object to be added
     * @throws IllegalArgumentException if the ingredient is null
     */
    public void addIngredient(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient can't be null");
        }
        ingredients.add(ingredient);
    }

    /**
     * Sets the ingredient list.
     *
     * @param ingredients the new list of ingredients
     * @throws IllegalArgumentException if the ingredients list is null
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients list can't be null");
        }
        this.ingredients = new ArrayList<>(ingredients);
    }

    /**
     * Returns the name of the recipe.
     *
     * @return the name of the recipe
     */
    @JsonProperty("name")
    public String getNavn() {
        return navn;
    }

    /**
     * Returns the tutorial text for the recipe.
     *
     * @return the tutorial text
     */
    @JsonProperty("tutorial")
    public String getTutorial() {
        return tutorial;
    }

    /**
     * Returns the list of ingredients.
     *
     * @return a new ArrayList containing all ingredients
     */
    @JsonProperty("ingredients")
    public ArrayList<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);      
    }

    /**
     * Removes the last ingredient in the list.
     */
    public void removeLastIngredient() {
        if (ingredients.size() > 0) {
            ingredients.remove(ingredients.size() - 1);
        }
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Retrieves ingredients adjusted for the specified number of portions.
     *
     * @param portions the number of portions to adjust for
     * @return a list of adjusted ingredients
     */
    public ArrayList<Ingredient> getIngredientsForPortions(int portions) {
        ArrayList<Ingredient> output = new ArrayList<>(ingredients);
        return output.stream()
            .map(ingredient -> {
                Ingredient scaled = new Ingredient(
                    ingredient.getIngredientName(), 
                    ingredient.getAmount(), 
                    ingredient.getUnit()
                );
                scaled.setAmount(scaled.getAmount() * portions / this.portions);
                return scaled;
            })
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a string of a complete recipe.
     *
     * @return a string representation of the recipe
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(navn).append("\t").append(type).append("\t").append(tutorial).append("\t");

        for (Ingredient ingredient : ingredients) {
            builder.append(ingredient.toString()).append(", ");
        }

        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
        // return navn + "\t"  + type + "\t" + tutorial + "\t" + ingredients;
    }
    
    /**
    * Returns an iterator over elements of type {@code Ingredient}.
    *
    * @return an Iterator.
    */
    @Override
    public Iterator<Ingredient> iterator() {
        return ingredients.iterator();
    }
}
