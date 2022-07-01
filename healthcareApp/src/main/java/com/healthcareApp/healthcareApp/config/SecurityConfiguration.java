package com.healthcareApp.healthcareApp.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationConfiguration configuration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter customAuthFilter = new AuthenticationFilter(authenticationManager(configuration));
        customAuthFilter.setFilterProcessesUrl("/token");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()));
        http.authorizeRequests()
                .antMatchers("/token").permitAll()
                .antMatchers("/doctor/reg").permitAll()
                .antMatchers( "/doctor/register").permitAll()
                .antMatchers( "/doctor/diagnosis").permitAll()
                .antMatchers( "/doctor/sendDiagnosis").permitAll()
                .antMatchers( "/patient/register").permitAll()
                .antMatchers( "/patient/register").permitAll()
                .antMatchers( "/patient/selectDoctor").permitAll()
                .antMatchers( "/patient/sendMail").permitAll()
                //.antMatchers( "/api/patient/**").hasAnyAuthority("ROLE_PATIENT")
                .anyRequest().authenticated();
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new JwtOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
