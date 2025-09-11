package co.com.crediya.usecase;

import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportsUseCase {

    private final ApprovedReportRepositoryPort repository;

    public Mono<Void> incrementValueToOne(String metric) {
        return repository.incrementValueToOne(metric);
    }

}
