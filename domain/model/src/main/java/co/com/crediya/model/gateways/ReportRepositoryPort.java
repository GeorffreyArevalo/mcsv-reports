package co.com.crediya.model.gateways;

import co.com.crediya.model.Report;
import reactor.core.publisher.Mono;

public interface ReportRepositoryPort {

    Mono<Void> saveReport(Report report);

}
