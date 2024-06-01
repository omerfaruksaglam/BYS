package com.baskent.Nursapp.Auth.config;


import com.baskent.Nursapp.DataAccessLayer.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfig{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtAuthenticationFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder auth, CustomAuthenticationProvider customAuthProvider) throws Exception{
        auth.authenticationProvider(customAuthProvider);
        return http
                .cors() // CORS yapılandırmasını etkinleştir
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/nursapp/auth/getbolmfacuni","/nursapp/auth/authenticate").permitAll()
                )
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/nursapp/auth/register").authenticated()
                )
                .authorizeHttpRequests(authh -> authh
                        // Rol bazında yetkilendirmeler
                        .requestMatchers("/nursapp/app/student/**").hasAuthority("STUDENT")
                        .requestMatchers("/nursapp/app/teacher/**").hasAuthority("TEACHER")
                        .requestMatchers("/nursapp/app/secondgrader/**").hasAuthority("SECOND_GRADER")
                        .requestMatchers("/nursapp/app/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/nursapp/app/report/**").hasAnyAuthority("STUDENT", "TEACHER", "SECOND_GRADER", "ADMIN")
                        // Diğer tüm istekler için kimlik doğrulama gerektir
                        .anyRequest().authenticated()
                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserRepository userRepository) {
        return new CustomAuthenticationProvider(userRepository, passwordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;


        /*
        * CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
        * */
    }

}
