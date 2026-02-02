package com.api.gateway.config;
import com.api.gateway.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import java.util.List;


/*
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // ✅ Enable CORS
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/staff/**").hasRole("ADMIN") // adjust as needed
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(exchanges -> exchanges
                        // Publicly accessible endpoints
                        .pathMatchers("/auth/**", "/eureka/**", "/swagger-ui.html", "/swagger-ui/**",
                                "/v3/api-docs/**","/rooms/getAll","/guest/**","/api/payments/**","/api/reservations/**","/api/reservations/add","/api/payments/rate-test","/rooms/**","/rooms/getRoom/{roomType}","/rooms/get-price/{roomNumber}","/rooms/getPriceByRoomType","rooms/availability/{roomId}","/rooms/get/{roomNumber}", "/swagger-resources/**", "/webjars/**","/guest/add","/guest/update/{id}","/guest/get/memberCode/{memberCode}","/guest/get/id/{id}","/api/reservations//get/{id}","/api/reservations/room/{roomNumber}").permitAll()


                        .pathMatchers("/rooms/get/{roomNumber}","/api/payments/status/{bookingId}","/api/payments/create","/api/reservations/**").hasAnyRole("USER", "RECEPTIONIST","ADMIN")


                        .pathMatchers("rooms/availability/{roomId}","rooms/update").hasAnyRole("RECEPTIONIST","ADMIN")


                        .pathMatchers("/auth/**","/guest/**","/api/payments/**","/api/reservations/**","/rates/**","/rooms/**","/staff/**").hasRole("ADMIN")
                        // All others must be authenticated
                        .anyExchange()
                        .authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource())) // ✅ Apply CORS
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/login","/auth/register","/auth/request-otp","/auth/verify-otp","/auth/reset-password",
                                "/eureka/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/rooms/getAll","rooms/availability/{roomId}","/rooms/getRoom/{roomType}","/rooms/get-price/{roomNumber}","/rooms/getPriceByRoomType","/rooms/get/{roomNumber}","/rooms/available","rooms/available/byType",
                                "/guest/**", "/api/payments/**", "/api/reservations/**",
                                "/api/reservations/add", "/api/payments/rate-test", "/swagger-resources/**", "/webjars/**", "/guest/add",
                                "/guest/update/{id}", "/guest/get/memberCode/{memberCode}", "/guest/get/id/{id}",
                                "/api/reservations//get/{id}", "/api/reservations/room/{roomNumber}").permitAll()

                        .pathMatchers("/rooms/get/{roomNumber}", "/api/payments/status/{bookingId}",
                                "/api/payments/create", "/api/reservations/**").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")

                        .pathMatchers("rooms/availability/{roomId}", "/rooms/**","/rooms/delete/{roomNumber}").hasAnyRole("RECEPTIONIST", "ADMIN")

                        .pathMatchers("/auth/**", "/guest/**", "/api/payments/**", "/api/reservations/**",
                                "/rates/**", "/rooms/**", "/staff/**").hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}