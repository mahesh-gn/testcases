package com.jsp.producer.controller;

import com.jsp.producer.model.Student;
import com.jsp.producer.responseEntity.CustomResponseEntity;
import com.jsp.producer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseEntity<Student>> getStudent(@PathVariable int id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/byId")
    public ResponseEntity<Student> getStudentByID(@RequestParam int id) {
        Student studentByID = studentService.getStudentByID(id);
        if (studentByID==null)return ResponseEntity.notFound().build();
        return ResponseEntity.ok(studentByID);
    }

//    @GetMapping("/byId")
//    public ResponseEntity<Student> getStudentByID(@RequestParam int id) {
//        Student student = studentService.getStudentByID(id);
//        return ResponseEntity.ok().body(student);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponseEntity<Student>> updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
        return studentService.updateStudent(id, updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/hh")
    public ResponseEntity<CustomResponseEntity<List<Student>>> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/gg")
    public boolean findByName(@RequestParam String name){
        return studentService.findByName(name);
    }

}