package persistence;



import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.Book;
import core.Recipe;
import java.io.IOException;


/**
 * Custom deserializer for {@link Book} objects.
 */
public class BookDeserializer extends JsonDeserializer<Book> {



    private RecipeDeserializer dinnerDeserializer = new RecipeDeserializer();
    
    /**
     * Deserializes JSON content into a {@link Book} object.
     *
     * @param p the JSON parser
     * @param ctxt the deserialization context
     * @return the deserialized {@link Book} object
     * @throws IOException if an I/O error occurs
     * @throws JacksonException if a Jackson error occurs
     */
    @Override
    public Book deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JacksonException {
        TreeNode treeNode = p.getCodec().readTree(p);
        return deserialize((JsonNode) treeNode);
    }

    /**
     * Deserializes a {@link JsonNode} into a {@link Book} object.
     *
     * @param node the JSON node
     * @return the deserialized {@link Book} object, or null if the node is not an ObjectNode
     */
    public Book deserialize(JsonNode node) {
        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            Book cookingbook = new Book();

            JsonNode recipesNode = objectNode.get("recipes");
            if (recipesNode instanceof ArrayNode) {
                for (JsonNode element : (ArrayNode) recipesNode) {
                    Recipe recipe = dinnerDeserializer.deserialize(element);
                    if (recipe != null) {
                        cookingbook.addRecipe(recipe);
                    }
                }
            }
            return cookingbook;
        }
        return null;
    }
    
}
