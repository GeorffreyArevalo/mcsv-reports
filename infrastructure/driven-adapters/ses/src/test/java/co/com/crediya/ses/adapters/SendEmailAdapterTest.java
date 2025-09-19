package co.com.crediya.ses.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SendEmailAdapterTest {

    private SesAsyncClient sesClient;
    private SendEmailAdapter adapter;

    @BeforeEach
    void setUp() {
        sesClient = mock(SesAsyncClient.class);
        adapter = new SendEmailAdapter(sesClient);

        ReflectionTestUtils.setField(adapter, "sourceEmail", "source@test.com");
        ReflectionTestUtils.setField(adapter, "destinationEmail", "destination@test.com");
    }

    @Test
    void shouldSendEmailSuccessfully() {

        SendEmailResponse mockResponse = SendEmailResponse.builder().build();
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));


        StepVerifier.create(adapter.sendDailyReportEmail(5L, 1000L))
                .verifyComplete();


        ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(captor.capture());

        SendEmailRequest request = captor.getValue();
        assertThat(request.source()).isEqualTo("source@test.com");
        assertThat(request.destination().toAddresses()).contains("destination@test.com");
        assertThat(request.message().subject().data()).isEqualTo("Daily Report");
        assertThat(request.message().body().html().data())
                .isEqualTo("There are <strong>5</strong> request of loans and there is <strong>1000</strong> amount.");
    }
}
