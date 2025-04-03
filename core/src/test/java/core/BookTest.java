package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the Book class.
 */
public class BookTest {

    private Book book;

    @BeforeEach
    public void setup() {
        this.book = new Book();
    }
    
    @Test
    public void testBook() {
        Recipe recipe1 = new Recipe("Pasta");

        book.addRecipe(recipe1);


        assertEquals(1, book.getRecipes().size());
        assertTrue(book.getRecipes().contains(recipe1));
        assertEquals(recipe1, book.getRecipe(recipe1));
        assertEquals(null, book.getRecipe(new Recipe("Laks")));

        assertThrows(IllegalArgumentException.class, () -> book.removeRecipe("Nudler"));
        assertThrows(IllegalArgumentException.class, () -> book.removeRecipe((Recipe) null));
        assertThrows(IllegalArgumentException.class, () -> book.addRecipe(null));

        book.removeRecipe(recipe1);
        assertEquals(0, book.getRecipes().size());
        Recipe recipe2 = new Recipe("Nudler");
        book.addRecipe(recipe2);
        Recipe recipe3 = new Recipe("Ris");
        book.addRecipe(recipe3);

        Book newBook = new Book();
        newBook.setRecipes(book.getRecipes());

        assertEquals(2, newBook.getRecipes().size());
        assertEquals(null, newBook.getRecipe("Pasta"));
        assertEquals("Nudler", newBook.getRecipe("Nudler").getNavn());
        assertTrue(newBook.getRecipes().contains(recipe2));

        Book copyNewBook = new Book(newBook);

        assertEquals(newBook.getRecipes(), copyNewBook.getRecipes());

    }

    @Test
    public void testCreatorContructor() {

        Recipe laks = new Recipe("Laks");
        Recipe makrell = new Recipe("Makrell");

        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(laks);
        recipes.add(makrell);

        Book creatorBook = new Book(recipes);

        assertEquals(recipes.get(0).getNavn(), creatorBook.iterator().next().getNavn());
        creatorBook.removeRecipe("Laks");
        assertEquals("Book [recipes=[" + makrell.toString() + "]]", creatorBook.toString());
    }
}
