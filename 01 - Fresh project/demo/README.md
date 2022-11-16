# Fresh Project
## What has happened

- Dependencies: Look inside the POM File
- Bootstrap: Look into Demo-Application
- Spring Boot Annotations (https://www.baeldung.com/spring-boot-annotations)


## How to run?

Reference:
https://docs.spring.io/spring-boot/docs/2.7.5/maven-plugin/reference/htmlsingle/

./mvnw(.cmd) spring-boot:run

## What happens when run

- Look at Port
- Go to browser (Nothing there yet)

## Short Frontend Setup

lets add Thymeleaf (https://www.thymeleaf.org/)

POM.XML
```xml
	<dependency> 
    	<groupId>org.springframework.boot</groupId> 
    	<artifactId>spring-boot-starter-thymeleaf</artifactId> 
	</dependency>
```

application.properties

```properties
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true 
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

spring.application.name=Tutorial

logging.level.root=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR

```

lets add error.html

```html
<html lang="en">
<head><title>Error Occurred</title></head>
<body>
    <h1>Error Occurred!</h1>    
    <b>[<span th:text="${status}">status</span>]
        <span th:text="${error}">error</span>
    </b>
    <p th:text="${message}">message</p>
</body>
</html>
``` 

and home.html
```html
<html>

<head>
    <title>Home Page</title>
</head>

<body>
    <h1>Hello !</h1>
    <p>Welcome to <span th:text="${appName}">Our App</span></p>
</body>

</html>
``` 

Lets add a controller 
Lets talk a bit about Inversion of Controll and Dependency Injection (https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring)
What is now a @Controller? ( https://www.baeldung.com/spring-controllers)

```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {
    
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}
```


