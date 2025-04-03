package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Student} class.
 * 
 * This class contains tests that verify the functionality of the {@code Student} class,
 * including validation of constructor parameters, getter and setter methods, 
 * and interactions with associated {@code Book} objects.
 * 
 * <ul>
 * <li>{@link #testGetters()}: Tests the getters to ensure they return correct values.</li>
 * <li>{@link #testConstructor()}: Tests the constructor to validate input constraints.</li>
 * <li>{@link #testCreatorConstructor()}: Tests the constructor with {@code Book} association.</li>
 * <li>{@link #testSetters()}: Tests the setters to verify they update the fields correctly.</li>
 * </ul>
 */
public class StudentTest {


    @Test
    public void testGetters() {
        Student student1 = new Student("John", "Doe", 654321, "password123");

        assertEquals("John", student1.getFirstName());
        assertEquals("Doe", student1.getLastName());
        assertEquals("John Doe", student1.getFullName());
        assertEquals(654321, student1.getId());
        assertEquals("password123", student1.getPassword());

    }

    @Test
    public void testConstructor() {

        assertThrows(IllegalArgumentException.class, () -> 
            new Student("Jane", "Doe", 12345, "password123")
        );
        assertThrows(IllegalArgumentException.class, () -> 
            new Student("Jane", "", 123455, "password123")
        );
        assertThrows(IllegalArgumentException.class, () -> 
            new Student("", "Nordmann", 123456, "password123")
        );
        assertThrows(IllegalArgumentException.class, () -> 
            new Student("Jane", "Doe", 12345, "passord")
        );
        assertThrows(IllegalArgumentException.class, () -> 
        new Student("Jane", "Doe", 123456, "")
        );

    }

    @Test
    public void testCreatorConstructor() {
        Book cookingBook = new Book();

        Student student = new Student("Ola Nordmann", 123456, "Etdårligpassord", cookingBook);

        assertEquals(cookingBook.toString(), student.getCookingbook().toString());
        assertEquals(String.valueOf(123456), student.toString());

        Student newStudent = new Student();

        assertEquals(null, newStudent.getCookingbook());
        
        newStudent.setCookingbook(new Book());
        assertEquals(0, newStudent.getCookingbook().getRecipes().size());
        newStudent.setCookingbook(cookingBook);
        assertEquals(student.getCookingbook().toString(), newStudent.getCookingbook().toString());
    }

    @Test
    public void testSetters() {
        Student student1 = new Student("John", "Doe", 654321, "password123");

        student1.setFirstName("Jane");
        student1.setLastName("Nordmann");

        assertEquals("Jane", student1.getFirstName());
        assertEquals("Nordmann", student1.getLastName());
        assertEquals("Jane Nordmann", student1.getFullName());

        assertThrows(IllegalArgumentException.class, () -> student1.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> student1.setLastName(""));
    }
}
