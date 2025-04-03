package restserver.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import core.Book;
import core.Student;
import core.Users;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import persistence.UsersHandler;


/**
 * Unit tests for the CookingBookRepository class.
 * 
 * This class uses Mockito to mock the UsersHandler and test the 
 * functionality of the CookingBookRepository.
 * 
 * The following tests are included:
 * 
 * <ul>
 * <li>{@link #testLoadUsers()}: Tests that the repository correctly loads users.</li>
 * <li>{@link #testGetStudentById()}: Tests that the repository retrieves a student by ID.</li>
 * <li>{@link #testSaveUsers()}: Tests that the repository correctly saves the users.</li>
 * <li>{@link #testSaveSingleUser()}: Tests that the repository correctly saves a single user.</li>
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
 * <li>{@link UsersHandler}</li>
 * </ul>
 * 
 * Injected Mocks:
 * <ul>
 * <li>{@link CookingBookRepository}</li>
 * </ul>
 */
public class CookingBookRepositoryTest {
    
    @Mock
    private UsersHandler handler;

    @InjectMocks
    private CookingBookRepository repository;

    private final Student user1 = new Student("Test Testesen", 123456, "Passord", new Book());
    private final Student user2 = new Student("Tost Tostesen", 654321, "DårligPassord", new Book());

    private final Users users = new Users();

    /**
     * Sets up the test data and initializes the mock objects.
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ArrayList<Student> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        users.setUsers(userList);

        when(handler.read()).thenReturn(users.getUsers());
    }

    /**
     * Tests that the repository correctly loads users.
     */
    @Test
    public void testLoadUsers() throws Exception {
        ArrayList<Student> loadedUsers = repository.load();

        assertNotNull(loadedUsers);
        assertFalse(loadedUsers.isEmpty());
    }

    /**
     * Tests that the repository retrieves a student by ID.
     */
    @Test
    public void testGetStudentById() throws Exception {
        Student student = repository.getStudent(123456);
        
        assertNotNull(student);
        assertEquals(user1.toString(), student.toString());
        assertNull(repository.getStudent(121212));
    }

    /**
     * Tests that the repository correctly saves the users.
     */
    @Test
    public void testSaveUsers() throws Exception {
        doNothing().when(handler).write(users);
        repository.save(users);

        ArrayList<Student> loadedUsers = repository.load();

        assertNotNull(loadedUsers);
        assertFalse(loadedUsers.isEmpty());
    }

    /**
     * Tests that the repository correctly saves a single user.
     */
    @Test
    public void testSaveSingleUser() throws Exception {
        doNothing().when(handler).write(users);
        Student newUser = new Student("Ny bruker", 123123, "NyttPassord", new Book());

        repository.save(newUser);

        repository.save(repository.getStudent(123456));

    }
}
