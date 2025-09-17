package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ApprovedReportEntity;
import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;



@Repository
@Slf4j
public class ApprovedReportRepositoryAdapter extends TemplateAdapterOperations<ApprovedReport, String, ApprovedReportEntity> implements ApprovedReportRepositoryPort {

    public ApprovedReportRepositoryAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper,
                                           @Value("${aws.dynamodb.tables.approved-reports}") String nameTable
    ) {
        super(connectionFactory, mapper, d -> mapper.map(d, ApprovedReport.class ), nameTable);
        log.info("Creating ApprovedReportRepositoryAdapter - table: {}", nameTable);
    }

    public Mono<Void> update( ApprovedReport approvedReport ) {
        log.info("Updating ApprovedReport {}", approvedReport);
        return super.updateItem(approvedReport).log().then(Mono.empty());
    }

    @Override
    public Mono<ApprovedReport> findByMetric(String metric) {
        log.info("Finding ApprovedReport by metric {}", metric);
        return super.getById(metric).log();
    }

}
