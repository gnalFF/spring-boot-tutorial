# Security Project

## Lets add actuators
Reference: https://www.baeldung.com/spring-boot-actuators

By default there is a simple in memory instance of a MeterRegistry added. You can also target Prometheus, Datadog and manymore.
This is usually done by adding the correct depenceny and maybe changing the MeterRegistry accordingly, when not possible through configuration

More about this here:
https://www.baeldung.com/spring-boot-self-hosted-monitoring

add the following to the pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
lets change the securityconfig
```java
.antMatchers("/", "/login*", "/api/**", "/actuator", "/actuator/**")
```
lets activate all actuators
application.properties
```properties
management.endpoints.web.exposure.include=*
```

##What kind of endpoints exist?

- /auditevents lists security audit-related events such as user login/logout. Also, we can filter by principal or type among other fields.
- /beans returns all available beans in our BeanFactory. Unlike /auditevents, it doesn't support filtering.
- /conditions, formerly known as /autoconfig, builds a report of conditions around autoconfiguration.
- /configprops allows us to fetch all @ConfigurationProperties beans.
- /env returns the current environment properties. Additionally, we can retrieve single properties.
- /flyway provides details about our Flyway database migrations.
- /health summarizes the health status of our application.
- /heapdump builds and returns a heap dump from the JVM used by our application.
- /info returns general information. It might be custom data, build information or details about the latest commit.
- /liquibase behaves like /flyway but for Liquibase.
- /logfile returns ordinary application logs.
- /loggers enables us to query and modify the logging level of our application.
- /metrics details metrics of our application. This might include generic metrics as well as custom ones.
- /prometheus returns metrics like the previous one, but formatted to work with a Prometheus server.
- /scheduledtasks provides details about every scheduled task within our application.
- /sessions lists HTTP sessions given we are using Spring Session.
- /shutdown performs a graceful shutdown of the application.
- /threaddump dumps the thread information of the underlying JVM.

##Lets add a simple Monitor

add this above the homePage method in SimpleController
```java
@Timed("controllers.homepage")
```

## Lets add a Gauge

In SimpleController add the following

```java
private final MeterRegistry meterRegistry;
    private final AtomicInteger gauge;

    public SimpleController(MeterRegistry meterRegistry){
        this.meterRegistry = meterRegistry;
        gauge =  meterRegistry.gauge("controller.homepage.gauge", Arrays.asList(), new AtomicInteger());
    } 
```

To the homepage method add then
```java
gauge.set(new Random().nextInt(10));
```
