package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ReportEntity;
import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.Report;
import co.com.crediya.model.gateways.ReportRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;


@Repository
@Slf4j
public class ReportRepositoryAdapter extends TemplateAdapterOperations<Report, String, ReportEntity> implements ReportRepositoryPort {

    public ReportRepositoryAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper,
                                   @Value("aws.dynamodb.region.tables.reports") String nameTable) {
        super(connectionFactory, mapper, d -> mapper.map(d, Report.class ), nameTable);
    }

    @Override
    public Mono<Void> saveReport(Report report) {
        log.info("Saving report {}", report);
        return super.save(report).log().then(Mono.empty());
    }
}
