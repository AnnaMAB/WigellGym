package org.example.wigellgym.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers("/api/wigellgym/**").hasAnyRole("USER","ADMIN")
                                .anyRequest().authenticated()           //TODO------------------------------
                            );
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        UserDetails Anna = User
                .withUsername("19850101-1234")
                .password("{noop}1234")
                .roles("USER")
                .build();

        UserDetails Erik = User
                .withUsername("19900215-5678")
                .password("{noop}5678")
                .roles("USER")
                .build();

        UserDetails Maria = User
                .withUsername("19751230-9101")
                .password("{noop}9101")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, Anna, Erik, Maria);
    }


}
