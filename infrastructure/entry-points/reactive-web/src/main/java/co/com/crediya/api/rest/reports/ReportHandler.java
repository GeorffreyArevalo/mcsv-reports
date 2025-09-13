package co.com.crediya.api.rest.reports;

import co.com.crediya.api.mappers.ReportsMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.ReportsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReportHandler {

    private final ReportsUseCase reportsUseCase;
    private final ReportsMapper reportsMapper;

    public Mono<ServerResponse> listenGetReport(ServerRequest serverRequest) {

        String metric =  serverRequest.queryParam("metric").orElse("");

        return reportsUseCase.findByMetric(metric)
                .map(reportsMapper::modelToResponse)
                .flatMap( report ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(
                            ExceptionStatusCode.OK.status(), report
                        ))
                );
    }

}
