# SPRING SECURITY CONFIG - COPY PASTE SOLUTION

## QUICK FIX - SecurityConfig.java

Tạo hoặc update file SecurityConfig.java trong Spring Boot project:

**Location:** src/main/java/com/yourpackage/config/SecurityConfig.java

**COPY PASTE code này:**

```java
package com.yourpackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## application.properties

```properties
server.address=0.0.0.0
server.port=8080
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

## Restart Server

```bash
./mvnw spring-boot:run
```

## Test

```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"test","email":"test@test.com","password":"pass123","fullName":"Test"}'
```

Expected: HTTP 200 with JWT token
