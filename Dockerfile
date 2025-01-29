FROM eclipse-temurin:17.0.12_7-jre
WORKDIR /app
VOLUME /tmp
COPY ./build/libs/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]