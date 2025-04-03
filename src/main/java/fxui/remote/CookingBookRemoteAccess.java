package fxui.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Student;
import core.Users;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides remote access to the CookingBook service.
 */
public class CookingBookRemoteAccess {

    private static final String BASE_URL = "http://localhost:8080/cookingbook/users";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new CookingBookRemoteAccess instance.
     * Initializes the HttpClient and ObjectMapper with the necessary modules.
     */
    public CookingBookRemoteAccess() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public CookingBookRemoteAccess(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Get all users from the CookingBook service.
     * @return List of Students
     * @throws Exception if there is an error during the HTTP request or response processing
     */
    public ArrayList<Student> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
    
        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());
    
        if (response.statusCode() == 200) {
            // Directly deserialize the JSON into a list of Student objects
            return objectMapper.readValue(response.body(), Users.class).getUsers();
        } else {
            throw new IOException("Failed to get students, status code: " + response.statusCode());
        }
    }

    /**
     * Get a specific student by ID from the CookingBook service.
     * @param id Student ID
     * @return Student
     * @throws Exception if there is an error during the HTTP request or response processing
     */
    public Student getStudentById(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper map = new ObjectMapper();
        if (response.statusCode() == 200) {
            return map.readValue(response.body(), Student.class);
        } else {
            throw new IOException("Failed to get student, status code: " + response.statusCode());
        }
    }

    /**
     * Update an existing student in the CookingBook service.
     * @param id The student ID to update
     * @param student The updated student data
     * @throws Exception if there is an error during the HTTP request or response processing
     */
    public void updateStudent(int id, Student student) throws Exception {
        String studentJson = objectMapper.writeValueAsString(student);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(studentJson))
                .build();

        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to update student, status code: " 
                    + response.statusCode());
        }
    }

    private HashMap<Integer, String> loadAccountsFromServer() 
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/cookingbook/users/accounts"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
            request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {

            // Deserialize the response into a HashMap<Integer, String>
            ObjectMapper objectMap = new ObjectMapper();
            return objectMap.readValue(
                response.body(), 
                new com.fasterxml.jackson.core.type.TypeReference<HashMap<Integer, String>>() {}
            );
        } else {
            throw new IOException("Failed to fetch accounts from server, status code: " 
                    + response.statusCode());
        }
    }

    public boolean validateStudent(int id, String password) 
            throws IOException, InterruptedException {
        HashMap<Integer, String> users = loadAccountsFromServer();
        return users.containsKey(id) && users.get(id).equals(password);
    }

    /**
     * Adds a new student to the CookingBook service.
     * @param student The student to add
     * @throws IOException if there is an error during the HTTP request or response processing
     * @throws InterruptedException if the operation is interrupted
     */
    public void addStudent(Student student) throws IOException, InterruptedException {
        ObjectMapper objectMap = new ObjectMapper();
        String studentJson = objectMap.writeValueAsString(student);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/cookingbook/users"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(studentJson))
            .build();

        HttpResponse<String> response = httpClient.send(
            request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to add student. Status code: " + response.statusCode());
        } else {
            System.out.println("Student added successfully!");
        }
    }

}