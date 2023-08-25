package com.example.rinhadebackend2023q3javarocks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Document(collection = "pessoas")
public class Person implements Serializable {
    @MongoId(targetType = FieldType.STRING)
    @TextIndexed
    private String id;

    @TextIndexed
    @NotBlank @Size(max = 32)
    @Indexed(unique = true)
    private String apelido;

    @TextIndexed
    @NotBlank @Size(max = 100)
    private String nome;

    @TextIndexed
    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String nascimento;

    @TextIndexed
    private List<String> stack;

    public Person() {}

    @PersistenceCreator
    public Person(String id, String apelido, String nome, String nascimento, List<String> stack) {
        this.id = id;
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }
}
