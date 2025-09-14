package co.com.crediya.api.rest.reports;

import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.dtos.reports.ApprovedReportResponseDTO;
import co.com.crediya.api.mappers.ReportsMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.ReportsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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



    @Operation( tags = "Reports", operationId = "getReport", description = "Get a report of loans", summary = "Get a report of loans",
            parameters = {
                    @Parameter( in = ParameterIn.QUERY, name = "type", description = "Type of report", required = true, example = "count_approved_reports" ),
                    @Parameter( in = ParameterIn.HEADER, name = "Authorization", description = "Bearer token", required = true, example = "mkasjdlkjas782347812" ),
            },
            responses = { @ApiResponse( responseCode = "200", description = "Get report successfully.", content = @Content( array = @ArraySchema( schema = @Schema( implementation = ApprovedReportResponseDTO.class ) ) ) ),
                    @ApiResponse( responseCode = "401", description = "Unauthorized.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "403", description = "Access Denied.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenGetReport(ServerRequest serverRequest) {

        String type =  serverRequest.queryParam("type").orElse("");

        return reportsUseCase.findByMetric(type)
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
