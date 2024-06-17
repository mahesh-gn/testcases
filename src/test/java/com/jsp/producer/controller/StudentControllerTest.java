package com.jsp.producer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsp.producer.exceptions.StudentNotFoundException;
import com.jsp.producer.model.Student;
import com.jsp.producer.repository.StudentRepository;
import com.jsp.producer.responseEntity.CustomResponseEntity;
import com.jsp.producer.service.StudentProducer;
import com.jsp.producer.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@ExtendWith(MockitoExtension.class)

public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentRepository repository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private StudentProducer studentProducer;

    @MockBean
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentController studentController;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testGetStudentSuccess() throws Exception {
        // Arrange
        int studentId = 1;
        Student student = new Student(studentId, "John Doe", 22, "Java");
        CustomResponseEntity<Student> responseEntity = new CustomResponseEntity<>("Student retrieved successfully", student, HttpStatus.OK);
        when(studentService.getStudent(studentId)).thenReturn(new ResponseEntity<>(responseEntity, HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", studentId)).andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(studentId)).andExpect(jsonPath("$.data.name").value("John Doe")).andExpect(jsonPath("$.data.age").value("22")).andExpect(jsonPath("$.data.course").value("Java"));
    }

    @Test
    public void testGetStudentNotFound() throws Exception {
        int studentId = 1;
        when(studentService.getStudent(studentId)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/students/{id}", studentId)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetStudentInvalidId() throws Exception {

        mockMvc.perform(get("/api/students/{id}", "invalid")).andExpect(status().isBadRequest());
    }

    @Test
    public void getStudentByIdFailure() throws Exception {
        int id = 1;
        String errorMessage = "Student Not Found";
        Mockito.when(studentService.getStudentByID(id)).thenThrow(new StudentNotFoundException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/byId").param("id", String.valueOf(id))).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(content().string(errorMessage));

    }

    @Test
    public void getStudentByIdInvalidRequest() throws Exception {
        mockMvc.perform(get("/api/students/byId", "invalid")).andExpect(status().isBadRequest());
    }

    @Test
    public void m1() throws Exception {
        mockMvc.perform(get("/api/students/byId")
                        .param("id", String.valueOf(1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void m2() throws Exception {
        Student student = Student.builder()
                .id(1)
                .name("Abc")
                .age(22)
                .course("Java")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);

        Mockito.when(studentService.createStudent(student)).thenReturn(student);
        mockMvc.perform(post("/api/students/create")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }

}