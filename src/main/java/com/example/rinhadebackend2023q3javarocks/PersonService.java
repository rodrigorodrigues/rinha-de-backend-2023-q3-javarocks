package com.example.rinhadebackend2023q3javarocks;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Person findById(String id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not found person by id: %s", id)));
    }

    public List<Person> findAllByTerm(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matching("\"" + term + "\"");
        return personRepository.findAllBy(textCriteria);
    }

    public long total() {
        return personRepository.count();
    }
}
