FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
COPY /target/transfers-0.0.1-SNAPSHOT.jar transfers-0.0.1-SNAPSHOT.jar
#ADD wait-for-it.sh /usr/src/templates/
#ADD jdbc /usr/src/templates  , "-Dspring.profiles.active=container"
#WORKDIR /usr/src/templates
EXPOSE 8080
CMD ["java", "-jar", "transfers-0.0.1-SNAPSHOT.jar"]