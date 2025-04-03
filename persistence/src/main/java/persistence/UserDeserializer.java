package persistence;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.Student;
import core.Users;
import java.io.IOException;

/**
 * Custom deserializer for {@link Users} objects.
 */
public class UserDeserializer extends JsonDeserializer<Users> {



    private StudentDeserializer studentDeserializer = new StudentDeserializer();
    
    /**
     * Deserializes JSON content into a {@link Users} object.
     *
     * @param p the JSON parser
     * @param ctxt the deserialization context
     * @return the deserialized {@link Users} object
     * @throws IOException if an I/O error occurs
     * @throws JacksonException if a Jackson error occurs
     */
    @Override
    public Users deserialize(JsonParser p, DeserializationContext ctxt) 
        throws IOException, JacksonException {
        TreeNode treeNode = p.getCodec().readTree(p);
        return deserialize((JsonNode) treeNode);
    }

    /**
     * Deserializes a {@link JsonNode} into a {@link Users} object.
     *
     * @param node the JSON node
     * @return the deserialized {@link Users} object, or null if the node is not an ObjectNode
     */
    public Users deserialize(JsonNode node) {
        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            Users users = new Users();

            JsonNode recipesNode = objectNode.get("users");
            if (recipesNode instanceof ArrayNode) {
                for (JsonNode element : (ArrayNode) recipesNode) {
                    Student student = studentDeserializer.deserialize(element);
                    if (student != null) {
                        users.addUser(student);
                    }
                }
            }
            return users;
        }
        return null;
    }
    
}
