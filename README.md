# Spring Batch on Lattice
This repository consists of a POC for the deployment of Spring Batch jobs packaged as 
Docker images using Lattice's Task functionality.

# Configuration of the Batch Job
This Spring Batch job simply sends an email from the job.  It's an easy way to illustrate
that the job has executed.  To get it to run, you'll need to configure the 
`JavaMailSender` via the following properties in an application.properties in 
`job/src/main/resources`.  The sample below illustreates how to use GMail's SMTP server:

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=someusername
spring.mail.password=somepassword
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## References
* [Spring Batch](https://spring.io/projects/spring-batch)
* [Docker](https://www.docker.com)
* [Lattice](http://lattice.cf)
* [Spring Boot](https://spring.io/projects/spring-boot)