package com.example.rinhadebackend2023q3javarocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class RinhaDeBackend2023Q3JavaRocksApplicationTests {

    @Container
    @ServiceConnection
    static MongoDBContainer postgresContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @DisplayName("Should create a person")
    @Test
    void testShouldCreatePerson() throws Exception {
        Person request = new Person(null, "test", "test", "2000-01-01", Arrays.asList("Java", "Spring Boot"));
        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @DisplayName("Should fail to create a person")
    @Test
    void testShouldFailToCreatePerson() throws Exception {
        Person request = new Person(null, null, "test", "2000-01-01", Arrays.asList("Java", "Spring Boot"));

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Should search a person by id")
    @Test
    void testShouldSearchPersonById() throws Exception {
        Person request = new Person(null, "test", "test", "2000-01-01", Arrays.asList("Java", "Spring Boot"));
        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isNotEmpty();

        Person person = objectMapper.readValue(response, Person.class);

        mockMvc.perform(get("/pessoas/"+person.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stack", hasSize(2)));

        mockMvc.perform(get("/pessoas/"+ UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should search by term")
    @Test
    void testShouldSearchPersonByTerm() throws Exception {
        Person request = new Person(null, "test 1", "test 1", "2000-01-01", Arrays.asList("Java", "Spring Boot"));
        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());

        request = new Person(null, "test 2", "test 2", "2000-01-01", Arrays.asList("Java", "Spring Data"));

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(get("/pessoas?t=Java"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", hasSize(2)));

        mockMvc.perform(get("/pessoas?t=test 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", hasSize(1)));

        mockMvc.perform(get("/pessoas?t=Python"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/pessoas?t="))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
