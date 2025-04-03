package core;

/**
 * Represents a lunch recipe.
 */
public class Lunch extends Recipe {

    /**
     * Constructs a new Lunch recipe with the specified name.
     *
     * @param name the name of the lunch recipe
     */
    public Lunch(String name) {
        super(name);

        this.type = "Lunch";
    }
    
}
