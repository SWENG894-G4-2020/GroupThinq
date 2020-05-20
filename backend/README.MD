# Backend Project (Java Spring Boot)
To run this through a Docker container, navigate to the `SWENG894` directory, then run
```
docker run --name backend_dev -v ${PWD}\backend:/backend -w /backend -p 8080:8080 gradle gradle clean build bootRun
```
