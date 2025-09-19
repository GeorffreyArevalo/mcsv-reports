package co.com.crediya.sqs.listener.config;

import co.com.crediya.sqs.listener.helper.SQSListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Configuration
public class SQSConfig {

    @Bean
    public SQSListener sqsListenerUpdateReport(
            SqsAsyncClient client,
           SQSProperties properties,
           @Qualifier("listenerSqsUpdateReport")
           Function<Message, Mono<Void>> fn
    ) {
        return SQSListener.builder()
                .client(client)
                .properties(properties)
                .queueUrl( properties.queueUrlUpdateReport() )
                .processor(fn)
                .build()
                .start();
    }

    @Bean
    public SQSListener sqsListenerDailyReport(
            SqsAsyncClient client,
            SQSProperties properties,
            @Qualifier("listenerSqsDailyReport")
            Function<Message, Mono<Void>> fn
    ) {
        return SQSListener.builder()
                .client(client)
                .properties(properties)
                .queueUrl( properties.queueUrlDailyReport() )
                .processor(fn)
                .build()
                .start();
    }

    @Bean
    public SqsAsyncClient configSqs(SQSProperties properties) {
        return SqsAsyncClient.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(getProviderChain())
                .build();
    }

    private AwsCredentialsProviderChain getProviderChain() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

}
