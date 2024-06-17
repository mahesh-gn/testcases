package com.jsp.producer.service;

import com.jsp.producer.exceptions.StudentAlreadyExistsException;
import com.jsp.producer.exceptions.StudentNotFoundException;
import com.jsp.producer.model.Student;
import com.jsp.producer.repository.StudentRepository;
import com.jsp.producer.responseEntity.CustomResponseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StudentProducer studentProducer;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    String consumerURL = "http://localhost:8081/consumer";

    @Test
    public void createStudent_Success() {
        Student student = new Student();
        student.setId(1);
        Mockito.when(repository.existsById(student.getId())).thenReturn(false);
        Mockito.when(repository.save(student)).thenReturn(student);

        Mockito.when(restTemplate.postForObject(consumerURL, student, Student.class)).thenReturn(student);

        String mappedStudent = modelMapper.map(student, String.class);
        Mockito.doNothing().when(studentProducer).addStudent("add-student", 0, mappedStudent);

        Student student1 = studentService.createStudent(student);
        Mockito.verify(repository, Mockito.times(1)).save(student);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(consumerURL, student, Student.class);
        Mockito.verify(studentProducer, Mockito.times(1)).addStudent("add-student", 0, mappedStudent);
    }


    @Test
    public void createStudent_Failure() {
        Student student = new Student();
        student.setId(1);
        Mockito.when(repository.existsById(student.getId())).thenReturn(true);
        Assertions.assertThrows(StudentAlreadyExistsException.class, () -> studentService.createStudent(student));
        Mockito.verify(repository, Mockito.times(1)).existsById(1);
        Mockito.verify(repository, Mockito.never()).save(student);
        Mockito.verify(restTemplate, Mockito.never()).postForObject(Mockito.anyString(), Mockito.any(), Mockito.any());
        Mockito.verify(studentProducer, Mockito.never()).addStudent(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString());

    }

    @Test
    public void deleteStudentTest() {
        Mockito.when(repository.existsById(1)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(1);
        studentService.deleteStudent(1);
        Mockito.verify(repository, Mockito.times(1)).existsById(1);
        Mockito.verify(repository, Mockito.times(1)).deleteById(1);
    }


    @Test
    public void deleteNonExistingStudent() {
        Mockito.when(repository.existsById(1)).thenReturn(false);
        Assertions.assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(1));
        Mockito.verify(repository, Mockito.times(1)).existsById(1);
        Mockito.verify(repository, Mockito.never()).deleteById(1000);
    }

//    @Test
//    public void getStudent(){
//        Student student=new Student();
//        student.setId(1);
//        Mockito.when(repository.findById(student.getId())).thenReturn(Optional.of(student));
//
//        Mockito.when(restTemplate.getForObject(consumerURL, Student.class,student)).thenReturn(student);
//
//        String mappedStudent = modelMapper.map(student, String.class);
//        Mockito.doNothing().when(studentProducer).getStudentById("get-student",1,mappedStudent);
//
//        ResponseEntity<CustomResponseEntity<Student>> responseEntity = studentService.getStudent(student.getId());
//        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//        Mockito.verify(repository,Mockito.times(1)).findById(student.getId());
//        Mockito.verify(restTemplate,Mockito.times(1)).getForObject(consumerURL, Student.class,student);
//        Mockito.verify(studentProducer,Mockito.times(1)).getStudentById("get-student",1,mappedStudent);
//    }

    @Test
    public void getStudent1(){
        Student student=new Student();
        int id=1;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(student));
        ResponseEntity<CustomResponseEntity<Student>> result = studentService.getStudent(id);
        Mockito.verify(repository,Mockito.times(1)).findById(1);
        Assertions.assertEquals(HttpStatus.OK,result.getStatusCode());
    }

}