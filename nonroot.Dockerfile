FROM openjdk:21-rc-oracle as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
COPY ./src ./src
# Install Maven manually
RUN curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.mirror.digitalpacific.com.au/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.8.4 /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/local/bin \
    && rm -f /tmp/apache-maven.tar.gz

FROM openjdk:19-jdk-alpine3.16
RUN apk update && apk add curl
WORKDIR /app
# Set non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 9090
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]