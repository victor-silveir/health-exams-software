FROM openjdk:11

ADD target/*.jar health-software.jar

ENTRYPOINT [ "sh", "-c", "java -jar health-software.jar" ]