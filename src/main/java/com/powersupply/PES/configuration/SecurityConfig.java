package com.powersupply.PES.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable() // UI쪽 들어오는거 disable
                .csrf().disable() // cross site 기능
                .cors().configurationSource(corsConfigurationSource()).and() // cross site 도메인 다른 경우 허용
                .authorizeRequests()
                .antMatchers("/css/**","/js/**").permitAll() // 정적 메소드 누구나 접근 가능
                .antMatchers("/signin","/signup","/finduser").permitAll() // 누구나 접근 가능
                .antMatchers("/api/signin","/api/signup","/api/finduser","/api/rank","/api/problemlist/**","/api/problem/**").permitAll() // 기본 요청 언제나 접근 가능
                .antMatchers(HttpMethod.GET, "/api/comment/**").permitAll()
                .antMatchers("/api/submit/**","/api/answer/**").hasRole("NEW_STUDENT")
                .antMatchers("/api/comment/**","/api/commentlist/**").hasRole("REGULAR_STUDENT")
                .antMatchers(HttpMethod.GET, "/api/questions/**").hasRole("REGULAR_STUDENT")
                .antMatchers("/api/manage/**","/api/questions/**").hasRole("MANAGER")
                .anyRequest().hasRole("USER")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new HeaderLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));  // 모든 도메인 허용
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS"));  // POST, GET, OPTIONS 메서드 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        //roleHierarchy.setHierarchy("ROLE_MANAGER > ROLE_REGULAR_STUDENT > ROLE_USER and ROLE_NEW_STUDENT > ROLE_USER");
        String hierarchy =  "ROLE_MANAGER > ROLE_REGULAR_STUDENT > ROLE_USER\n" +
                "ROLE_NEW_STUDENT > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
