package co.com.crediya.ports;

import reactor.core.publisher.Mono;

public interface SendEmailPort {

    Mono<Void> sendDailyReportEmail(Long countReports, Long amountReports);

}
