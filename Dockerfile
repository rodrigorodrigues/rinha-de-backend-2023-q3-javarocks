FROM container-registry.oracle.com/graalvm/native-image-ee:latest as graalvm

RUN microdnf -y install wget unzip zip findutils tar

COPY . /home/app
WORKDIR /home/app

RUN \
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash; \
    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
    sdk list java; \
    sdk install java 20.0.2-graalce; \
    sdk use java 20.0.2-graalce; \
    java -version;  \
    echo "JAVA_HOME=${JAVA_HOME}";  \
    ## Run Gradle \
    ./gradlew nativeCompile -PskipTests

FROM ubuntu

EXPOSE 8080

# # For Gradle build
COPY --from=graalvm /home/app/build/native/nativeCompile/rinha-de-backend-2023-q3-javarocks rinha-de-backend-2023-q3-javarocks

ENTRYPOINT ["/rinha-de-backend-2023-q3-javarocks"]
