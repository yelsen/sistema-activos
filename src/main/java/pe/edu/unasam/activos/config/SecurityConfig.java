package pe.edu.unasam.activos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.unasam.activos.security.CustomUserDetailsService;
import pe.edu.unasam.activos.security.JwtAuthenticationFilter;
import pe.edu.unasam.activos.security.handler.CustomAuthenticationFailureHandler;
import pe.edu.unasam.activos.security.handler.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final CustomUserDetailsService userDetailsService;
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final CustomAuthenticationFailureHandler authenticationFailureHandler;
        private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/login",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/libs/**",
                                                                "/fonts/**",
                                                                "/images/**")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/**")
                                .headers(headers -> headers
                                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                                .contentSecurityPolicy(csp -> csp.policyDirectives(
                                                "script-src 'self' 'unsafe-inline'; object-src 'none';"))
                                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true)
                                                .maxAgeInSeconds(31536000)))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .requestMatchers(
                                                                "/libs/**",
                                                                "/fonts/**",
                                                                "/images/**",
                                                                "/css/**",
                                                                "/js/**")
                                                .permitAll()
                                                .requestMatchers(
                                                                "/",
                                                                "/login**",
                                                                "/logout",
                                                                "/error/**",
                                                                "/uploads/**",
                                                                "/forgot-password")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(authenticationSuccessHandler)
                                                .failureHandler(authenticationFailureHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .authenticationProvider(authenticationProvider()); // ¡Esta es la línea clave!

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                provider.setHideUserNotFoundExceptions(false);
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
