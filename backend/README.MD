# Backend Project (Java Spring Boot)
To run this through a Docker container, navigate to the `SWENG894` directory, then run the following command:
```
docker run --name backend_dev \
    -v ${PWD}\backend:/backend \
    -w /backend \
    -p 8080:8080 \
    --env-file ./.env \
gradle gradle clean build bootRun
```
Ensure your .env file has been copied from the example and is populated.
