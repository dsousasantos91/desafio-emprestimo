#!/bin/bash

mvn clean install -DskipTests -f ms-emprestimo
mvn clean install -DskipTests -f ms-pagamento

docker image rm -f dsousasantos91/ms-emprestimo:1.0.0 dsousasantos91/ms-pagamento:1.0.0
docker build -t dsousasantos91/ms-emprestimo:1.0.0 ms-emprestimo
docker build -t dsousasantos91/ms-pagamento:1.0.0 ms-pagamento

docker container rm -f emprestimodb pgadmin4 emprestimo_amqp ms-emprestimo ms-pagamento
docker-compose -f docker-compose.yml up -d