package com.example.rinhadebackend2023q3javarocks;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
@ImportRuntimeHints(RinhaDeBackend2023Q3JavaRocksApplication.SerializationHintsRegistrar.class)
public class RinhaDeBackend2023Q3JavaRocksApplication {

    public static void main(String[] args) {
        SpringApplication.run(RinhaDeBackend2023Q3JavaRocksApplication.class, args);
    }

    static class SerializationHintsRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.serialization().registerType(Person.class);
        }
    }
}
