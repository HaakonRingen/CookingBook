package core;

/**
 * Represents a breakfast recipe.
 */
public class Breakfast extends Recipe {


    /**
     * Constructs a new Breakfast recipe with the specified name.
     *
     * @param name the name of the breakfast recipe
     */
    public Breakfast(String name) {
        super(name);

        this.type = "Breakfast";
    }
    
}
