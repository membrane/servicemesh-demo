FROM adoptopenjdk/openjdk13 AS build
WORKDIR /app
ADD gradlew /app/
ADD gradle /app/gradle/
RUN sed -i 's/\r//' gradlew && bash ./gradlew --version
ADD build.gradle /app
RUN bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes
ADD src /app/src/
RUN bash ./gradlew assemble || \
 bash ./gradlew assemble || \
 bash ./gradlew assemble

FROM adoptopenjdk/openjdk13:alpine-jre
COPY --from=build /app/build/libs/app*.jar /app/
CMD /bin/sh -c "exec java -jar /app/app*.jar"