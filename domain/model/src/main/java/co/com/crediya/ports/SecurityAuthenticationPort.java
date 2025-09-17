package co.com.crediya.ports;

import reactor.core.publisher.Mono;

public interface SecurityAuthenticationPort {

    Mono<String> getSubjectToken();

}
