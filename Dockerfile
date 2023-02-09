### STAGE 1: Build ###
FROM maven:3-openjdk-17 AS builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install app dependencies
COPY pom.xml .
# RUN mvn -B dependency:resolve-plugins dependency:resolve
RUN echo Download Start : $(date +%F_%T)
RUN mvn --batch-mode \
    --quiet \
    --errors \
    dependency:resolve-plugins \
    dependency:resolve
RUN echo Download End : $(date +%F_%T)

# Bundle app source
COPY src src
# RUN mvn -B package
RUN mvn --batch-mode \
    --quiet \
    --errors \
    package -DskipTests

RUN echo BuildEnd : $(date +%F_%T)


### STAGE 2: Production Environment ###
FROM openjdk:17

#RUN addgroup -g 1001 -S spring
#RUN adduser -S boot -u 1001

ARG JAR_FILE=target/*.jar
COPY --from=builder --chown=boot:spring /usr/src/app/${JAR_FILE} ./app.jar

#USER boot

# BUILD ARGUMENTS
ARG VERSION
ENV VERSION $VERSION
ARG BUILD_TIMESTAMP
ENV BUILD_TIMESTAMP $BUILD_TIMESTAMP


ENV DB_URL mariadb:3306
ENV DB_USER skyscape
ENV DB_PASS skyscape!!
ENV JWT_SECRET jwt_secret_key!!@@
ENV PORT 8080
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app.jar"]