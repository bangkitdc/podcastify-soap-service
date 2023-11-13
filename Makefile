start:
	docker compose up -d --build

stop:
	docker compose down

stop-clean:
	docker compose down
	if exist mysql rmdir /s /q mysql

build:
	docker build -t podcastify-soap-service-app .