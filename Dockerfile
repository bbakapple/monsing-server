FROM openjdk:17

ARG MODULE
ARG JAR_FILE=${MODULE}/build/libs/${MODULE}.jar

COPY ${JAR_FILE} app.jar

ENV PROFILE=${PROFILE}
ENV JASYPT=${JASYPT}

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Djasypt.encryptor.password=${JASYPT}", "app.jar"]
