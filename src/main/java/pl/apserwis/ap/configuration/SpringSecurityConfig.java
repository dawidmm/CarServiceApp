package pl.apserwis.ap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager loginBean() {
        UserDetails user = User
                .withUsername("admin")
                .password("$2a$12$NyhSTr24igRMSm/Gqo/tsuu1x1T379P9fbVUiTc1h5vr5hiLk23mi")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
