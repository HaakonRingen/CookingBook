package core;


/**
 * Reprecent a dinner with the param: name, tutorial, portion and ingredients.
 */
public class Dinner extends Recipe {

    /**
     * Constructs a new Dinner with the specified name.
     *
     * @param name the name of the dinner
     */
    public Dinner(String name) {
        super(name);

        this.type = "Dinner";

    }
}