package co.com.crediya.ses.adapters;

import co.com.crediya.ports.SendEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Component
@RequiredArgsConstructor
public class SendEmailAdapter implements SendEmailPort {

    private final SesAsyncClient sesClient;

    @Override
    public Mono<Void> sendDailyReportEmail(Long countReports, Long amountReports) {

        return Mono.fromFuture( () ->
            sesClient.sendEmail(
                SendEmailRequest
                    .builder()
                    .source("")
                    .destination( destinatationBuilder -> destinatationBuilder.toAddresses("").build() )
                    .message( messageBuilder ->
                        messageBuilder.subject( sunjectBuilder -> sunjectBuilder.data("").build() )
                            .body( bodyBuilder -> bodyBuilder.html(htmlBuilder -> htmlBuilder.data("").build()).build() )
                    ).build()
            )
        ).then();
    }
}
