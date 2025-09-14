package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ApprovedReportEntity;
import co.com.crediya.model.ApprovedReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApprovedReportRepositoryAdapterTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    private ApprovedReportRepositoryAdapter repositoryAdapter;

    private ApprovedReport model;
    private ApprovedReportEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        model = ApprovedReport.builder()
                .metric("id")
                .value(1L)
                .lastUpdated("2025-09-14")
                .build();


        entity = ApprovedReportEntity.builder()
                .metric("id")
                .value(1L)
                .lastUpdated("2025-09-14")
                .build();

        // Mapper simulado
        when(mapper.map(entity, ApprovedReport.class)).thenReturn(model);
        when(mapper.map(model, ApprovedReportEntity.class)).thenReturn(entity);

        repositoryAdapter = spy(new ApprovedReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper));
    }

    @Test
    void testUpdate() {
        doReturn(Mono.just(model)).when(repositoryAdapter).updateItem(any(ApprovedReport.class));

        StepVerifier.create(repositoryAdapter.update(model))
                .verifyComplete();

        verify(repositoryAdapter, times(1)).updateItem(model);
    }

    @Test
    void testFindByMetric() {
        doReturn(Mono.just(model)).when(repositoryAdapter).getById("id");

        StepVerifier.create(repositoryAdapter.findByMetric("id"))
                .expectNextMatches(report ->
                        report.getMetric().equals("id") && report.getValue().equals(1L))
                .verifyComplete();

        verify(repositoryAdapter, times(1)).getById("id");
    }

    @Test
    void testMapperConversion() {
        ApprovedReport mappedModel = mapper.map(entity, ApprovedReport.class);
        ApprovedReportEntity mappedEntity = mapper.map(model, ApprovedReportEntity.class);

        assert mappedModel != null;
        assert mappedEntity != null;
    }
}
