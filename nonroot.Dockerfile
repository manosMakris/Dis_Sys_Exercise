FROM openjdk:21-rc-oracle as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN apt install maven -y
RUN apt-get update && apt-get install -y maven

FROM openjdk:19-jdk-alpine3.16
RUN apk update && apk add curl
WORKDIR /app
# Set non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 9090
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]