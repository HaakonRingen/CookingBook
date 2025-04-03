package persistence;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.Breakfast;
import core.Dessert;
import core.Dinner;
import core.Ingredient;
import core.Lunch;
import core.Recipe;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Custom deserializer for Recipe objects.
 */
public class RecipeDeserializer extends JsonDeserializer<Recipe> {

    private String name;
    private String tutorial;
    private String type;

    private IngredientDeserializer ingredientDeserializer = new IngredientDeserializer();

    /**
     * Deserializes JSON content into a Recipe object.
     *
     * @param p the JSON parser
     * @param ctxt the deserialization context
     * @return the deserialized Recipe object
     * @throws IOException if an I/O error occurs
     * @throws JacksonException if a Jackson error occurs
     */
    @Override
    public Recipe deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JacksonException {
        TreeNode treenode = p.getCodec().readTree(p);
        return deserialize((JsonNode) treenode);
    }

    /**
     * Deserializes a JsonNode into a Recipe object.
     *
     * @param node the JsonNode to deserialize
     * @return the deserialized Recipe object, or null if deserialization fails
     */
    public Recipe deserialize(JsonNode node) {

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        
        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            JsonNode textNode = objectNode.get("name");
            if (textNode instanceof TextNode) {
                name = ((TextNode) textNode).asText();
            }
            JsonNode typeNode = objectNode.get("type");
            if (typeNode instanceof TextNode) {
                type = ((TextNode) typeNode).asText();
            }

            JsonNode tutorialNode = objectNode.get("tutorial");
            if (tutorialNode instanceof TextNode) {
                tutorial = ((TextNode) tutorialNode).asText();
            }

            JsonNode ingredientsNode = objectNode.get("ingredients");
            if (ingredientsNode instanceof ArrayNode) {
                for (JsonNode element : (ArrayNode) ingredientsNode) {
                    Ingredient ingredient = ingredientDeserializer.deserialize(element);
                    if (ingredient != null) {
                        ingredients.add(ingredient);
                    }
                }
            }

            if (name != null && tutorial != null && !ingredients.isEmpty()) {
                Recipe recipe = null;

                if (type.equals("Breakfast")) {
                    recipe = new Breakfast(name);
                } else if (type.equals("Lunch")) {
                    recipe = new Lunch(name);
                } else if (type.equals("Dinner")) {
                    recipe = new Dinner(name);
                } else if (type.equals("Dessert")) {
                    recipe = new Dessert(name);
                } else {
                    return null;
                }

                recipe.setTutorial(tutorial);
                for (Ingredient i : ingredients) {
                    recipe.addIngredient(i);
                }
                return recipe;
            }
        }
        return null;
    }
}