package com.example.rinhadebackend2023q3javarocks;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findAllBy(TextCriteria criteria);
}
