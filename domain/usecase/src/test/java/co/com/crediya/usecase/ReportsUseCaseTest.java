package co.com.crediya.usecase;

import co.com.crediya.enums.CodesMetrics;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.Report;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import co.com.crediya.model.gateways.ReportRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportsUseCaseTest {

    @Mock
    private ApprovedReportRepositoryPort approvedReportRepository;

    @Mock
    private ReportRepositoryPort reportRepository;

    @InjectMocks
    private ReportsUseCase reportsUseCase;

    private ApprovedReport approvedReport;

    @BeforeEach
    void setUp() {
        approvedReport = ApprovedReport.builder()
                .metric(CodesMetrics.METRIC_INCREASE_REPORTS_APPROVED.getValue())
                .value(5L)
                .lastUpdated(LocalDateTime.now().toString())
                .build();
    }

    @Test
    @DisplayName("Must increment report values successfully when report exists")
    void testIncrementValuesReportsWithExisting() {
        ApprovedReport amountReport = ApprovedReport.builder()
                .metric(CodesMetrics.METRIC_INCREASE_AMOUNT_REPORTS_APPROVED.getValue())
                .value(10L)
                .build();

        when(approvedReportRepository.findByMetric(CodesMetrics.METRIC_INCREASE_REPORTS_APPROVED.getValue()))
                .thenReturn(Mono.just(approvedReport));
        when(approvedReportRepository.findByMetric(CodesMetrics.METRIC_INCREASE_AMOUNT_REPORTS_APPROVED.getValue()))
                .thenReturn(Mono.just(amountReport));
        when(approvedReportRepository.update(any())).thenReturn ( Mono.empty() );

        StepVerifier.create(reportsUseCase.incrementValuesReports(7L))
                .verifyComplete();

        StepVerifier.create(approvedReportRepository.update(approvedReport))
                .expectNext()
                .verifyComplete();

        StepVerifier.create(approvedReportRepository.update(amountReport))
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("Must create report and increment values when report does not exist")
    void testIncrementValuesReportsWithNoExisting() {
        when(approvedReportRepository.findByMetric(any())).thenReturn(Mono.empty());
        when(approvedReportRepository.update(any())).thenReturn( Mono.empty() );

        StepVerifier.create(reportsUseCase.incrementValuesReports(5L))
                .verifyComplete();

        StepVerifier.create(approvedReportRepository.update(
                        ApprovedReport.builder()
                                .metric(CodesMetrics.METRIC_INCREASE_REPORTS_APPROVED.getValue())
                                .value(1L)
                                .build()))
                .expectNext(  )
                .verifyComplete();

        StepVerifier.create(approvedReportRepository.update(
                        ApprovedReport.builder()
                                .metric(CodesMetrics.METRIC_INCREASE_AMOUNT_REPORTS_APPROVED.getValue())
                                .value(5L)
                                .build()))
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return ApprovedReport and save Report when metric exists")
    void testFindByMetricExisting() {
        when(approvedReportRepository.findByMetric("metric-test")).thenReturn(Mono.just(approvedReport));
        when(reportRepository.saveReport(any(Report.class))).thenReturn(Mono.empty());

        StepVerifier.create(reportsUseCase.findByMetric("metric-test"))
                .expectNextMatches(r -> r.getMetric().equals(approvedReport.getMetric())
                        && r.getValue().equals(approvedReport.getValue()))
                .verifyComplete();

        verify(reportRepository).saveReport(any(Report.class));
    }

    @Test
    @DisplayName("Must throw exception when metric not found")
    void testFindByMetricNotFound() {
        when(approvedReportRepository.findByMetric("not-found")).thenReturn(Mono.empty());

        StepVerifier.create(reportsUseCase.findByMetric("not-found"))
                .expectError(CrediyaResourceNotFoundException.class)
                .verify();
    }
}
