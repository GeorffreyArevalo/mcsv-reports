package co.com.crediya.api.rest.reports;

import co.com.crediya.api.config.PathsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ReportRouterRest {

    private final PathsConfig pathsConfig;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ReportHandler handler) {
        return route(GET(pathsConfig.getReports()), handler::listenGetReport);
    }
}
