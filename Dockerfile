FROM amazoncorretto:11
WORKDIR /app
ADD gradle ./gradle/
COPY gradlew .
COPY build.gradle settings.gradle /app/
COPY gradle/ app/gradle
COPY src/ app/src
RUN ./gradlew bootJar

COPY build/libs/boosty-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

#FROM amazoncorretto:11
#WORKDIR /app
#COPY gradlew /app
#ADD gradle /app/gradle
#COPY build.gradle settings.gradle /app/
#ENTRYPOINT ["sh", "./gradlew", "bootJar"]
