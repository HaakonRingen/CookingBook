package persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Book;
import core.Recipe;
import java.io.IOException;

/**
 * Serializes a {@link Book} object into JSON.
 */
public class BookSerializer extends JsonSerializer<Book> {

    /**
     * Serializes a {@link Book} object into JSON.
     *
     * @param cookingbook the {@link Book} object to serialize
     * @param gen the JSON generator
     * @param serializers the serializer provider
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(Book cookingbook, 
                          JsonGenerator gen, 
                          SerializerProvider serializers) 
                          throws IOException {
        gen.writeStartObject();

        gen.writeArrayFieldStart("recipes");
        for (Recipe recipe : cookingbook) {
            gen.writeObject(recipe);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
    
}