build:
	mvn complile

unit-test:
	@echo "executando testes unitários"
	@mvn test

integration-test:
	mvn test -P integration-test

system-test:
	mvn test -P system-test

performance-test:
	mvn gatling:test -P performance-test

test: unit-test integration-test

start-app:
	mvn spring-boot:start

package:
	mvn package

docker-build: package
	docker build -t backend:dev -f ./Dockerfile

docker-start:
	docker compose -f docker-compose.yaml up -d

docker-stop:
	docker compose -f docker-compose down


