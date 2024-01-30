package kr.co.writenow.writenow.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kr.co.writenow.writenow.config.security.jwt.JwtAuthenticationFilter;
import kr.co.writenow.writenow.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtTokenProvider tokenProvider;

  @Value("${frontDomain}")
  private String FRONT_DOMAIN;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(config -> config.configurationSource(request -> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedHeaders(Collections.singletonList("*"));
          configuration.setAllowedMethods(Collections.singletonList("*"));
          configuration.setAllowedOriginPatterns(
              Collections.singletonList(FRONT_DOMAIN));
          configuration.setAllowCredentials(true);
          return configuration;
        }))
        .exceptionHandling(config -> config.accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint()))
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(authenticationFilter(tokenProvider), AnonymousAuthenticationFilter.class)
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/user/register", "/user/login", "/monitor/prometheus")
                .permitAll()
                .requestMatchers("/post/**").hasAnyRole("USER")
                .anyRequest().authenticated());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter authenticationFilter(JwtTokenProvider tokenProvider) {
    return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler(objectMapper());
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint(objectMapper());
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
