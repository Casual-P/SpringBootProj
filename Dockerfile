FROM openjdk:11
ADD /target/SpringJWTauth-0.0.1-SNAPSHOT.jar myApp.jar
ENTRYPOINT ["java", "-jar", "myApp.jar"]