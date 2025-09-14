package co.com.crediya.usecase;

import co.com.crediya.enums.CodesMetrics;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.Report;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import co.com.crediya.model.gateways.ReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ReportsUseCase {

    private final ApprovedReportRepositoryPort approvedReportRepository;
    private final ReportRepositoryPort reportRepository;

    public Mono<Void> incrementValuesReports(Long amount) {
        return Mono.zip(
                incrementValueToOne(), increaseAmount(amount)
        ).then(Mono.empty());
    }

    private Mono<Void> incrementValueToOne() {
        return approvedReportRepository.findByMetric(CodesMetrics.METRIC_INCREASE_REPORTS_APPROVED.getValue())
                .switchIfEmpty( Mono.just(ApprovedReport.builder().metric(CodesMetrics.METRIC_INCREASE_REPORTS_APPROVED.getValue()).value(0L).build()) )
                .log()
                .flatMap( approvedReport -> {
                    approvedReport.setValue(approvedReport.getValue() + 1);
                    approvedReport.setLastUpdated(LocalDateTime.now().toString());
                    return approvedReportRepository.update(approvedReport);
                });
    }

    private Mono<Void> increaseAmount(Long amount) {
        return approvedReportRepository.findByMetric(CodesMetrics.METRIC_INCREASE_AMOUNT_REPORTS_APPROVED.getValue())
                .switchIfEmpty( Mono.just(ApprovedReport.builder().metric(CodesMetrics.METRIC_INCREASE_AMOUNT_REPORTS_APPROVED.getValue()).value(0L).build()) )
                .log()
                .flatMap( approvedReport -> {
                    approvedReport.setValue(approvedReport.getValue() + amount);
                    approvedReport.setLastUpdated(LocalDateTime.now().toString());
                    return approvedReportRepository.update(approvedReport);
                });
    }

    public Mono<ApprovedReport> findByMetric(String metric) {
        return approvedReportRepository.findByMetric(metric)
                .switchIfEmpty( Mono.error( new CrediyaResourceNotFoundException(
                        String.format( ExceptionMessages.REPORT_WITH_METRIC_NOT_FOUND.getMessage(), metric )
                )))
                .flatMap( approvedReport -> {
                    Report report = Report.builder()
                        .metric(UUID.randomUUID().toString())
                        .type(approvedReport.getMetric())
                        .value(approvedReport.getValue())
                        .timestamp(LocalDateTime.now().toString())
                        .build();
                    return reportRepository.saveReport(report).thenReturn(approvedReport);
                });
    }

}
