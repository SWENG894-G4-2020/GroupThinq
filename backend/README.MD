There are a couple things you must do before running the back-end service:
  1. Make sure Java JDK is installed on your computer.
  2. Make sure JAVA_HOME and PATH variables are set
  
Once the above has been done, navigate to the backend folder and run `./gradlew clean build assemble` to create a .JAR file for execution.
Then navigate to build/libs, and run `java -jar <.jar name here>` to deploy.

Another option for deployment is to stay in the backend folder directory and run `./gradlew clean build; ./gradlew bootRun` and skip building the JAR.
