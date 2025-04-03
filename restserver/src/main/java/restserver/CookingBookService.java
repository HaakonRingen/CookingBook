package restserver;

import core.Student;
import core.Users;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restserver.repository.CookingBookRepository;

/**
 * Service class for managing cooking book operations.
 */
@Service
public class CookingBookService {

    private final CookingBookRepository repository;

  
    @Autowired
    public CookingBookService(final CookingBookRepository repository) {
        this.repository = repository;
    }

    public Users getAllUsers() throws Exception {
        return new Users(repository.load());
    }

    /**
     * Adds a new student to the repository.
     *
     * @param student the student to be added
     * @throws Exception if there is an error during the operation
     */
    public void addNewStudent(Student student) throws Exception {
        if (repository.getStudent(student.getId()) != null) {
            throw new IllegalStateException("Student with ID "
                    + student.getId() + " already exists.");
        }
        repository.save(student);
    }

    public Student getStudentById(int id) throws Exception {
        return repository.getStudent(id);
    }

    public void saveUsers(Users users) throws Exception {
        repository.save(users);
    }

    /**
     * Updates an existing student in the repository.
     *
     * @param id             the ID of the student to be updated
     * @param updatedStudent the updated student object
     * @throws Exception if there is an error during the operation
     */
    public void updateStudent(int id, Student updatedStudent) throws Exception {

        Student existingStudent = repository.getStudent(id);
        if (existingStudent != null) {

            if (updatedStudent.getId() != id) {
                throw new IllegalStateException("Student ID cannot be changed.");
            }
            repository.save(updatedStudent);
        } else {
            throw new IllegalArgumentException("Student with ID " + id + " does not exist.");
        }
    }

    /**
     * Retrieves a map of student IDs and their corresponding passwords.
     *
     * @return a HashMap where the key is the student ID and the value is the
     *         student's password
     * @throws Exception if there is an error during the operation
     */
    public HashMap<Integer, String> getAccounts() throws Exception {
        ArrayList<Student> students = repository.load();
        HashMap<Integer, String> accounts = new HashMap<>();
        for (Student student : students) {
            accounts.put(student.getId(), student.getPassword());
        }
        return accounts;
    }

}
