package core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Users class represents a collection of Student objects.
 * It provides methods to add, retrieve, and iterate over the students.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class Users implements Iterable<Student> {
    
    private ArrayList<Student> users;

    public Users() {
        users = new ArrayList<>();
    }

    /**
     * Constructs a Users object with a given list of students.
     * 
     * @param list the list of students to initialize the Users object with
     * @throws IllegalArgumentException if any element in the list is null
     */
    @JsonCreator
    public Users(ArrayList<Student> list) {
        for (Student student : list) {
            if (student == null) {
                throw new IllegalArgumentException("All elements must not be null");
            }
        }
        this.users = list;
    }

    /**
     * Adds a student to the users list.
     *
     * @param user the student to add
     * @throws IllegalArgumentException if the user is null
     */
    public void addUser(Student user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.add(user);
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id the ID of the student to retrieve
     * @return the student with the given ID, or null if not found
     */
    public Student getUser(int id) {
        for (Student user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @JsonProperty("users")
    public ArrayList<Student> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(ArrayList<Student> users) {
        this.users = new ArrayList<>(users);
    }

    @Override
    public Iterator<Student> iterator() {
        return users.iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Student user : users) {
            result.append(" ").append(user.toString());
        }
        return result.toString();
    }
    
}
