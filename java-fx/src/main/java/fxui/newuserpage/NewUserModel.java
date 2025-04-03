package fxui.newuserpage;


import core.Student;
import fxui.remote.CookingBookRemoteAccess;

/**
 * The NewUserModel class is responsible for creating new user accounts.
 * It interacts with the CookingBookRemoteAccess to add new students.
 */
public final class NewUserModel {


    private Boolean success = false;
    private CookingBookRemoteAccess access;

    public NewUserModel() throws Exception {
        this.access = new CookingBookRemoteAccess();
    }

    /**
     * Creates a new user with the given details.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param id the ID of the user
     * @param password the password of the user
     * @param confirmPassword the confirmation of the password
     */
    public void createUser(String firstName, String lastName, String id,
        String password, String confirmPassword) {
        checkParams(firstName, lastName, id, password, confirmPassword);
        try {
            int parsedId = Integer.parseInt(id); // Ensuring ID is a number
            Student newStudent = new Student(firstName, lastName, parsedId, password);
            access.addStudent(newStudent);
            success = true;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Student ID must be a valid 6-digit number");
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        
    }

    /**
     * Checks the parameters for user creation and validates their correctness.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param id the ID of the user
     * @param password the password of the user
     * @param confirmPassword the confirmation of the password
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void checkParams(String firstName, String lastName, String id, 
                             String password, String confirmPassword) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty() || id.length() != 6 || !id.matches("\\d{6}")) {
            throw new IllegalArgumentException("Student ID must be a 6-digit number");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (firstName.length() < 2) {
            throw new IllegalArgumentException("First name must be at least 2 characters long");
        }
        if (lastName.length() < 2) {
            throw new IllegalArgumentException("Last name must be at least 2 characters long");
        }
    }


    /**
     * Returns the success status of the user creation process.
     * 
     * @return true if the user was successfully created, false otherwise
     */
    public Boolean getSuccess() {
        Boolean value = success;
        success = false;
        return value;
    }

    public void setAccess(CookingBookRemoteAccess access) {
        this.access = access;
    }
    
}
