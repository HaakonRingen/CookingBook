package restserver;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Book;
import core.Recipe;
import core.Student;
import core.Users;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



/**
 * Unit tests for the CookingBookController class.
 * 
 * This class uses MockMvc and Mockito to test the functionality of the 
 * CookingBookController, which handles HTTP requests for managing students 
 * and their cooking books.
 * 
 * The following tests are included:
 * 
 * <ul>
 * <li>{@link #testGetAllUsers()}: Tests that the controller retrieves all users 
 *   and returns the correct JSON response.</li>
 * <li>{@link #testGetUserById_shouldBe_Success()}: Tests that the controller retrieves 
 *   a student by ID when the student exists and returns the correct JSON response.</li>
 * <li>{@link #testGetUserById_NotFound()}: Tests that the controller returns a 404 status 
 *   when trying to retrieve a student by an ID that does not exist.</li>
 * <li>{@link #testGetAccounts()}: Tests that the controller retrieves all user accounts 
 *   (ID and password pairs) and returns the correct JSON response.</li>
 * <li>{@link #testAddStudent()}: Tests that the controller correctly adds a new student 
 *   and calls the service method with the expected student details.</li>
 * <li>{@link #testUpdateStudent()}: Tests that the controller updates an existing student's 
 *   cooking book with new recipes and verifies that the service method is called with the 
 *   correct updated details.</li>
 * </ul>
 * 
 * The {@link #setUp()} method initializes the mock objects and sets up the test data 
 * before each test.
 * 
 * Dependencies:
 * <ul>
 * <li>MockMvc for simulating HTTP requests and responses.</li>
 * <li>Mockito for mocking service dependencies.</li>
 * <li>Jackson ObjectMapper for JSON serialization and deserialization.</li>
 * </ul>
 * 
 * Mocked Dependencies:
 * <ul>
 * <li>{@link CookingBookService}: The service class mocked to simulate business logic 
 *   without hitting the actual database or backend logic.</li>
 * </ul>
 * 
 * Test Data:
 * <ul>
 * <li>{@link Student} user1: A sample student with ID 123456 and an empty cooking book.</li>
 * <li>{@link Student} user2: A sample student with ID 654321 and an empty cooking book.</li>
 * <li>{@link Users} users: A collection of sample users containing user1 and user2.</li>
 * </ul>
 */
@WebMvcTest(CookingBookController.class)
public class CookingBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CookingBookService service;



    private final Student user1 = new Student("Test Testesen", 123456, "Passord", new Book());
    private final Student user2 = new Student("Tost Tostesen", 654321, "DårligPassord", new Book());

    private final Users users = new Users();
    


    /**
     * Sets up the test environment before each test.
     * 
     * @throws Exception if an error occurs during setup
     */
    @BeforeEach
    public void setUp() throws Exception {

        ArrayList<Student> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        users.setUsers(userList);
    }

    /**
     * Tests the retrieval of all users.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetAllUsers() throws Exception {
        when(service.getAllUsers()).thenReturn(users);
        MvcResult result = mockMvc.perform(get("/cookingbook/users"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String expectedJson = "{\n"
                + "  \"users\" : [ {\n"
                + "    \"name\" : \"Test Testesen\",\n"
                + "    \"ID\" : 123456,\n"
                + "    \"password\" : \"Passord\",\n"
                + "    \"cookingbook\" : {\n"
                + "      \"recipes\" : [ ]\n"
                + "    }\n"
                + "  }, {\n"
                + "    \"name\" : \"Tost Tostesen\",\n"
                + "    \"ID\" : 654321,\n"
                + "    \"password\" : \"DÃ¥rligPassord\",\n"
                + "    \"cookingbook\" : {\n"
                + "      \"recipes\" : [ ]\n"
                + "    }\n"
                + "  } ]\n"
                + "}";
        String actualJson = result.getResponse().getContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    /**
     * Tests the retrieval of a user by ID when the user exists.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetUserById_shouldBe_Success() throws Exception {

        int userId = user1.getId();
        when(service.getStudentById(userId)).thenReturn(user1);


        mockMvc.perform(get("/cookingbook/users/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ID").value(user1.getId()))
                .andExpect(jsonPath("$.name").value(user1.getFullName()));
    }

    /**
     * Tests the retrieval of a user by ID when the user does not exist.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetUserById_NotFound() throws Exception {

        int userId = 999999;
        when(service.getStudentById(userId)).thenReturn(null);


        mockMvc.perform(get("/cookingbook/users/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    /**
     * Tests the retrieval of user accounts.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetAccounts() throws Exception {
        HashMap<Integer, String> accounts = new HashMap<>();
        for (Student user : users.getUsers()) {
            accounts.put(user.getId(), user.getPassword());
        }

        when(service.getAccounts()).thenReturn(accounts);

        MvcResult result = mockMvc.perform(get("/cookingbook/users/accounts"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String expectedJson = "{\n"
                + "  \"123456\" : \"Passord\",\n"
                + "  \"654321\" : \"DÃ¥rligPassord\"\n"
                + "}";

        String actualJson = result.getResponse().getContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    /**
     * Tests the addition of a new student.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testAddStudent() throws Exception {

        Student expected = user1;
        doNothing().when(service).addNewStudent(any());

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/cookingbook/users")
                .content(mapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


        verify(service).addNewStudent(argThat(student -> 
                student.getId() == expected.getId()
                && student.getFullName().equals(expected.getFullName())
        ));
    }

    /**
     * Tests the update of an existing student.
     * 
     * @throws JsonProcessingException if an error occurs during JSON processing
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testUpdateStudent() throws JsonProcessingException, Exception {
        Student updatedStudent = user1;
        Book newBook = new Book();
        newBook.addRecipe(new Recipe("Recipe 1"));
        newBook.addRecipe(new Recipe("Recipe 2"));
        updatedStudent.setCookingbook(newBook);

        ObjectMapper mapper = new ObjectMapper();
        Student initialStudent = user1;

        mockMvc.perform(put("/cookingbook/users/" + initialStudent.getId())
            .content(mapper.writeValueAsString(updatedStudent))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        verify(service).updateStudent(eq(initialStudent.getId()), argThat(student -> {

            Book book = student.getCookingbook();
            return book != null 
                && book.getRecipes().size() == 2 
                && book.getRecipes().get(0).getNavn().equals("Recipe 1") 
                && book.getRecipes().get(1).getNavn().equals("Recipe 2");
        }));

        assertTrue(initialStudent
            .getCookingbook().getRecipes().size() 
            == updatedStudent
            .getCookingbook().getRecipes().size());

    }
            
}
