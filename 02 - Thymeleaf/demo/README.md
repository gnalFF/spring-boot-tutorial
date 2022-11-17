# Thymeleaf Project

##Lets add security  
Reference: https://www.baeldung.com/spring-security-login

pom.xml

```xml
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-security</artifactId> 
</dependency>
```

Once This is added everything is secured, that is why we need to add a SecurityConfig

```java
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .csrf()
            .disable();
        return http.build();
    }
}
```
Lets add a PasswordEncoder
```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
```

Lets now add a UserDetailsManager

```java
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder().encode("user1Pass"))
                .roles("USER")
                .build();
        UserDetails user2 = User.withUsername("user2")
                .password(passwordEncoder().encode("user2Pass"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("adminPass"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, admin);
    }
```

Lets add 2 more routes for admin and anonym

anonymous
```html
<html>

<head>
    <title>Auth</title>
</head>

<body>
    <h1>Hello Authenticated</h1>
</body>

</html>
```

admin
```html
<html>

<head>
    <title>Home Admin</title>
</head>

<body>
    <h1>Hello Admin</h1>
</body>

</html>
```

Lets Change the FilterChain in SecurityConfig

```java
    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/", "/login*", "/api/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
        return http.build();
    }
```

lets add new routes to our simplecontroller

```java

    @GetMapping("/auth/home")
    public String anon(Model model) {
        model.addAttribute("appName", appName);
        return "auth";
    }

    @GetMapping("/admin/home")
    public String admin(Model model) {
        model.addAttribute("appName", appName);
        return "admin";
    }
```
