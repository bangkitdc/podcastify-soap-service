FROM maven:3-amazoncorretto-8 as build

WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn clean install assembly:single

FROM amazoncorretto:8

WORKDIR /target

COPY --from=build /app/target .

EXPOSE 5555

CMD java -cp podcastify-soap-service-jar-with-dependencies.jar com.podcastify.main.Main