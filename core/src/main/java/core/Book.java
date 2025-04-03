
package core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represent a collection of dinners in the cookbook.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Book implements Iterable<Recipe> {

    private ArrayList<Recipe> recipes; // List of dinners

    public Book() {
        recipes = new ArrayList<>(); // Initialize dinner list
    }
    /**
     * Makes a copy of a cookbook.
     *
     * @param book the Book object to be copied
     * 
     */

    public Book(Book book) {

        this.recipes = book.getRecipes();
    }

    /**
     * Makes a copy of a recipe.
     *
     * @param recipes the recipes to be copied
     * 
     */
    @JsonCreator
    public Book(@JsonProperty("recipes") ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * Returns a list of all dinners in the cookbook.
     *
     * @return a new ArrayList containing all dinners
     */
    @JsonProperty("recipes")
    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    
    /**
     * Adds a new recipe to the cookbook.
     *
     * @param recipe the Dinner object to be added
     * @throws IllegalArgumentException if the dinner is null
     */
    public void addRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Dinner can't be null");
        }
        recipes.add(recipe);
    }

    /**
     * Gets a recipe from the cookbook by matching the given Dinner object.
     *
     * @param recipe the Dinner object to be matched
     * @return the matching Dinner object or null if no match is found
     */
    public Recipe getRecipe(Recipe recipe) {
        for (Recipe r : recipes) {
            if (r.equals(recipe)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Get a recipe from the cookbook by its name.
     *
     * @param name the name of the dinner
     * @return the matching Dinner object or null if no match is found
     */
    public Recipe getRecipe(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getNavn().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Removes a recipe from the cookbook by matching the given recipe object.
     *
     * @param recipe the recipe object to be removed
     * @throws IllegalArgumentException if dinner is null
     */
    public void removeRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Dinner can't be null");
        }
        recipes.remove(recipe);
    }

    /**
     * Removes a recipe from the cookbook by its name.
     *
     * @param name the name of the recipe to be removed
     * @throws IllegalArgumentException if the dinner is not found
     */
    public void removeRecipe(String name) {
        Recipe recipe = getRecipe(name);
        if (recipe == null) {
            throw new IllegalArgumentException("Dinner not found");
        }
        recipes.remove(recipe);
    }

    /**
     * Sets the entire list of recipe in the cookbook.
     *
     * @param recipes the new list of recipes
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    /**
     * Returns an iterator over elements of type {@code Dinner}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Recipe> iterator() {
        return recipes.iterator();
    }
    
    /**
    * Returns a string representation of the Book object.
    * The string representation consists of a list of recipes in the book.
     *
     * @return a string representation of the Book object
    */
    @Override
    public String toString() {
        return "Book [recipes=" + recipes + "]";
    }
    
}
