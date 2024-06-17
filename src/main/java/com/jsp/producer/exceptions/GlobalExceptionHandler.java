package com.jsp.producer.exceptions;

import com.jsp.producer.model.Student;
import com.jsp.producer.responseEntity.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(StudentNotFoundException.class)
//    public ResponseEntity<CustomResponseEntity<Student>> studentNotFoundException(StudentNotFoundException e) {
//        return new ResponseEntity<>(CustomResponseEntity.<Student>builder()
//                .message(e.getMessage())
//                .data(null)
//                .status(HttpStatus.NOT_FOUND)
//                .build(), HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<CustomResponseEntity<Student>> studentAlreadyExistsException(StudentAlreadyExistsException e) {
        return new ResponseEntity<>(CustomResponseEntity.<Student>builder()
                .message(e.getMessage())
                .data(null)
                .status(HttpStatus.CONFLICT)
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}