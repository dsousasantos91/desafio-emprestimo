FROM openjdk:11

LABEL authors="dsous"

VOLUME /tmp

RUN apt-get update && apt-get install -y wget

COPY ./target/ms-pagamento-1.0.0-SNAPSHOT.jar ms-pagamento.jar

CMD ["java","-jar","/ms-pagamento.jar"]

