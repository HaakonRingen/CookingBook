package restserver;

import core.Student;
import core.Users;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * REST controller for managing cooking book users.
 * 
 * This controller provides endpoints for retrieving, adding, and updating users
 * within the cooking book application.
 * 
 * <ul>
 * <li>{@link #getAllUsers()}: Retrieves all users.</li>
 * <li>{@link #getUserById(int)}: Retrieves a user by ID.</li>
 * <li>{@link #getAccounts()}: Retrieves a list of user accounts with IDs and names.</li>
 * <li>{@link #addNewStudent(Student)}: Adds a new student.</li>
 * <li>{@link #updateStudent(int, Student)}: Updates an existing student by ID.</li>
 * </ul>
 */
@RestController
@RequestMapping(path = "cookingbook/users")
public class CookingBookController {

    private final CookingBookService cookingBookService;

    @Autowired
    public CookingBookController(CookingBookService cookingBookService) {
        this.cookingBookService = cookingBookService;
    }


    @GetMapping
    public ResponseEntity<Users> getAllUsers() throws Exception {
        return ResponseEntity.ok(cookingBookService.getAllUsers());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID, or a 404 status if not found
     * @throws Exception if an error occurs during retrieval
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getUserById(@PathVariable int id) throws Exception {
        Student student = cookingBookService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves a list of user accounts with their IDs and names.
     *
     * @return a {@code ResponseEntity} containing a map of user IDs to names
     * @throws Exception if an error occurs during retrieval
     */
    @GetMapping("/accounts")
    public ResponseEntity<HashMap<Integer, String>> getAccounts() throws Exception {
        HashMap<Integer, String> accounts = cookingBookService.getAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Adds a new student to the cooking book application.
     *
     * @param student the {@link Student} object to add
     * @return a {@code ResponseEntity} with a 200 status upon successful addition
     * @throws Exception if an error occurs during the operation
     */
    @PostMapping
    public ResponseEntity<Void> addNewStudent(@RequestBody Student student) throws Exception {
        cookingBookService.addNewStudent(student);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing student by their ID.
     *
     * @param id the ID of the student to update
     * @param student the updated {@link Student} object
     * @return a {@code ResponseEntity} with a 200 status upon successful update
     * @throws Exception if an error occurs during the operation
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable int id, @RequestBody Student student)
        throws Exception {
        cookingBookService.updateStudent(id, student);
        return ResponseEntity.ok().build();
    }
    
}