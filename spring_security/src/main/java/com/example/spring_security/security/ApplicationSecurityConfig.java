package com.example.spring_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.spring_security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
//                .antMatchers(POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINNE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe() //defaults to 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("verySecured")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // this line for because we disable CSRF
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
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
        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("12345"))
//                .roles(ADMIN.name()) //ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        // tom has only authorities to read
        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("12345"))
//                .roles(ADMINTRAINNE.name()) //ROLE_ADMINTRAINNE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                zahidUser,
                lindaUser,
                tomUser
        );
    }
}
