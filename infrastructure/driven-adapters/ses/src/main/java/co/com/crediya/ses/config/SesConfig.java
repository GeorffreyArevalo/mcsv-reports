package co.com.crediya.ses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SesConfig {

    @Value("${aws.ses.region}")
    private String region;

    @Bean
    public SesAsyncClient sesClient() {

        return SesAsyncClient.builder()
                .region(Region.of(region))
                .build();

    }


}
