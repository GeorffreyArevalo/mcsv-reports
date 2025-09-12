package co.com.crediya.model.gateways;

import co.com.crediya.model.ApprovedReport;
import reactor.core.publisher.Mono;

public interface ApprovedReportRepositoryPort {

    Mono<Void> update(ApprovedReport approvedReport);

    Mono<ApprovedReport> findByMetric(String metric);


}
