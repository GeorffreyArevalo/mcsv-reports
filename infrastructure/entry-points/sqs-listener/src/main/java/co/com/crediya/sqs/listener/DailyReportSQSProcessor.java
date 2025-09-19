package co.com.crediya.sqs.listener;

import co.com.crediya.usecase.ReportsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service("listenerSqsDailyReport")
@RequiredArgsConstructor
@Slf4j
public class DailyReportSQSProcessor implements Function<Message, Mono<Void>> {

    private final ReportsUseCase reportsUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received SQS daily report message: {}", message.body());
        return  reportsUseCase.getDailyReport();
    }
}
