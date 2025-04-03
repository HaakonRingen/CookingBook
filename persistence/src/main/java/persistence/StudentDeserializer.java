package persistence;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.Book;
import core.Student;
import java.io.IOException;

/**
 * Custom deserializer for {@link Student} objects.
 */
public class StudentDeserializer extends JsonDeserializer<Student> {

    @Override
    public Student deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JacksonException {
        TreeNode treeNode = p.getCodec().readTree(p);
        return deserialize((JsonNode) treeNode);
    }

    /**
     * Deserializes a {@link JsonNode} into a {@link Student} object.
     *
     * @param node the JSON node to deserialize
     * @return the deserialized Student object
     */
    public Student deserialize(JsonNode node) {
        Student student = null;
        BookDeserializer bookDeserializer = new BookDeserializer();

        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            // Deserialize name
            String name = null;
            JsonNode nameNode = objectNode.get("name");
            if (nameNode instanceof TextNode) {
                name = nameNode.asText();
            }

            // Deserialize ID
            int id = 0;
            JsonNode idNode = objectNode.get("ID");
            if (idNode != null && idNode.isNumber()) {
                id = idNode.intValue();
            }

            // Deserialize password
            String password = null;
            JsonNode passwordNode = objectNode.get("password");
            if (passwordNode instanceof TextNode) {
                password = passwordNode.asText();
            }

            // Create student if all required fields are present
            if (name != null && id != 0 && password != null) {
                String[] nameParts = name.split(" ");
                String firstName = nameParts.length > 0 ? nameParts[0] : null;
                String lastName = nameParts.length > 1 ? nameParts[1] : null;
                student = new Student(firstName, lastName, id, password);
            }

            // Deserialize cookingbook (optional)
            JsonNode bookNode = objectNode.get("cookingbook");
            if (bookNode != null && bookNode instanceof ObjectNode && student != null) {
                Book book = bookDeserializer.deserialize(bookNode);
                if (book != null) {
                    student.setCookingbook(book);
                }
            }
        }

        return student;
    }
}