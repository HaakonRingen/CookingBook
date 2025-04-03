package persistence;


import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.Book;
import core.Ingredient;
import core.Recipe;
import core.Student;
import core.Users;

/**
 * A module that registers serializers and deserializers for {@link Users}, 
 * {@link Student}, {@link Book}, 
 * {@link Recipe}, and {@link Ingredient} classes.
 */
public class BookStudentModule extends SimpleModule {
    
    private static final String name = "BookStudentModule";
    private static final VersionUtil version = new VersionUtil() {};
    
    /**
     * Constructs a new {@code BookModule} and registers the serializers and 
     * deserializers for {@link Users}, 
     * {@link Student}, {@link Book}, {@link Recipe}, and {@link Ingredient} classes.
     */
    @SuppressWarnings("deprecation")
    public BookStudentModule() {
        super(name, version.version());
        addSerializer(Ingredient.class, new IngredientSerializer());
        addSerializer(Recipe.class, new RecipeSerializer());
        addSerializer(Book.class, new BookSerializer());
        addSerializer(Student.class, new StudentSerializer());
        addSerializer(Users.class, new UserSerializer());

        addDeserializer(Ingredient.class, new IngredientDeserializer());
        addDeserializer(Recipe.class, new RecipeDeserializer());
        addDeserializer(Book.class, new BookDeserializer());
        addDeserializer(Student.class, new StudentDeserializer());
        addDeserializer(Users.class, new UserDeserializer());
    }
}