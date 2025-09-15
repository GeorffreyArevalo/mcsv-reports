package co.com.crediya.security.config;

import co.com.crediya.security.exceptions.handlers.AccessDeniedExceptionHandler;
import co.com.crediya.security.exceptions.handlers.UnauthorizedExceptionHandler;
import co.com.crediya.security.util.KeysUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeysUtil keysUtil;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final UnauthorizedExceptionHandler unauthorizedExceptionHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {


        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange( exchange ->
                        exchange
                                .pathMatchers(
                                        "/reports/openapi/**"
                                ).permitAll()
                                .pathMatchers("/reports/actuator/health").permitAll()
                                .pathMatchers( HttpMethod.GET, "/reports/api/v1/reports" ).hasAnyAuthority("SCOPE_ADVISER", "SCOPE_ADMIN")
                                .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .oauth2ResourceServer( oauth2 ->
                        oauth2.jwt( jwt -> jwt.publicKey( keysUtil.loadPublicKey() ) )
                )
                .exceptionHandling( exceptionHandlingSpec ->
                        exceptionHandlingSpec
                                .authenticationEntryPoint(unauthorizedExceptionHandler)
                                .accessDeniedHandler(accessDeniedExceptionHandler)
                ).build();

    }



}
