# Store microservice

## Local development

Start a PostgreSQL database in a Docker container. The database username, password and database name must mach the
configuration in `api/src/main/resources/config.yaml`.

```bash
docker run  -d --name pg-notifications
            -e POSTGRES_USER=dbuser
            -e POSTGRES_PASSWORD=postgres
            -e POSTGRES_DB=notifications
            -p 5432:5432
            postgres:13
```

Then compile the project using Maven and run the generated JAR file using the following commands.

```bash
mvn clean package
java -jar .\api\target\notification-catalog-api-1.0.0-SNAPSHOT.jar
```

Available at: [localhost:8080/v1/notifications](http://localhost:8080/v1/notifications)

## Docker

```bash
# Compile the project to jar compressed file and build a Docker image named notification-catalog.
docker build -t notification-catalog .

# Create a new network
docker network create --driver bridge price-comparison-network

# Run a postgres database in container named pg-notifications in defined network.
# Since we have defined both the container name and network, we can connect to this instance
# using the following connection string "jdbc:postgresql://pg-notifications:5432/notifications"
docker run  -d --name pg-notifications
            -e POSTGRES_USER=dbuser
            -e POSTGRES_PASSWORD=postgres
            -e POSTGRES_DB=notifications
            -p 5432:5432
            --network price-comparison-network
            postgres:13

# Run a Docker container from image named notification-catalog.
# Define database server, username and password as environment variables.
docker run  -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-notifications:5432/notifications
            -e KUMULUZEE_DATASOURCES0_USERNAME=dbuser
            -e KUMULUZEE_DATASOURCES0_PASSWORD=postgres
            --name notification-catalog
            -p 8080:8080
            --network price-comparison-network
            notification-catalog
```

We can use the [ElephantSQL](https://www.elephantsql.com/) PostgreSQL database as a Service.
