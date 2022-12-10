package com.example.spring_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.spring_security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.spring_security.security.ApplicationUserRole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINNE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {

        // zahid has no authorities to read write
        UserDetails zahidUser = User.builder()
                .username("zahid")
                .password(passwordEncoder.encode("12345"))
//                .roles(STUDENT.name()) //ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        // hasan has authorities to read, write, put and delete
        UserDetails hasanUser = User.builder()
                .username("hasan")
                .password(passwordEncoder.encode("12345"))
//                .roles(ADMIN.name()) //ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        // tom has only authorities to read
        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("12345"))
//                .roles(ADMINTRAINNE.name()) //ROLE_ADMINTRAINNE
                .authorities(ADMINTRAINNE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                zahidUser,
                hasanUser,
                tomUser
        );
    }
}
