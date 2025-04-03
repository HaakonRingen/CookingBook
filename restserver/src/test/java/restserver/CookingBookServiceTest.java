package restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import core.Book;
import core.Student;
import core.Users;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import restserver.repository.CookingBookRepository;




/**
 * Unit tests for the CookingBookService class.
 * 
 * This class uses Mockito to mock the CookingbookRepository and test the 
 * functionality of the CookingBookService.
 * 
 * The following tests are included:
 * 
 * <ul>
 * <li>{@link #testGetAllUsers()}: Tests that the service correctly retrieves all users.</li>
 * <li>{@link #testAddNewStudent()}: Tests that the service correctly adds a new student and 
 *   throws an exception if the student already exists.</li>
 * <li>{@link #testGetStudentById()}: Tests that the service retrieves a student by ID.</li>
 * <li>{@link #testSaveUsers()}: Tests that the service correctly saves the users.</li>
 * <li>{@link #testUpdateStudent()}: Tests that the service correctly updates a student and 
 *   throws exceptions for invalid updates.</li>
 * <li>{@link #testAccounts()}: Tests that the service correctly retrieves user accounts.</li>
 * </ul>
 * 
 * The {@link #setUp()} method initializes the mock objects and sets up the test data.
 * 
 * Dependencies:
 * <ul>
 * <li>Mockito for mocking dependencies.</li>
 * <li>JUnit Jupiter for unit testing.</li>
 * </ul>
 * 
 * Mocked Dependencies:
 * <ul>
 * <li>{@link CookingBookRepository}</li>
 * </ul>
 * 
 * Injected Mocks:
 * <ul>
 * <li>{@link CookingBookService}</li>
 * </ul>
 * 
 * Test Data:
 * <ul>
 * <li>{@link Student} user1: A sample student with ID 123456.</li>
 * <li>{@link Student} user2: A sample student with ID 654321.</li>
 * <li>{@link Users} users: A collection of sample users.</li>
 * </ul>
 */
public class CookingBookServiceTest {

    @Mock
    private CookingBookRepository repository;

    @InjectMocks
    private CookingBookService service;

    private final Student user1 = new Student("Test Testesen", 123456, "Passord", new Book());
    private final Student user2 = new Student("Tost Tostesen", 654321, "DårligPassord", new Book());

    private final Users users = new Users();
    

    /**
     * Sets up the test data and initializes the mock objects.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ArrayList<Student> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        users.setUsers(userList);
    }

    /**
     * Tests that the service correctly retrieves all users.
     */
    @Test
    public void testGetAllUsers() throws Exception {

        when(repository.load()).thenReturn(users.getUsers());

        Users usersReturned = service.getAllUsers();

        assertNotNull(usersReturned);
        assertNotNull(usersReturned.getUsers());
        assertTrue(usersReturned.getUsers().size() == 2);
    }

    /**
     * Tests that the service correctly adds a new student and 
     *   throws an exception if the student already exists.
     */
    @Test
    public void testAddNewStudent() throws Exception {

        when(repository.getStudent(123456)).thenReturn(user1);

        Student newUser = new Student("Ny Testbruker", 123456, "Passord", new Book());
        assertThrows(IllegalStateException.class, () -> service.addNewStudent(newUser));

        Student newUser2 = new Student("Ny Testbruker", 121212, "Passord", new Book());

        when(repository.getStudent(121212)).thenReturn(null);
        doNothing().when(repository).save(newUser2);

        service.addNewStudent(newUser2);
    }

    /**
     * Tests that the service retrieves a student by ID.
     */
    @Test
    public void testGetStudentById() throws Exception {

        when(repository.getStudent(123456)).thenReturn(user1);

        Student user = service.getStudentById(123456);

        assertNotNull(user);
        assertEquals(user1.toString(), user.toString());
    }

    /**
     * Tests that the service correctly saves the users.
     */
    @Test
    public void testSaveUsers() throws Exception {

        doNothing().when(repository).save(users);

        service.saveUsers(users);
    }

    /**
     * Tests that the service correctly updates a student and 
     *   throws exceptions for invalid updates.
     */
    @Test
    public void testUpdateStudent() throws Exception {

        when(repository.getStudent(123456)).thenReturn(user1);
        when(repository.getStudent(121212)).thenReturn(null);

        Student newUser = new Student("Ny Testbruker", 121212, "Passord", new Book());

        assertThrows(IllegalArgumentException.class, () -> service.updateStudent(121212, newUser));

        Student updatedUser = new Student("Oppdatert Testbruker", 123456, "Passord", new Book());

        assertThrows(IllegalStateException.class, () -> service.updateStudent(123456, newUser));

        doNothing().when(repository).save(updatedUser);
        service.updateStudent(123456, updatedUser);
    }

    /**
     * Tests that the service correctly retrieves user accounts.
     */
    @Test
    public void testAccounts() throws Exception {

        when(repository.load()).thenReturn(users.getUsers());

        HashMap<Integer, String> accounts = service.getAccounts();

        assertTrue(accounts.containsKey(123456));
        assertFalse(accounts.containsKey(121212));

        assertTrue((accounts.get(123456).equals("Passord")));
    }
    
}
