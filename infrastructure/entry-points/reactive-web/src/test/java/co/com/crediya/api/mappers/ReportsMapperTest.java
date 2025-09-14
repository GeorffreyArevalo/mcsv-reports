package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.reports.ApprovedReportResponseDTO;
import co.com.crediya.model.ApprovedReport;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReportsMapperTest {

    private final ReportsMapper reportsMapper = Mappers.getMapper(ReportsMapper.class);

    private final ApprovedReport report = ApprovedReport.builder()
            .metric("approved_loans")
            .value(100L)
            .lastUpdated("2025-09-14T12:00:00Z")
            .build();

    @Test
    void testModelToResponse() {
        Mono<ApprovedReportResponseDTO> result =
                Mono.fromCallable(() -> reportsMapper.modelToResponse(report));

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.metric().equals(report.getMetric()) &&
                                dto.value().equals(report.getValue())
                )
                .verifyComplete();
    }
}
