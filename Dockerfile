FROM openjdk:22-jdk

WORKDIR /app

COPY target/micronaut-1-0.1.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# Run:
#   'docker build -t ivangorbunovv/micronaut-1-image .'
