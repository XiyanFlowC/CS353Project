package org.cstp.meowtool;

import org.cstp.meowtool.filters.JwtAuthenticationFilter;
import org.cstp.meowtool.handlers.JwtAccessDiniedHandler;
import org.cstp.meowtool.handlers.JwtAuthenticationFailureEntryPoint;
// import org.cstp.meowtool.handlers.JwtLogoutSuccessHandler;
// import org.cstp.meowtool.handlers.LoginFailureHandler;
// import org.cstp.meowtool.handlers.LoginSuccessHandler;
// import org.cstp.meowtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalAuthentication
public class SecurityConfig {
    // @Autowired
    // LoginFailureHandler loginFailureHandler;

    // @Autowired
    // LoginSuccessHandler loginSuccessHandler;

    @Autowired
    JwtAuthenticationFailureEntryPoint jwtAuthenticationFailureEntryPoint;

    @Autowired
    JwtAccessDiniedHandler jwtAccessDiniedHandler;

    // @Autowired
    // UserService userService;

    // @Autowired
    // JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] URL_WHITELIST = {
        "/auth/login",
        "/auth/logout",
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
            .antMatchers(URL_WHITELIST).permitAll()
            .anyRequest().authenticated()

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationFailureEntryPoint)
            .accessDeniedHandler(jwtAccessDiniedHandler)

            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
