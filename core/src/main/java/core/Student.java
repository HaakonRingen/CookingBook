package core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a student with a first name, last name, student ID, password, and a cooking book.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class Student {

    private String firstname;
    private String lastname;
    private int studentId;
    private String password;
    private Book cookingbook;

    /**
     * Constructs a new Student with the specified name, ID, password, and cooking book.
     *
     * @param name the full name of the student
     * @param id the student ID
     * @param password the password of the student
     * @param cookingBook the cooking book of the student
     */
    @JsonCreator
    public Student(@JsonProperty("name") String name,
        @JsonProperty("ID") int id,
        @JsonProperty("password") String password,
        @JsonProperty("cookingbook") Book cookingBook) {
        String [] names = name.split(" ");
        this.firstname = names[0];
        this.lastname = names.length > 1 ? names[1] : "";
        this.studentId = id;
        this.password = password;
        this.cookingbook = cookingBook;
    }

    public Student() {}

    /**
     * Constructs a new Student with the specified first name, last name, ID, and password.
     *
     * @param firstname the first name of the student
     * @param lastname the last name of the student
     * @param studentId the student ID
     * @param password the password of the student
     */
    public Student(String firstname, String lastname, int studentId, String password) {
        if (firstname == null || firstname.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastname == null || lastname.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (String.valueOf(studentId).length() != 6) {
            throw new IllegalArgumentException("Student ID must be a 6-digit number");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        
        this.firstname = firstname;
        this.lastname = lastname;
        this.studentId = studentId;
        this.password = password;
        this.cookingbook = new Book();
    }
    // Setters

    /**
     * Sets the first name of the student.
     *
     * @param firstname the first name to set
     * @throws IllegalArgumentException if the first name is null or empty
     */
    public void setFirstName(String firstname) {
        if (firstname == null || firstname.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstname = firstname;
    }

    /**
     * Sets the last name of the student.
     *
     * @param lastname the last name to set
     * @throws IllegalArgumentException if the last name is null or empty
     */
    public void setLastName(String lastname) {
        if (lastname == null || lastname.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastname = lastname;
    }

    public void setCookingbook(Book cookingbook) {
        this.cookingbook =  new Book(cookingbook);
    }

    // Getters
    @JsonProperty("name")
    public String getFullName() {
        return firstname + " " + lastname;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    @JsonProperty("ID")
    public int getId() {
        return studentId;
    }
    
    @JsonProperty("cookingbook")
    public Book getCookingbook() {
        return cookingbook;
    }

    @Override
    public String toString() {
        return String.valueOf(studentId);
    }
    
}