package com.jsp.producer.responseEntity;

import com.jsp.producer.model.Student;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomResponseEntity<T> {

    private String message;
    private T data;
    private HttpStatus status;

    public CustomResponseEntity(Student student){

    }

}