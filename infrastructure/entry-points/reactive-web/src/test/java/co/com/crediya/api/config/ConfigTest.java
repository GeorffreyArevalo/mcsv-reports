package co.com.crediya.api.config;

import co.com.crediya.api.dtos.reports.ApprovedReportResponseDTO;
import co.com.crediya.api.mappers.ReportsMapper;
import co.com.crediya.api.rest.reports.ReportHandler;
import co.com.crediya.api.rest.reports.ReportRouterRest;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.usecase.ReportsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ReportRouterRest.class, ReportHandler.class, PathsConfig.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @MockitoBean
    private ReportsUseCase reportsUseCase;

    @MockitoBean
    private ReportsMapper reportsMapper;

    @Autowired
    private WebTestClient webTestClient;

    private final ApprovedReport approvedReport = ApprovedReport.builder()
            .value(1L)
            .metric("approved_reports")
            .build();

    private final ApprovedReportResponseDTO approvedReportResponseDTO = new ApprovedReportResponseDTO("approved_reports", 1L);


    @Test
    void corsConfigurationShouldAllowOrigins() {

        when( reportsUseCase.findByMetric( approvedReport.getMetric() ) ).thenReturn(Mono.just(approvedReport));
        when( reportsMapper.modelToResponse( approvedReport ) ).thenReturn(approvedReportResponseDTO);


        webTestClient.get()
                .uri( uriBuilder ->
                    uriBuilder.path("/api/v1/reports")
                            .queryParam("type", approvedReport.getMetric())
                            .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}