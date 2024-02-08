#!/bin/bash

docker container rm $(docker ps -a -q) -f

docker run -dit -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=pg_client -p 5432:5432 --network emprestimo-network --name postgresql_client postgres:12-alpine
docker run -dit -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=pg_business -p 5433:5432 --network emprestimo-network --name postgresql_business postgres:12-alpine
docker run -dit -e RABBITMQ_ERLANG_COOKIE=secret_pass -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 5672:5672 -p 15672:15672 --network emprestimo-network --name rabbitmq rabbitmq:3.8.3-management 
docker run -dit -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=root -p 5050:80 --network emprestimo-network --name pgadmin4 dpage/pgadmin4
docker run -dit -e SPRING_PROFILES_ACTIVE=prod -P --network emprestimo-network --name ms-emprestimo dsousasantos91/ms-emprestimo:1.0.0
docker run -dit -e SPRING_PROFILES_ACTIVE=prod -P --network emprestimo-network --name ms-pagamento dsousasantos91/ms-pagamento:1.0.0