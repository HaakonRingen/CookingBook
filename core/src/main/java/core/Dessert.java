package core;

/**
 * Represents a dessert recipe.
 */
public class Dessert extends Recipe {

    /**
     * Constructs a new Dessert with the specified name.
     *
     * @param name the name of the dessert
     */
    public Dessert(String name) {
        super(name);

        this.type = "Dessert";
    }
    
}
