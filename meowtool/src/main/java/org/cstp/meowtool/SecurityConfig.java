package org.cstp.meowtool;

import org.cstp.meowtool.filters.JwtAuthenticationFilter;
import org.cstp.meowtool.handlers.JwtAccessDiniedHandler;
import org.cstp.meowtool.handlers.JwtAuthenticationFailureEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFailureEntryPoint jwtAuthenticationFailureEntryPoint;

    @Autowired
    JwtAccessDiniedHandler jwtAccessDiniedHandler;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] URL_WHITELIST = {
        "/auth/**",
        "/user/register",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
            // using standard controllers for this, disable default pages.
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()
            
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/user/user").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/user/user").hasRole("ADMIN")
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/proj/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/term/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().permitAll()
            //.antMatchers(URL_WHITELIST).permitAll()
            //.anyRequest().authenticated()

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationFailureEntryPoint)
            .accessDeniedHandler(jwtAccessDiniedHandler)

            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
