package com.jsp.producer.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @InjectMocks
    StudentRepository repository;

    @Test
    public void findByNameTest(){
        repository.findByName("abv");
    }

}