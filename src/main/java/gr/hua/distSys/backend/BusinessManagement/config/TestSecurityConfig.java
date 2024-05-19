package gr.hua.distSys.backend.BusinessManagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity in testing
            .authorizeRequests()
                .antMatchers("/api/auth/signup", "/api/auth/signin").permitAll() // Allow access to the specific endpoints
                .anyRequest().authenticated(); // Secure other endpoints
    }
}