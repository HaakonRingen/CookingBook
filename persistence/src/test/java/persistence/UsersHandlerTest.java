package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Book;
import core.Student;
import core.Users;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for the {@link UsersHandler} class.
 * 
 * This class verifies the file operations of the {@code UsersHandler} class, specifically
 * the writing and reading of {@code Users} data to and from JSON files.
 * 
 * <ul>
 * <li>{@link #testWrite()}: Tests the writing of a {@code Users}
 *  object to a temporary JSON file.</li>
 * <li>{@link #testRead()}: Tests the reading of a {@code Users}
 *  object from a JSON file and verifies the contents.</li>
 * </ul>
 */
public class UsersHandlerTest {
    
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BookStudentModule());
    }

    @TempDir
    File tempDir;

    /*
    * Verify that the writer works
    */
    /*
    * Verify that the writer works
    */
    @Test
    public void testWrite() {

        File tempFile = new File(tempDir, "temp.json");

        UsersHandler handler = new UsersHandler(tempFile.getAbsolutePath());

        Users users = new Users();
        Student student = new Student("Test Testesen", 123456, "Testpassord", new Book());
        users.addUser(student);
        try {
            handler.write(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Verify that the reafer works
    */
    /*
    * Verify that the reader works
    */
    @Test
    public void testRead() {
        File tempFile = new File(tempDir, "temp.json");

        UsersHandler handler = new UsersHandler();
        handler.setFilename(tempFile.getAbsolutePath());

        Users users = new Users();
        Student student = new Student("Test Testesen", 123456, "Testpassord", new Book());
        users.addUser(student);
        try {
            handler.write(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Users readUsers = new Users();
        try {
            readUsers.setUsers(handler.read());
            System.out.println(readUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Test", readUsers.getUser(123456).getFirstName());
    }
}
