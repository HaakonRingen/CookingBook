package persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Student;
import java.io.IOException;

/**
 * A custom serializer for Student objects.
 */
public class StudentSerializer extends JsonSerializer<Student> {
    
    /**
     * Serializes a student object to JSON.
     * 
     * @param student the student object to serialize
     * @param gen the JSON generator used to write JSON content
     * @param serializers the serializer provider
     * @throws IOException if an I/O error occurs
     */

    @Override
    public void serialize(Student student, JsonGenerator gen, SerializerProvider serializers) 
        throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", student.getFullName());
        gen.writeNumberField("ID", student.getId());
        gen.writeStringField("password", student.getPassword());
        gen.writeObjectField("cookingbook", student.getCookingbook());
        gen.writeEndObject();
    }
    
}
