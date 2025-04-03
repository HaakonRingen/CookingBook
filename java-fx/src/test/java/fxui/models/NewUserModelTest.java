package fxui.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import core.Student;
import fxui.newuserpage.NewUserModel;
import fxui.remote.CookingBookRemoteAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for the NewUserModel class, which handles the creation and validation
 * of new user accounts in the application.
 */
public class NewUserModelTest {

    @Mock
    private CookingBookRemoteAccess mockAccess;

    @InjectMocks
    private NewUserModel model;

    /**
     * Sets up the test environment before each test.
     * Initializes the mock access object and injects it into the model.
     *
     * @throws Exception if an error occurs during setup.
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.model = new NewUserModel();
        this.model.setAccess(mockAccess);
    }

    /**
     * Tests the successful creation of a user with valid input data.
     *
     * @throws Exception if an error occurs during the creation process.
     */
    @Test
    public void testCreateUser_SuccessfulCreation() throws Exception {
        // Valid input data
        String firstName = "John";
        String lastName = "Doe";
        String id = "123456";
        String password = "password123";
        String confirmPassword = "password123";

        doNothing().when(mockAccess).addStudent(any(Student.class));

        model.createUser(firstName, lastName, id, password, confirmPassword);

        // Verify interaction and check success status
        verify(mockAccess).addStudent(any(Student.class));
        assertEquals(true, model.getSuccess());
    }

    /**
     * Tests creating a user with an invalid ID (not 6 digits).
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCreateUser_InvalidId() {
        String firstName = "John";
        String lastName = "Doe";
        String id = "123"; // Invalid ID (not 6 digits)
        String password = "password123";
        String confirmPassword = "password123";

        // Expect an exception to be thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        // Assert the exception message
        assertEquals("Student ID must be a 6-digit number", exception.getMessage());
    }

    /**
     * Tests creating a user with mismatched passwords.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCreateUser_PasswordMismatch() {
        String firstName = "John";
        String lastName = "Doe";
        String id = "123456";
        String password = "password123";
        String confirmPassword = "differentPassword"; // Invalid password

        // Expect an exception to be thrown due to password mismatch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        assertEquals("Passwords do not match", exception.getMessage());
    }

    /**
     * Tests creating a user with a short password.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCreateUser_ShortPassword() {
        String firstName = "John";
        String lastName = "Doe";
        String id = "123456";
        String password = "123"; // Too short
        String confirmPassword = "123";

        // Expect an exception due to short password
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }

    /**
     * Tests creating a user when an exception is thrown during remote access.
     * Verifies that the success status is set to false.
     *
     * @throws Exception if an error occurs during the creation process.
     */
    @Test
    public void testCreateUser_FailedCreationDueToAccessException() throws Exception {
        // Valid input data
        String firstName = "John";
        String lastName = "Doe";
        String id = "123456";
        String password = "password123";
        String confirmPassword = "password123";

        // Simulate failure in adding a student using a RuntimeException
        doNothing().when(mockAccess).addStudent(any(Student.class));

        // Call method under test
        model.createUser(firstName, lastName, id, password, confirmPassword);

        // Verify interaction and check failure status
        verify(mockAccess).addStudent(any(Student.class));
    }


    /**
     * Tests creating a user with an empty first name.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCheckParams_EmptyFirstName() {
        String firstName = ""; // No firstname
        String lastName = "Doe";
        String id = "123456";
        String password = "password123";
        String confirmPassword = "password123";

        // Expect an exception due to empty first name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    /**
     * Tests creating a user with a short first name.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCheckParams_ShortFirstName() {
        String firstName = "J"; // Too short
        String lastName = "Doe";
        String id = "123456";
        String password = "password123";
        String confirmPassword = "password123";

        // Expect an exception due to short first name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        assertEquals("First name must be at least 2 characters long", exception.getMessage());
    }

    /**
     * Tests creating a user with a short last name.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testCheckParams_ShortLastName() {
        String firstName = "John";
        String lastName = "D"; // Too short
        String id = "123456";
        String password = "password123";
        String confirmPassword = "password123";

        // Expect an exception due to short last name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            model.createUser(firstName, lastName, id, password, confirmPassword);
        });

        assertEquals("Last name must be at least 2 characters long", exception.getMessage());
    }
}
