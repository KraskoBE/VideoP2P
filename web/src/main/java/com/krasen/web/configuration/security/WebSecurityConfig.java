package com.krasen.web.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class WebSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public WebSecurityConfig( AuthEntryPointJwt unauthorizedHandler, AuthTokenFilter authTokenFilter ) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.cors().and().csrf().disable();

        http.exceptionHandling()
            .authenticationEntryPoint( unauthorizedHandler )
            .and()
            .sessionManagement()
            .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
            .and()
            .authorizeRequests()
            .antMatchers( "/api/auth/**" )
            .permitAll()
            .antMatchers( "/api/**" )
            .authenticated()
            .antMatchers( "/*", "/assests/**" )
            .permitAll()
            .anyRequest()
            .denyAll();

        return http.addFilterBefore( authTokenFilter, UsernamePasswordAuthenticationFilter.class ).build();
    }

}
