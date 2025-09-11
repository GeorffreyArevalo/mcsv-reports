package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ApprovedReportEntity;
import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.ApprovedReport;
import co.com.crediya.model.gateways.ApprovedReportRepositoryPort;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<ApprovedReport, String, ApprovedReportEntity> implements ApprovedReportRepositoryPort {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, ApprovedReport.class ), "approved_reports");
    }

    public Mono<Void> incrementValueToOne( String metric ) {

        UpdateItemEnhancedRequest<ApprovedReportEntity> updateItemEnhancedRequest = UpdateItemEnhancedRequest.builder(ApprovedReportEntity.class)
                .item( new ApprovedReportEntity(){{ setMetric(metric); }} )
                .conditionExpression( Expression.builder()
                        .expression("ADD #v :inc")
                        .putExpressionName("#v", metric)
                        .putExpressionValue(":inc", AttributeValue.builder().n("1").build() )
                        .build()
                )
                .build();
        return super.updateItem(updateItemEnhancedRequest).then(Mono.empty());
    }

}
