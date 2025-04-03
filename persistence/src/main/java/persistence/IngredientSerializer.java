package persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Ingredient;
import java.io.IOException;

/**
 * A custom serializer for the Ingredient class.
 */
public class IngredientSerializer extends JsonSerializer<Ingredient> {
     
    /**
     * Serializes an Ingredient object to JSON.
     * 
     * @param ingredient the Ingredient object to serialize
     * @param gen the JSON generator used to write JSON content
     * @param serializers the serializer provider
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(
        Ingredient ingredient, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("ingredient", ingredient.getIngredientName());
        gen.writeNumberField("amount", ingredient.getAmount());
        gen.writeStringField("unit", ingredient.getUnit());
        gen.writeEndObject();
    }
    
}
