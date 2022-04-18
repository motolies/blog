### STAGE 1: Build ###
FROM docker.hvy.kr/maven:3-openjdk-17 AS builder
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
    package

RUN echo BuildEnd : $(date +%F_%T)


### STAGE 2: Production Environment ###
FROM amazoncorretto:17-alpine-jdk

#RUN addgroup -g 1001 -S spring
#RUN adduser -S boot -u 1001

ARG JAR_FILE=target/*.jar
COPY --from=builder --chown=boot:spring /usr/src/app/${JAR_FILE} ./app.jar

#USER boot

ENV DB_URL mariadb:3306
ENV UPLOAD_PATH /skyscape/file
ENV PORT 8080
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app.jar"]