package core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an ingredient used in a recipe.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class Ingredient {

    private String ingredientName;   // Name of the ingredient
    private float amount;  // Quantity of the ingredient
    private String unit;   // Unit of measurement

    /**
     * Constructs a new Ingredient object with the specified name, amount, and unit.
     *
     * @param ingredientName the name of the ingredient
     * @param amount the quantity of the ingredient
     * @param unit the unit of measurement for the ingredient
     * @throws IllegalArgumentException if the name is null, the amount is less than or equal to 
     *     zero, or the unit is invalid
     */
    @JsonCreator
    public Ingredient(
        @JsonProperty("ingredient") String ingredientName, 
        @JsonProperty("amount") float amount, 
        @JsonProperty("unit") String unit
    ) {

        if (ingredientName == null || ingredientName.isEmpty()) {
            throw new IllegalArgumentException("Name of the ingredient can't be null");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be negative or zero");
        }

        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit can't be null");
        }

        // Ensure unit is a valid option
        if (!unit.equals("L") 
            && !unit.equals("dl") 
            && !unit.equals("ml") 
            && !unit.equals("kg") 
            && !unit.equals("g") 
            && !unit.equals("ts") 
            && !unit.equals("ss") 
            && !unit.equals("stk")) {
            throw new IllegalArgumentException("Unit must be L, dl, cl, ml, kg, g, ts, ss or stk");
        }
        
        this.ingredientName = ingredientName;
        this.amount = amount;
        this.unit = unit;

    }

    /**
     * Returns the name of the ingredient.
     *
     * @return the name of the ingredient
     */
    @JsonProperty("ingredient")
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * Sets the name of the ingredient.
     *
     * @param name the new name of the ingredient
     * @throws IllegalArgumentException if the name is null
     */
    public void setIngredientName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name of the ingredient can't be null");
        }
        this.ingredientName = name;
    }

    /**
     * Returns the amount of the ingredient.
     *
     * @return the amount of the ingredient
     */
    @JsonProperty("amount")
    public float getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient.
     *
     * @param amount the new amount of the ingredient
     * @throws IllegalArgumentException if the amount is less than or equal to zero
     */
    public void setAmount(float amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be negative or zero");
        }
        this.amount = amount;
    }

    /**
     * Returns the unit of measurement for the ingredient.
     *
     * @return the unit of measurement
     */
    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the ingredient.
     *
     * @param unit the new unit of measurement
     * @throws IllegalArgumentException if the unit is invalid
     */
    public void setUnit(String unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit can't be null");
        }

        // Ensure unit is valid
        if (unit.equals("L") 
            || unit.equals("dl") 
            || unit.equals("ml") 
            || unit.equals("kg") 
            || unit.equals("g") 
            || unit.equals("ts") 
            || unit.equals("ss") 
            || unit.equals("stk")) {
            this.unit = unit;
        } else {
            throw new IllegalArgumentException("Unit must be L, dl, cl, ml, kg, g, ts, ss or stk");
        }
    }

    /**
     * Returns a string representation of the amount and unit of the ingredient.
     *
     * @return a string in the format "amount unit"
     */
    public String getAmountWithUnit() {
        return amount + " " + unit;
    }

    /**
     * Returns a string representation of the Ingredient object.
     * The string representation consists of the name, amount, and unit of the ingredient.
     *
     * @return a string representation of the Ingredient object
     */
    @Override
    public String toString() {
        return ingredientName + " " + getAmountWithUnit();
    }   
}
