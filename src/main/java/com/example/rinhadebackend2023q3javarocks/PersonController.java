package com.example.rinhadebackend2023q3javarocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pessoas")
public class PersonController {
    private final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Person> create(@Validated @RequestBody Person person) {
        person.setId(UUID.randomUUID().toString());
        log.debug("Creating person: {}={}", Thread.currentThread(), person);
        person = personService.create(person);
        return ResponseEntity.created(URI.create("/pessoas/"+person.getId())).body(person);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Person> findById(@PathVariable String id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Person>> findAll(@RequestParam(name = "t") String term) {
        log.debug("FindAll by term: {}={}", Thread.currentThread(), term);
        if (!StringUtils.hasText(term)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(personService.findAllByTerm(term));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Map<String, String>> handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseEntity.unprocessableEntity()
                .body(Collections.singletonMap("error", "Nickname already created: " + e.getLocalizedMessage()));
    }
}
