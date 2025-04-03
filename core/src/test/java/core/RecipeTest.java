package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;


/**
 * Test class for the {@link Recipe} class.
 * 
 * This class provides unit tests for the functionalities of the {@code Recipe} class,
 * including ingredient management, portion scaling, and recipe details.
 */
public class RecipeTest {

    @Test
    public void testAddIngredient() {
        Recipe recipe = new Dinner("A good dinner");
        assertEquals("Dinner", recipe.getType());
        Ingredient ingredient = new Ingredient("Tomato", 2, "stk");
        recipe.addIngredient(ingredient);

        assertTrue(recipe.getIngredients().contains(ingredient));
        assertEquals(1, recipe.getIngredients().size());

        Ingredient ingredient2 = new Ingredient("Water", 1, "L");
        recipe.addIngredient(ingredient2);
        assertEquals("Tomato", recipe.getIngredients().get(0).getIngredientName());

    }

    /**
     * Tests adding an ingredient to the recipe.
     */
    @Test
    public void testAddNullIngredientThrowsException() {
        Recipe recipe = new Recipe("A good recipe");
        assertThrows(IllegalArgumentException.class, () -> {
            recipe.addIngredient(null);
        });
    }

    /**
     * Tests that adding a null ingredient throws an exception.
     * Verifies that an {@link IllegalArgumentException} is thrown when attempting to
     */
    @Test
    public void testTutorial() {
        Recipe recipe = new Recipe("Spaghetti Carbonara");
        assertThrows(IllegalArgumentException.class, () -> {
            recipe.setTutorial(null);
        });
        recipe.setTutorial("Dette er framgangsmåten for å lage spaghetti carbonara");

        assertEquals("Dette er framgangsmåten for å lage spaghetti "
                     + "carbonara", recipe.getTutorial());
    }

    /*
     * Ensures that setting a null tutorial throws an exception,
     */
    @Test
    public void testRemoveLastIngredient() {
        Recipe recipe = new Recipe("A good dinner");

        Ingredient ingredient1 = new Ingredient("Pasta", 200, "g");
        Ingredient ingredient2 = new Ingredient("Salt", 2, "ts");
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        assertEquals(2, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients()
            .stream()
            .anyMatch(ingredient -> ingredient.getIngredientName().equals("Salt")));

        recipe.removeLastIngredient();
        assertEquals(1, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients()
            .stream()
            .noneMatch(ingredient -> ingredient.getIngredientName().equals("Salt")));

        recipe.addIngredient(new Ingredient("Pesto", 4, "ss"));

        Recipe recipe2 = new Recipe("An extremely good dinner");

        recipe2.setIngredients(recipe.getIngredients());
        assertEquals(recipe2.getIngredients(), recipe.getIngredients());


        assertEquals(2, recipe.getIngredients().size());

        recipe.removeLastIngredient();
        recipe.removeLastIngredient();

        assertEquals(0, recipe.getIngredients().size());
    }

    /**
     * Ensures that the number of portions cannot be set to zero or negative,
     * and verifies the portions value is correctly set.
     */
    @Test
    public void testSetPortions() {

        Recipe recipe = new Recipe("mat");
        
        // should throw when argument is 0
        assertThrows(IllegalArgumentException.class, () -> recipe.setPortions(0));
        // should throw throw when argument is negative
        assertThrows(IllegalArgumentException.class, () -> recipe.setPortions(-1));
        // test standard case
        recipe.setPortions(1);
        assertEquals(1, recipe.getPortions());


    }

    /**
     * Verifies that setting a null name throws an exception
     * and checks that the name is correctly set otherwise.
     */
    @Test
    public void testSetNavn() {
        Recipe recipe = new Recipe("mat");
        // should throw when argument is null
        assertThrows(IllegalArgumentException.class, () -> recipe.setNavn(null));

        //test standard case
        recipe.setNavn("curry");
        assertEquals("curry", recipe.getNavn());
    }

    /**
     * Verifies that ingredient amounts adjust correctly based on the
     * specified number of portions.
     */
    @Test
    public void testGetIngredientsForPortion() {
        Recipe recipe = new Recipe("mat");
        Ingredient currypowder = new Ingredient("currypowder", 10, "g");
        recipe.addIngredient(currypowder);
        recipe.setPortions(1);
        
        // test for one portion
        
        assertEquals(currypowder.getIngredientName(),
            recipe.getIngredientsForPortions(1).get(0).getIngredientName());
        assertEquals(currypowder.getAmount(), 
                     recipe.getIngredientsForPortions(1).get(0).getAmount());

        // test for two portions
        assertEquals(20, recipe.getIngredientsForPortions(2).get(0).getAmount());
        assertEquals(10, recipe.getIngredientsForPortions(1).get(0).getAmount());
        // test for multiple ingredients and 2 portions
        recipe.addIngredient(new Ingredient("milk", 30, "ml"));
        ArrayList<Float> amounts = new ArrayList<>();
        amounts.add(20.0f);
        amounts.add(60.0f);
        assertEquals(amounts, recipe.getIngredientsForPortions(2)
            .stream()
            .map(Ingredient::getAmount)
            .collect(Collectors.toList()));

    }

    /*
     * Verifies getter for portions works
     */
    @Test
    public void testGetPortions() {
        Recipe recipe = new Recipe("mat");
        assertEquals(1, recipe.getPortions());
    }

    /*
     * Verifies the toString works as it should
     */
    @Test
    public void testToString() {
        Recipe recipe = new Dessert("cookies");
        recipe.setTutorial("hello testing");
       

        Ingredient sugar = new Ingredient("sugar", 100, "g");
        recipe.addIngredient(sugar);

        String answer = "cookies\tDessert\thello testing\tsugar 100.0 g";

        assertEquals(answer, recipe.toString());
    }

    /**
     * Verifies that all provided fields are correctly initialized
     * and checks that an exception is thrown if null ingredients are set.
     */
    @Test
    public void testCreatorContructor() {

        Ingredient tomat = new Ingredient("Tomat", 4, "stk");
        Ingredient agurk = new Ingredient("Agurk", 1, "stk");
        Ingredient salat = new Ingredient("Salat", 3, "dl");

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(tomat);
        ingredients.add(agurk);
        ingredients.add(salat);



        Recipe greskSalat = new Recipe("Gresk salat",
            "Miks alle ingrediensene i en bolle",
            "Lunch", ingredients);

        assertEquals("Gresk salat", greskSalat.getNavn());
        assertEquals("Lunch", greskSalat.getType());
        assertEquals(ingredients, greskSalat.getIngredients());

        Recipe bareSalat = new Recipe("Gresk salat",
            "Miks alle ingrediensene i en bolle",
            "Lunch",
            null);

        assertEquals(0, bareSalat.getIngredients().size());

        assertThrows(IllegalArgumentException.class, () -> greskSalat.setIngredients(null));
    }
}