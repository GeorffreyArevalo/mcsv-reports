package co.com.crediya.usecase;

import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ReportsUseCase {

    private final ApprovedReportRepositoryPort repository;

    public Mono<Void> incrementValueToOne(String metric) {
        return repository.findByMetric(metric)
                .switchIfEmpty( Mono.just(ApprovedReport.builder().metric(metric).value(0L).build()) )
                .log()
                .flatMap( approvedReport -> {
                    approvedReport.setValue(approvedReport.getValue() + 1);
                    approvedReport.setLastUpdated(LocalDateTime.now());
                    return repository.update(approvedReport);
                });
    }

    public Mono<ApprovedReport> findByMetric(String metric) {
        return repository.findByMetric(metric);
    }

}
