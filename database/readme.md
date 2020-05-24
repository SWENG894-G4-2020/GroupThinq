# SWENG 894 G4: Groupthinq - Database

To run a development Postgres server, create the initial setup SQL statements in the setup.sql file found in the /scripts folder.

There is no need to build a new Docker image, the standard Postgres image will meet our current needs.

Then, run the following Docker command from the /database folder:

(NOTE: you must use Powershell if running on Windows for this command to succeed)

```
docker run --name postgres_dev -v ${PWD}\scripts:/docker-entrypoint-initdb.d -p 5432:5432 --env-file ../.env -d postgres:alpine
```

The above example will run a Postgres server on port 5432 containing a database after the setup statements have been run. The username and password are _postgres_ and _password_ respectively.