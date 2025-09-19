package co.com.crediya.sqs.listener;

import co.com.crediya.usecase.ReportsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.model.Message;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SQSProcessorTest {

    private ReportsUseCase reportsUseCase;
    private UpdateReportSQSProcessor sqsProcessor;

    @BeforeEach
    void setUp() {
        reportsUseCase = Mockito.mock(ReportsUseCase.class);
        sqsProcessor = new UpdateReportSQSProcessor(reportsUseCase);
    }

    @Test
    void apply_shouldCallIncrementValuesReports() {
        String body = "\"123\"";
        Message message = Message.builder().body(body).build();

        when(reportsUseCase.incrementValuesReports(eq(123L)))
                .thenReturn(Mono.empty());

        Mono<Void> result = sqsProcessor.apply(message);

        StepVerifier.create(result)
                .verifyComplete();

        verify(reportsUseCase, times(1)).incrementValuesReports(123L);
    }


}
