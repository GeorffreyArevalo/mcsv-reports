package co.com.crediya.ses.adapters;

import co.com.crediya.ports.SendEmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendEmailAdapter implements SendEmailPort {

    private final SesAsyncClient sesClient;

    @Value("${report.email.source}")
    private String sourceEmail;

    @Value("${report.email.destination}")
    private String destinationEmail;

    private static final String EMAIL_SUBJECT = "Daily Report";
    private static final String EMAIL_BODY = "There are <strong>%s</strong> request of loans and there is <strong>%s</strong> amount.";

    @Override
    public Mono<Void> sendDailyReportEmail(Long countReports, Long amountReports) {

        return Mono.fromFuture( () ->
            sesClient.sendEmail(
                SendEmailRequest
                    .builder()
                    .source(sourceEmail)
                    .destination( destinatationBuilder -> destinatationBuilder.toAddresses(destinationEmail).build() )
                    .message( messageBuilder ->
                        messageBuilder.subject( sunjectBuilder -> sunjectBuilder.data(EMAIL_SUBJECT).build() )
                            .body( bodyBuilder -> bodyBuilder.html(htmlBuilder -> htmlBuilder.data(
                                String.format(EMAIL_BODY, countReports, amountReports)
                            ).build()).build() )
                    ).build()
            )
        )
        .doOnError( error -> log.error(error.getMessage()))
        .then();
    }
}
