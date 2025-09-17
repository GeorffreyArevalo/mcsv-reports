package co.com.crediya.dynamodb.adapters;

import co.com.crediya.dynamodb.entities.ReportEntity;
import co.com.crediya.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportRepositoryAdapterTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    private ReportRepositoryAdapter repositoryAdapter;

    private Report model;
    private ReportEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        model = Report.builder()
                .metric("123")
                .type("FINANCIAL")
                .timestamp("Quarterly report")
                .build();

        entity = ReportEntity.builder()
                .metric("123")
                .type("FINANCIAL")
                .timestamp("Quarterly report")
                .build();

        when(mapper.map(entity, Report.class)).thenReturn(model);
        when(mapper.map(model, ReportEntity.class)).thenReturn(entity);

        repositoryAdapter = spy(new ReportRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper, "reports"));
    }


    @Test
    void testReportEntitySettersAndGetters() {
        ReportEntity entity = new ReportEntity();
        entity.setMetric("Memory");
        entity.setType("MB");
        entity.setValue(2048L);
        entity.setTimestamp("2025-09-17T14:10:00Z");
        entity.setConsultedAt("2025-09-17T14:15:00Z");

        assertEquals("Memory", entity.getMetric());
        assertEquals("MB", entity.getType());
        assertEquals(2048L, entity.getValue());
        assertEquals("2025-09-17T14:10:00Z", entity.getTimestamp());
        assertEquals("2025-09-17T14:15:00Z", entity.getConsultedAt());
    }

    @Test
    void testSaveReport() {

        doReturn(Mono.just(model)).when(repositoryAdapter).save(any(Report.class));

        StepVerifier.create(repositoryAdapter.saveReport(model))
                .verifyComplete();

        verify(repositoryAdapter, times(1)).save(model);
    }

    @Test
    void testMapperConversion() {
        Report mappedModel = mapper.map(entity, Report.class);
        ReportEntity mappedEntity = mapper.map(model, ReportEntity.class);

        assert mappedModel != null;
        assert mappedEntity != null;
    }
}
