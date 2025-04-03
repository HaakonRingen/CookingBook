package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test class for the Ingredient class.
 */
public class IngredientTest {

    @Test
    public void testNewIngredient() {
        Ingredient ingredient = new Ingredient("Sugar", 100, "g");
        assertEquals("Sugar", ingredient.getIngredientName());
        assertEquals(100, ingredient.getAmount());
        assertEquals("g", ingredient.getUnit());

        ingredient.setAmount(1);

        ingredient.setUnit("kg");

        ingredient.setIngredientName("Brown sugar");

        assertEquals(ingredient, ingredient);
    }

    @Test
    public void testIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient("Butter", -100, "g");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient("Butter", 100, "big cups");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient("", 100, "g");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient("Butter", 100, null);
        });

        Ingredient ingredient = new Ingredient("Butter", 100, "g");
        assertThrows(IllegalArgumentException.class, () -> {
            ingredient.setUnit("whole package");;
        });
        assertThrows(IllegalArgumentException.class, () -> ingredient.setIngredientName(null));
        assertThrows(IllegalArgumentException.class, () -> ingredient.setUnit(null));
        assertThrows(IllegalArgumentException.class, () -> ingredient.setAmount(-100));

    }
    
}
