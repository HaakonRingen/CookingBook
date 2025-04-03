package fxui.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Student;
import core.Users;
import fxui.remote.CookingBookRemoteAccess;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link CookingBookRemoteAccess}.
 * This class tests interactions between the remote access layer and HTTP requests/responses.
 */
public class CookingBookRemoteTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @InjectMocks
    private CookingBookRemoteAccess remoteAccess;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sets up the test environment before each test is run.
     * Initializes mocks and the instance of {@link CookingBookRemoteAccess}.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        remoteAccess = new CookingBookRemoteAccess(mockHttpClient, objectMapper);
    }

    /**
     * Tests the successful retrieval of all users from a remote endpoint.
     *
     * @throws Exception if an error occurs during HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllUsers_successfulResponse() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        Users mockUsers = new Users();
        mockUsers.setUsers(new ArrayList<>());
        String jsonResponse = objectMapper.writeValueAsString(mockUsers);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        // Act
        ArrayList<Student> result = remoteAccess.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Tests the behavior of getting all users when the response fails with a non-200 status code.
     *
     * @throws IOException if an I/O error occurs during HTTP request.
     * @throws InterruptedException if the request is interrupted.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllUsers_failedResponse() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(IOException.class, () -> remoteAccess.getAllUsers());
    }

    /**
     * Tests the successful retrieval of a student by ID from a remote endpoint.
     *
     * @throws Exception if an error occurs during HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetStudentById_successfulResponse() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        Student mockStudent = new Student();
        String jsonResponse = objectMapper.writeValueAsString(mockStudent);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        Student result = remoteAccess.getStudentById(1);

        assertNotNull(result);
    }

    /**
     * Tests the behavior of getting a student by ID
     *  when the response fails with a non-200 status code.
     *
     * @throws IOException if an I/O error occurs during HTTP request.
     * @throws InterruptedException if the request is interrupted.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetStudentById_failedResponse() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(404);

        assertThrows(IOException.class, () -> remoteAccess.getStudentById(1));
    }

    /**
     * Tests the successful update of a student via a remote endpoint.
     *
     * @throws Exception if an error occurs during HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStudent_successfulResponse() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        Student mockStudent = new Student();

        remoteAccess.updateStudent(1, mockStudent);

        verify(mockHttpClient, times(1)).send(any(
            HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    /**
     * Tests the behavior of updating a student when the response fails with a non-200 status code.
     *
     * @throws IOException if an I/O error occurs during HTTP request.
     * @throws InterruptedException if the request is interrupted.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStudent_failedResponse() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(
            HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);
        Student mockStudent = new Student();

        assertThrows(IOException.class, () -> remoteAccess.updateStudent(1, mockStudent));
    }

    

    /**
     * Tests adding a student with a failed response from the remote endpoint.
     *
     * @throws IOException if an I/O error occurs during HTTP request.
     * @throws InterruptedException if the request is interrupted.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddStudent_failedResponse() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);
        Student mockStudent = new Student();

        assertThrows(IOException.class, () -> remoteAccess.addStudent(mockStudent));
    }


    /**
     * Tests validating a student with invalid credentials.
     *
     * @throws Exception if an error occurs during HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testValidateStudent_invalidCredentials() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);

        HashMap<Integer, String> mockAccounts = new HashMap<>();
        mockAccounts.put(1, "correctPassword");
        String jsonResponse = objectMapper.writeValueAsString(mockAccounts);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        boolean isValid = remoteAccess.validateStudent(1, "wrongPassword");

        assertFalse(isValid, "The validation should return false for invalid credentials.");
    }

    /**
     * Tests validating a student when the user is not found in the system.
     *
     * @throws Exception if an error occurs during HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testValidateStudent_userNotFound() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);

        HashMap<Integer, String> mockAccounts = new HashMap<>();
        String jsonResponse = objectMapper.writeValueAsString(mockAccounts);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        boolean isValid = remoteAccess.validateStudent(2, "somePassword");

        assertFalse(isValid, "The validation should return false for a non-existent user.");
    }
}