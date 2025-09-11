package co.com.crediya.model.gateways;

import reactor.core.publisher.Mono;

public interface ApprovedReportRepositoryPort {

    Mono<Void> incrementValueToOne(String metric);

}
