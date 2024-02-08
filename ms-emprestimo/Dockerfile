FROM openjdk:11

LABEL authors="dsous"

VOLUME /tmp

RUN apt-get update && apt-get install -y wget

COPY ./target/ms-emprestimo-1.0.0-SNAPSHOT.jar ms-emprestimo.jar

CMD ["java","-jar","/ms-emprestimo.jar"]
