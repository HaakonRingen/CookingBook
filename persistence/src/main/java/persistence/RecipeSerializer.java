package persistence;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Ingredient;
import core.Recipe;
import java.io.IOException;

/**
 * A custom serializer for {@link Recipe} objects.
 */
public class RecipeSerializer extends JsonSerializer<Recipe> {

  
    /**
     * Serializes a Recipe object into JSON format.
     *
     * @param recipe the Recipe object to serialize
     * @param gen the JsonGenerator used to write JSON content
     * @param serializers the SerializerProvider that can be used to get serializers for 
     *                    serializing Objects value
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(
        Recipe recipe, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", recipe.getNavn());
        gen.writeStringField("type", recipe.getType());
        gen.writeStringField("tutorial", recipe.getTutorial());

        gen.writeArrayFieldStart("ingredients");
        for (Ingredient ingredient : recipe.getIngredients()) {
            gen.writeObject(ingredient);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
    
}
