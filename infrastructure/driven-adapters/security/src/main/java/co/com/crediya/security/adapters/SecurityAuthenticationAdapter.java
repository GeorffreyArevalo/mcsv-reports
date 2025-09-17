package co.com.crediya.security.adapters;

import co.com.crediya.ports.SecurityAuthenticationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SecurityAuthenticationAdapter implements SecurityAuthenticationPort {

    @Override
    public Mono<String> getSubjectToken() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .filter( JwtAuthenticationToken.class::isInstance )
            .map( JwtAuthenticationToken.class::cast )
            .map( jwtAuthenticationToken -> {
                log.info( "Getting authentication token {}", jwtAuthenticationToken.getToken() );
                return jwtAuthenticationToken.getToken();
            }).map( JwtClaimAccessor::getSubject );
    }


}
