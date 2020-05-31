# SWENG 894 G4: Groupthinq - Frontend

To run the development server, first build the docker image (from this folder):

(NOTE: Expect some deprecation warnings on build)

```
docker build -t frontend:dev .
```

Then, run the image with the frontend application as a volume in the container, on the port of your choice:

(NOTE: you must use Powershell if running on Windows for this command to succeed)

```
docker run -v ${PWD}/groupthinq_frontend:/frontend/groupthinq_frontend -v -p 8081:8081 --rm frontend:dev
```

The above example will run the frontend application on localhost:8081.

The Vue server supports *hot reloading*, so there is no need to reload the docker container if making changes to the sourcecode. Changes will be reflected in the application within a few seconds of saving the file.