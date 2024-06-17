package com.jsp.producer.repository;

import com.jsp.producer.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean findByName(String name);
    List<Student> findByCourse(String course);
}