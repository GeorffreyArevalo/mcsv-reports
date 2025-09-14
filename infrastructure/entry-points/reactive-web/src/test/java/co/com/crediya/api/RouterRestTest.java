package co.com.crediya.api;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.reports.ApprovedReportResponseDTO;
import co.com.crediya.api.mappers.ReportsMapper;
import co.com.crediya.api.rest.reports.ReportHandler;
import co.com.crediya.api.rest.reports.ReportRouterRest;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.usecase.ReportsUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ReportRouterRest.class, ReportHandler.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class RouterRestTest {

    private static final String REPORT_PATH = "/api/v1/reports";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ReportsUseCase reportsUseCase;

    @MockitoBean
    private ReportsMapper reportsMapper;

    private final ApprovedReport approvedReport = ApprovedReport.builder()
            .value(1L)
            .metric("approved_reports")
            .build();

    private final ApprovedReportResponseDTO approvedReportResponseDTO = new ApprovedReportResponseDTO("approved_reports", 1L);

    @Test
    void testListenGETOtherUseCase() {

        when( reportsUseCase.findByMetric( approvedReport.getMetric() ) ).thenReturn(Mono.just(approvedReport));
        when( reportsMapper.modelToResponse( approvedReport ) ).thenReturn(approvedReportResponseDTO);

        webTestClient.get()
                .uri( uriBuilder ->
                    uriBuilder.queryParam("type", approvedReport.getMetric() )
                            .path(REPORT_PATH).build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.OK.status())
                .jsonPath("$.data.metric").isEqualTo(  approvedReport.getMetric() )
                .jsonPath( "$.data.value" )
                .value( value ->
                    Assertions.assertThat( value ).isEqualTo( 1 )
                );
    }


}
