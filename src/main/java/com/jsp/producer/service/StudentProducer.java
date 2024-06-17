package com.jsp.producer.service;

import com.jsp.producer.model.Student;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ModelMapper modelMapper;

    public void addStudent(String topic, int partition, String student) {
        kafkaTemplate.send(topic, String.valueOf(partition), modelMapper.map(student, String.class));
    }

    public void deleteStudent(String topic, int partition, Student student) {
        kafkaTemplate.send(topic, String.valueOf(partition), modelMapper.map(student, String.class));
    }

    public void updateStudent(String topic, int partition, Student student) {
        kafkaTemplate.send(topic, String.valueOf(partition), modelMapper.map(student, String.class));
    }

    public void getStudent(String topic, int partition, String student) {
        kafkaTemplate.send(topic, String.valueOf(partition), modelMapper.map(student, String.class));
    }
    public void getStudentById(String topic, int partition, String student) {
        kafkaTemplate.send(topic, String.valueOf(partition), modelMapper.map(student, String.class));
    }
}
