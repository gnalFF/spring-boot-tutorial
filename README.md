# spring-boot-tutorial

## Spring vs Spring Boot

Find reference here: https://www.baeldung.com/spring-vs-spring-boot


### What is Spring
Simply put, the Spring framework provides comprehensive infrastructure support for developing Java applications.

It's packed with some nice features like Dependency Injection, and out of the box modules like:

- Spring JDBC
- Spring MVC
- Spring Security
- Spring AOP
- Spring ORM
- Spring Test

These modules can drastically reduce the development time of an application.

### What is Spring Boot
Spring Boot is basically an extension of the Spring framework, which eliminates the boilerplate configurations required for setting up a Spring application.
It takes an opinionated view of the Spring platform, which paves the way for a faster and more efficient development ecosystem.

Here are just a few of the features in Spring Boot:

- Opinionated ‘starter' dependencies to simplify the build and application configuration
- Embedded server to avoid complexity in application deployment
- Metrics, Health check, and externalized configuration
- Automatic config for Spring functionality – whenever possible
- Let's get familiar with both of these frameworks step by step.


This is how you would setup a webapp in Spring

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>5.3.5</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.5</version>
</dependency>
```

This is Spring Boot, where all dependencies are add to the archive during build time

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.4.4</version>
</dependency>
```

Another good example is testing libraries. We usually use the set of 
-Spring Test, 
-JUnit, Hamcrest, and 
-Mockito libraries. 
In a Spring project, we should add all of these libraries as dependencies.

Alternatively, in Spring Boot we only need the starter dependency for testing to automatically include these libraries. (https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions.properties)

Spring Boot provides a number of starter dependencies for different Spring modules. Some of the most commonly used ones are:

- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-test
- spring-boot-starter-web
- spring-boot-starter-thymeleaf

For the full list of starters, also check out the Spring documentation.

### Bootstrapping
The basic difference in bootstrapping an application in Spring and Spring Boot lies with the servlet. Spring uses either the web.xml or SpringServletContainerInitializer as its bootstrap entry point.
On the other hand, Spring Boot uses only Servlet 3 features to bootstrap an application. Let's talk about this in detail.

#### How Spring bootstraps?
Spring supports both the legacy web.xml way of bootstrapping as well as the latest Servlet 3+ method.

#####Let's see the web.xml approach in steps:

1. Servlet container (the server) reads web.xml.
2. The DispatcherServlet defined in the web.xml is instantiated by the container.
3. DispatcherServlet creates WebApplicationContext by reading WEB-INF/{servletName}-servlet.xml.
4. Finally, the DispatcherServlet registers the beans defined in the application context.

#####Here's how Spring bootstraps using the Servlet 3+ approach:

1. The container searches for classes implementing ServletContainerInitializer and executes.
2. The SpringServletContainerInitializer finds all classes implementing WebApplicationInitializer.
3. The WebApplicationInitializer creates the context with XML or @Configuration classes.
4. The WebApplicationInitializer creates the DispatcherServlet with the previously created context.

#### How Spring Boot bootstraps

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

By default, Spring Boot uses an embedded container to run the application. In this case, Spring Boot uses the public static void main entry point to launch an embedded web server.

It also takes care of the binding of the Servlet, Filter, and ServletContextInitializer beans from the application context to the embedded servlet container.

Another feature of Spring Boot is that it automatically scans all the classes in the same package or sub packages of the Main-class for components.

Additionally, Spring Boot provides the option of deploying it as a web archive in an external container. In this case, we have to extend the SpringBootServletInitializer:

```java
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    // ...
}
```
Here the external servlet container looks for the Main-class defined in the META-INF file of the web archive, and the SpringBootServletInitializer will take care of binding the Servlet, Filter, and ServletContextInitializer.

### Packaging and Deployment

Finally, let's see how an application can be packaged and deployed. Both of these frameworks support common package managing technologies like Maven and Gradle; however, when it comes to deployment, these frameworks differ a lot.

For instance, the Spring Boot Maven Plugin provides Spring Boot support in Maven. It also allows packaging executable jar or war archives and running an application “in-place.”

Some of the advantages of Spring Boot over Spring in the context of deployment include:

- Provides embedded container support
- Provision to run the jars independently using the command java -jar
- Option to exclude dependencies to avoid potential jar conflicts when deploying in an external container
- Option to specify active profiles when deploying
- Random port generation for integration tests

### Initializing a Spring Boot app

https://start.spring.io/

Choose
- Build: Maven
- Language: Java
- Version: 2.75
- Packaging: Jar
- Java 17
- Dependencies
    - Spring Web
    - Spring Data JPA
