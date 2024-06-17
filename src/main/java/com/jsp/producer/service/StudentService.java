package com.jsp.producer.service;

import com.jsp.producer.exceptions.StudentAlreadyExistsException;
import com.jsp.producer.exceptions.StudentNotFoundException;
import com.jsp.producer.model.Student;
import com.jsp.producer.repository.StudentRepository;
import com.jsp.producer.responseEntity.CustomResponseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentProducer studentProducer;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    private final String consumerURL = "http://localhost:8081/consumer";

    public Student createStudent(Student student) {
        if (repository.existsById(student.getId())) {
            throw new StudentAlreadyExistsException("Student with ID " + student.getId() + " already exists.");
        }
        Student savedStudent = repository.save(student);
        restTemplate.postForObject(consumerURL, savedStudent, Student.class);
        studentProducer.addStudent("add-student", 0, modelMapper.map(savedStudent, String.class));

        return savedStudent;
    }

    public ResponseEntity<CustomResponseEntity<Student>> getStudent(int id) {
        Student student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with ID " + id + " not found."));
        restTemplate.getForObject(consumerURL, Student.class, student);
        studentProducer.getStudentById("get-student", 5, modelMapper.map(student, String.class));

        CustomResponseEntity<Student> response = CustomResponseEntity.<Student>builder()
                .message("Student retrieved successfully")
                .data(student)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity<Student>> updateStudent(int id, Student updatedStudent) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }
        updatedStudent.setId(id);
        Student savedStudent = repository.save(updatedStudent);
        studentProducer.updateStudent("update-student", 4, savedStudent);

        CustomResponseEntity<Student> response = CustomResponseEntity.<Student>builder()
                .message("Student updated successfully")
                .data(savedStudent)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void deleteStudent(int id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }
        repository.deleteById(id);
        studentProducer.deleteStudent("delete-student", 2, new Student(id));

        CustomResponseEntity<Void> response = CustomResponseEntity.<Void>builder()
                .message("Student deleted successfully")
                .data(null)
                .status(HttpStatus.OK)
                .build();

    }

    public ResponseEntity<CustomResponseEntity<List<Student>>> getAllStudents() {
        List<Student> students = repository.findAll();
        students.forEach(student -> studentProducer.getStudent("get-student", 1, student.toString()));

        CustomResponseEntity<List<Student>> response = CustomResponseEntity.<List<Student>>builder()
                .message("Students retrieved successfully")
                .data(students)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public boolean findByName(String name){
        return repository.findByName(name);
    }

    public List<Student> findByCourse(String course){
        return repository.findByCourse(course);
    }

//    public ResponseEntity<Student> getStudentByID(int id) {
//        Optional<Student> byId = repository.findById(id);
//        return byId.map(student -> ResponseEntity.ok().body(student)).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    public Student getStudentByID(int id) {
        Optional<Student> byId = repository.findById(id);
        if (byId.isEmpty()) {
            throw new StudentNotFoundException("Student Not Found");
        }
        return byId.get();
    }

}
//throw new StudentNotFoundException("Student Not Found");