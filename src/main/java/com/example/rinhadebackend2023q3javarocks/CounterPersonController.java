package com.example.rinhadebackend2023q3javarocks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contagem-pessoas")
public class CounterPersonController {
    private final PersonService personService;

    public CounterPersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<Long> total() {
        return ResponseEntity.ok(personService.total());
    }
}
