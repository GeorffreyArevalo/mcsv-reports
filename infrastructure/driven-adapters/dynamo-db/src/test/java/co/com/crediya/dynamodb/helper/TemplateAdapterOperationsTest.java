package co.com.crediya.dynamodb.helper;

import co.com.crediya.dynamodb.adapters.ApprovedReportRepositoryAdapter;
import co.com.crediya.dynamodb.entities.ApprovedReportEntity;
import co.com.crediya.model.ApprovedReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class TemplateAdapterOperationsTest {


    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ApprovedReportEntity> customerTable;

    private ApprovedReportEntity modelEntity;

    private ApprovedReport model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("approved_reports", TableSchema.fromBean(ApprovedReportEntity.class)))
                .thenReturn(customerTable);

        modelEntity = new ApprovedReportEntity();
        modelEntity.setMetric("id");
        modelEntity.setValue(1L);

        model = new ApprovedReport();
        model.setValue(1L);
        model.setMetric("id");
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        ApprovedReportEntity modelEntityUnderTest = new ApprovedReportEntity("id", 1L, "");

        assertNotNull(modelEntityUnderTest.getMetric());
        assertNotNull(modelEntityUnderTest.getValue());
    }

    @Test
    void testSave() {
        when(mapper.map(model, ApprovedReportEntity.class)).thenReturn(modelEntity);
        when(customerTable.putItem(modelEntity))
                .thenReturn(CompletableFuture.completedFuture(null));

        ApprovedReportRepositoryAdapter dynamoDBTemplateAdapter =
                new ApprovedReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper, "approved_reports");

        StepVerifier.create(dynamoDBTemplateAdapter.save(model))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String id = "id";

        when(customerTable.getItem(
                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, ApprovedReport.class)).thenReturn(model);


        ApprovedReportRepositoryAdapter dynamoDBTemplateAdapter =
                new ApprovedReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper, "approved_reports");

        StepVerifier.create(dynamoDBTemplateAdapter.getById("id"))
                .expectNext(model)  // aquí ya no null
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(model, ApprovedReportEntity.class)).thenReturn(modelEntity);
        when(mapper.map(modelEntity, ApprovedReport.class)).thenReturn(model);

        when(customerTable.deleteItem(any(ApprovedReportEntity.class)))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));

        ApprovedReportRepositoryAdapter dynamoDBTemplateAdapter =
                new ApprovedReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper, "approved_reports");

        StepVerifier.create(dynamoDBTemplateAdapter.delete(model))
                .expectNext(model)
                .verifyComplete();
    }


    @Test
    void testUpdateItem() {
        when(mapper.map(model, ApprovedReportEntity.class)).thenReturn(modelEntity);
        when(customerTable.updateItem(modelEntity))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, ApprovedReport.class)).thenReturn(model);

        ApprovedReportRepositoryAdapter dynamoDBTemplateAdapter =
                new ApprovedReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper, "approved_reports");

        StepVerifier.create(dynamoDBTemplateAdapter.updateItem(model))
                .expectNext(model)
                .verifyComplete();

        verify(customerTable).updateItem(modelEntity);
        verify(mapper).map(model, ApprovedReportEntity.class);
        verify(mapper).map(modelEntity, ApprovedReport.class);
    }




}
