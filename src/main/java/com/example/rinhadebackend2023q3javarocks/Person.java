package com.example.rinhadebackend2023q3javarocks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Document(collection = "pessoas")
public record Person (
        @MongoId(targetType = FieldType.STRING)
        @TextIndexed
        String id,

        @TextIndexed
        @NotBlank @Size(max = 32)
        @Indexed(unique = true)
        String apelido,

        @TextIndexed
        @NotBlank @Size(max = 100)
        String nome,

        @TextIndexed
        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
        String nascimento,

        @TextIndexed
        List<String> stack
) implements Serializable {}
