# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.0/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.0/reference/htmlsingle/#using-boot-devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.0/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.6.0/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [Spring LDAP](https://docs.spring.io/spring-boot/docs/2.6.0/reference/htmlsingle/#boot-features-ldap)

### Guides
The following guides illustrate how to use some features concretely:

* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

### Building the fat jar
--------------------
mvn clean package -Dmaven.test.skip=true

### Building the client jar
---------------------------
mvn clean jar:jar install -Dmaven.test.skip=true -Dspring-boot.repackage.skip=true

### Eureka and hawtio server 1
java -jar -Dserver.port=8080 -Dcache.api.port=8081 cacher-0.0.1-SNAPSHOT.jar

### Eureka and hawtio server 1
java -jar -Dserver.port=8082 -Dcache.api.port=8083 -Deureka.port=8080 cacher-0.0.1-SNAPSHOT.jar
