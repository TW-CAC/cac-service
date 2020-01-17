.DEFAULT_GOAL:=start
  
DOCKER_COMPOSE:=docker-compose -f docker-compose.yml

.PHONY: build run start stop update update-proxy multitail

build:
	$(DOCKER_COMPOSE) build --pull

run:
	$(DOCKER_COMPOSE) up

start:
	$(DOCKER_COMPOSE) up -d

restart:
	$(DOCKER_COMPOSE) restart

stop:
	$(DOCKER_COMPOSE) down


logs:
	$(if $(SERVICE_NAME), $(info -- Tailing logs for $(SERVICE_NAME)), $(info -- Tailing all logs, SERVICE_NAME not set.))
	$(DOCKER_COMPOSE) logs -f $(SERVICE_NAME)
