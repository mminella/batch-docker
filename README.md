# Spring Batch on Lattice
This repository consists of a POC for the deployment of Spring Batch jobs packaged as 
Docker images using Lattice's Task functionality.

To run the demo:

1. Get Lattice up and running locally via the instructions here: [Lattice Getting Started](http://lattice.cf/docs/getting-started/)
2. Get a Docker Hub account and create a repository called <username>/batch
2. Update the following configurations:
    1. In `build.gradle` update the group (line 61) to match your username in Docker Hub.
    2. Create an `application.properties` file in `job/src/main/resources` and configure 
    the following:  
    ```
	spring.mail.host=smtp.gmail.com
	spring.mail.port=587
	spring.mail.username=someusername
	spring.mail.password=somepassword
	spring.mail.properties.mail.smtp.auth=true
	spring.mail.properties.mail.smtp.starttls.enable=true
	job.email.to=emailToSendTheMessageTo
	```
3. Perform a `$ ./gradlew clean build buildDocker` from the root of this project.  This
   will build all modules and push the docker image for the job to Docker Hub.
4. Launch the orchestration web application via `$ java -jar orchestration/build/libs/orchestration-0.1.0.BUILD-SNAPSHOT.jar`
5. In a browser, navigate to http://localhost:8080
6. In the search field, enter your username and click search.
7. Select the 0.1.0-BUILD-SNAPSHOT tag of the <username>/batch repository and click launch.
8. The orchestration app will launch the task on lattice and you will receive the  email.  
   The status page you are taken to will refresh every 5 seconds displaying the status of 
   the task.

## References
* [Spring Batch](https://spring.io/projects/spring-batch)
* [Docker](https://www.docker.com)
* [Lattice](http://lattice.cf)
* [Spring Boot](https://spring.io/projects/spring-boot)