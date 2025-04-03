package persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Student;
import core.Users;
import java.io.IOException;

/**
 * A custom serializer for {@link Users} objects.
 */
public class UserSerializer extends JsonSerializer<Users> {

    /**
     * Serializes a {@link Users} object into JSON.
     *
     * @param users the {@link Users} object to serialize
     * @param gen the JSON generator
     * @param serializers the serializer provider
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(Users users, JsonGenerator gen, SerializerProvider serializers) 
        throws IOException {
        gen.writeStartObject();

        gen.writeArrayFieldStart("users");
        for (Student user : users) {
            gen.writeObject(user);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
    
}