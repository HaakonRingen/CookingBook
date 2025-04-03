package persistence;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.Ingredient;
import java.io.IOException;



/**
 * Custom deserializer for Ingredient objects.
 */
public class IngredientDeserializer extends JsonDeserializer<Ingredient> {

    private String name;
    private Float amount;
    private String unit;

    @Override
    public Ingredient deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JacksonException {
        TreeNode treenode = p.getCodec().readTree(p);

        return deserialize((JsonNode) treenode);
        
    }


    /**
     * Deserializes a JSON node into an Ingredient object.
     *
     * @param node the JSON node to deserialize
     * @return an Ingredient object if the JSON node contains valid data, otherwise null
     */
    public Ingredient deserialize(JsonNode node) {
        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            JsonNode textnode = objectNode.get("ingredient");
            if (textnode instanceof TextNode) {
                name = ((TextNode) textnode).asText();
            }
            JsonNode amountnode = objectNode.get("amount");
            if (amountnode != null && amountnode.isNumber()) {
                amount = amountnode.floatValue();
            }
            JsonNode unitnode = objectNode.get("unit");
            if (unitnode instanceof TextNode) {
                unit = ((TextNode) unitnode).asText();
            }

            if  (name != null && amount != null && unit != null) {
                return new Ingredient(name, amount, unit);
            }

        }
    
        return null;
    }

    
}
