package persistence;


import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Student;
import core.Users;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles the persistence of Users data, including reading from and writing to a JSON file.
 */
public class UsersHandler {

    private File savedRecipesFile;
    private ObjectMapper mapper;
    
    /**
     * Constructs a new UsersHandler and initializes the JSON file and ObjectMapper.
     */
    public UsersHandler() {
        savedRecipesFile = new File("SavedUsers.json");
        mapper = new ObjectMapper();
        mapper.registerModule(new BookStudentModule());
    }

    /**
     * Constructs a new UsersHandler and initializes the JSON file and ObjectMapper.
     * @param filename the name of the file to be used for reading and writing
     */
    public UsersHandler(String filename) {
        savedRecipesFile = new File(filename);
        mapper = new ObjectMapper();
        mapper.registerModule(new BookStudentModule());
    }

    /**
     * Sets the filename to be used for reading and writing.
     *
     * @param filename the name of the file to be used for reading and writing
     */
    public void setFilename(String filename) {
        savedRecipesFile = new File(filename);
    }

   
    /**
     * Writes the given Users object to a file using a pretty printer format.
     *
     * @param users the Users object to be written to the file
     * @throws StreamWriteException if there is an error during the writing process
     * @throws DatabindException if there is an error related to data binding
     * @throws IOException if an I/O error occurs
     */
    public void write(Users users) throws StreamWriteException, DatabindException, IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(savedRecipesFile, users);
    }
    
    /**
     * Reads the JSON file and returns a list of Student objects.
     *
     * @return an ArrayList of Student objects read from the file
     * @throws StreamReadException if there is an error during reading from the file
     * @throws DatabindException if there is an error in data binding
     * @throws IOException if there is an I/O error
     */
    public ArrayList<Student> read() throws StreamReadException, DatabindException, IOException {
        return mapper.readValue(savedRecipesFile, Users.class).getUsers();
    }

}