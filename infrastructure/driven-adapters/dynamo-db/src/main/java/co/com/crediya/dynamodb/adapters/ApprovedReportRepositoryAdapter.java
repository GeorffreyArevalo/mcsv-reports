package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ApprovedReportEntity;
import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;



@Repository
@Slf4j
public class ApprovedReportRepositoryAdapter extends TemplateAdapterOperations<ApprovedReport, String, ApprovedReportEntity> implements ApprovedReportRepositoryPort {

    public ApprovedReportRepositoryAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, ApprovedReport.class ), "reports");
    }

    public Mono<Void> update( ApprovedReport approvedReport ) {
        log.info("Updating ApprovedReport {}", approvedReport);
        return super.updateItem(approvedReport).log().then(Mono.empty());
    }

    @Override
    public Mono<ApprovedReport> findByMetric(String metric) {
        return super.getById(metric);
    }

}
