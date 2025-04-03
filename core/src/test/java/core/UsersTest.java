package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Users} class.
 * 
 * This class tests the functionality of the {@code Users} class, including the ability 
 * to add users, retrieve users by ID, and initialize users with a predefined list.
 * 
 * <ul>
 * <li>{@link #testUsers()}: Tests adding, retrieving, 
 * and validating users in the {@code Users} collection.</li>
 * <li>{@link #testCreatorConstructor()}: Tests initializing a {@code Users} object 
 * with a list of students and verifying its content.</li>
 * </ul>
 */
public class UsersTest {

    private Users users;
    private Student student1;
    private Student student2;
    

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setup() {
        users = new Users();
        student1 = new Student("Ola", "Normann", 123456, "password123");
        student2 = new Student("Kari", "Normann", 654321, "password321");
    }

    @Test
    public void testUsers() {
        users.addUser(student1);

        assertEquals(1, users.getUsers().size());
        assertTrue(users.getUsers().contains(student1));

        assertEquals(student1, users.getUser(123456));
        
        assertThrows(IllegalArgumentException.class, () -> users.addUser(null));

        users.addUser(student2);

        assertEquals(2, users.getUsers().size());
        assertTrue(users.getUsers().contains(student2));

        assertEquals(null, users.getUser(123));
        assertEquals(student2, users.getUser(654321));

        for (Student student : users) {
            assertTrue(users.getUsers().contains(student));
        }

    }

    @Test
    public void testCreatorConstructor() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        Users users = new Users(students);

        assertEquals(students, users.getUsers());

        Users newUsers = new Users();
        newUsers.setUsers(students);

        assertEquals(users.toString(), newUsers.toString());

    }
}
