package restserver.repository;



import core.Student;
import core.Users;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistence.UsersHandler;

/**
 * Repository class for handling cooking book related operations.
 * 
 * This class provides methods to load, retrieve, and save student information 
 * associated with a cooking book application. It interacts with the {@link UsersHandler}
 * for reading and writing data.
 * 
 * <ul>
 * <li>{@link #load()}: Loads all students from the repository.</li>
 * <li>{@link #getStudent(int)}: Retrieves a specific student by their ID.</li>
 * <li>{@link #save(Users)}: Saves a collection of users to the repository.</li>
 * <li>{@link #save(Student)}: Saves an individual student, updating if already exists.</li>
 * </ul>
 */
@Repository
public class CookingBookRepository {
    
    private final UsersHandler handler;

    @Autowired
    public CookingBookRepository(UsersHandler handler) {
        this.handler = handler;
    }

    public ArrayList<Student> load() throws Exception {
        return handler.read();
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id the ID of the student to retrieve
     * @return the student with the specified ID, or null if not found
     * @throws Exception if an error occurs during the operation
     */
    public Student getStudent(int id) throws Exception {
        ArrayList<Student> students = handler.read();
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public void save(Users users) throws Exception {
        handler.write(users);
    }

    /**
     * Saves a student to the repository.
     *
     * @param student the student to save
     * @throws Exception if an error occurs during the operation
     */
    public void save(Student student) throws Exception {
        ArrayList<Student> students = handler.read();
        boolean studentExists = false;


        for (Student stud : students) {
            if (stud.getId() == student.getId()) {
                students.set(students.indexOf(stud), student);
                studentExists = true;
                break;
            }
        }

        if (!studentExists) {
            students.add(student);
        }

        handler.write(new Users(students));
    }

}