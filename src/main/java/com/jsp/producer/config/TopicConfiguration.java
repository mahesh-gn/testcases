package com.jsp.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic addStudent() {
        return TopicBuilder.name("add-student").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic deleteStudent() {
        return TopicBuilder.name("delete-student").partitions(2).build();
    }

    @Bean
    public NewTopic getStudent() {
        return TopicBuilder.name("get-student").partitions(5).build();
    }

    @Bean
    public NewTopic updateStudent() {
        return TopicBuilder.name("update-student").partitions(4).build();
    }

}