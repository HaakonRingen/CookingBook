package persistence;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Book;
import core.Dessert;
import core.Dinner;
import core.Ingredient;
import core.Lunch;
import core.Recipe;
import core.Student;
import core.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link BookStudentModule} class.
 * 
 * This class verifies the serialization and deserialization functionalities
 * of the {@code BookStudentModule} class, ensuring that data from core modules
 * is correctly handled for JSON processing.
 * 
 * <ul>
 * <li>{@link #testSerializers()}: Tests the serialization of {@code Ingredient},
 * {@code Recipe}, {@code Book}, and {@code Users} objects to JSON format.</li>
 * <li>{@link #testDeserializers()}: Tests the deserialization of JSON data 
 * back into {@code Ingredient}, {@code Recipe}, {@code Book}, and {@code Users} objects.</li>
 * </ul>
 */
public class BookStudentModuleTest {

    private static ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BookStudentModule());
    }

    @Test
    public void testSerializers() throws JsonProcessingException {

       
        Recipe recipe = new Dinner("Pasta");
        recipe.setTutorial("Kok pasta med salt");

        Ingredient ingredient1 = new Ingredient("Pasta", 100, "g");
        Ingredient ingredient2 = new Ingredient("salt", 3, "ts");
        Book book = new Book();

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        book.addRecipe(recipe);

        String check = "{\"ingredient\":\"Pasta\",\"amount\":100.0,\"unit\":\"g\"}";
        assertEquals(check, mapper.writeValueAsString(ingredient1));
        check = "{\"ingredient\":\"salt\",\"amount\":3.0,\"unit\":\"ts\"}";
        assertEquals(check, mapper.writeValueAsString(ingredient2));
        check = "{\"name\":\"Pasta\",\"type\":\"Dinner\",\"tutorial\":"
            + "\"Kok pasta med salt\",\"ingredients"
            + "\":[{\"ingredient\":\"Pasta\",\"amount\":100.0,\"unit\":\"g\"},"
            + "{\"ingredient\":\"salt\",\"amount\":3.0,\"unit\":\"ts\"}]}";
        assertEquals(check,
                    mapper.writeValueAsString(recipe));

        check = "{\"recipes\":[{\"name\":\"Pasta\",\"type\":\"Dinner\","
            + "\"tutorial\":\"Kok pasta med salt\",\"ingredients\":[{\"ingredient\":\"Pasta\","
            + "\"amount\":100.0,\"unit\":\"g\"},{\"ingredient"
            + "\":\"salt\",\"amount\":3.0,\"unit\":\"ts\"}]}]}";
        assertEquals(check,
                    mapper.writeValueAsString(book));

        Users users = new Users();

        Student user = new Student("Ola", "Nordmann", 123456, "Passord123");
        user.setCookingbook(book);
        users.addUser(user);

        check = "{\"name\":\"Ola Nordmann\",\"ID\":123456,\"password\":"
            + "\"Passord123\",\"cookingbook\":{\"recipes\":[{\"name\":\"Pasta\","
            + "\"type\":\"Dinner\",\"tutorial\":\"Kok pasta med salt\",\"ingredients\":["
            + "{\"ingredient\":\"Pasta\",\"amount\":100.0,\"unit\":\"g\"},{\"ingredient\":\"salt\","
            + "\"amount\":3.0,\"unit\":\"ts\"}]}]}}";
        assertEquals(check, mapper.writeValueAsString(user));
        check = "{\"users\":[{\"name\":\"Ola Nordmann\",\"ID\":123456,\"password"
            + "\":\"Passord123\",\"cookingbook\":{\"recipes\":[{\"name\":\"Pasta\",\"type"
            + "\":\"Dinner\",\"tutorial\":\"Kok pasta med salt\",\"ingredients\":["
            + "{\"ingredient\":\"Pasta\",\"amount\":100.0,\"unit\":\"g\"},{\"ingredient\":"
            + "\"salt\",\"amount\":3.0,\"unit\":\"ts\"}]}]}}]}";
        assertEquals(check, mapper.writeValueAsString(users));
    }

    @Test
    public void testDeserializers() throws JsonProcessingException {
 
        Ingredient ingredient3 = new Ingredient("Poteter", 20, "stk");
        Ingredient ingredient4 = new Ingredient("Melk", 3, "dl");
        Recipe lunch = new Lunch("Potetmos");

        lunch.setTutorial("Kok potetene, deretter tilsett melk, og mos potetene");
        lunch.addIngredient(ingredient3);
        lunch.addIngredient(ingredient4);
        Book book2 = new Book();
        book2.addRecipe(lunch);
        Recipe dessert2 = new Dessert("Sjokoladekake");
        dessert2.setTutorial("Deilig sjokoladekake");
        Ingredient ingredientsjoko3 = new Ingredient("Sjokolade", 400, "g");
        Ingredient ingredientsjoko4 = new Ingredient("Mel", 250, "g");
        dessert2.addIngredient(ingredientsjoko3);
        dessert2.addIngredient(ingredientsjoko4);
        book2.addRecipe(dessert2);



        Ingredient ingredient5 = new Ingredient("Brød", 2, "stk");
        Ingredient ingredient6 = new Ingredient("Osteskive", 1, "stk");
        Recipe breakfast = new Lunch("Ostesmørbrød");
        String tutorial = "Legg en osteskive oppå det ene brødet,"
            + " deretter legg det andre brødet på toppen";
        breakfast.setTutorial(tutorial);
        breakfast.addIngredient(ingredient5);
        Book book3 = new Book();
        breakfast.addIngredient(ingredient6);
        book3.addRecipe(breakfast);

        String users1 = "{\"users\":[{\"name\":\"Kari Nordmann\",\"ID\":654321"
            + ",\"password\":\"Passord321\",\"cookingbook\":{\"recipes"
            + "\":[{\"name\":\"Ostesmørbrød\","
            + "\"type\":\"Breakfast\",\"tutorial\":\"Legg en osteskive oppå det ene brødet, "
            + "deretter legg det andre brødet på toppen\",\"ingredients"
            + "\":[{\"ingredient\":\"Brød\",\"amount\":2.0,\"unit\":\"stk\"},"
            + "{\"ingredient\":\"Osteskive\",\"amount\":1.0,\"unit\":\"stk\"}]}]}}]}";
        Student user = new Student("Kari", "Nordmann", 654321, "Passord321");
        user.setCookingbook(book3);
        Users users2 = new Users();

        String ingredient1 = "{\"ingredient\":\"Poteter\",\"amount\":20,\"unit\":\"stk\"}";
        String ingredient2 = "{\"ingredient\":\"Melk\",\"amount\":3.0,\"unit\":\"dl\"}";
        String recipe = "{\"name\":\"Potetmos\",\"type\":\"Lunch\",\"tutorial\":\"Kok potetene,"
            + " deretter tilsett melk, og mos potetene\","
            + "\"ingredients\":[" + ingredient1 + ", " + ingredient2 + "]}";
        String ingredientSjoko = "{\"ingredient\":\"Sjokolade\",\"amount\":400,\"unit\":\"g\"}";
        String ingredientSjoko2 = "{\"ingredient\":\"Mel\",\"amount\":250,\"unit\":\"g\"}";
    
        String dessert = "{\"name\":\"Sjokoladekake\",\"type\":\"Dessert\",\"tutorial"
            + "\":\"Deilig sjokoladekake\",\"ingredients"
            + "\":[" + ingredientSjoko + ", " + ingredientSjoko2 + "]}";
        String book = "{\"recipes\":[" + recipe + ", " + dessert + "]}";
        
        users2.addUser(user);

        String users3 = "{\"users\":[{\"name\":\"Kari Nordmann\",\"ID"
            + "\":654321,\"password\":\"Passord321\",\"cookingbook\":" + book + "}]}";

        assertEquals(users2.toString(), mapper.readValue(users1, Users.class).toString());


        user.setCookingbook(book2);

        assertEquals(users2.toString(), mapper.readValue(users3, Users.class).toString());
    }
    
}
