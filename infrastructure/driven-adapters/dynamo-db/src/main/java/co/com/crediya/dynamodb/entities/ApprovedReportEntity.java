package co.com.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
@Builder
public class ApprovedReportEntity {

    private String metric;
    private Long value;
    private LocalDateTime lastUpdated;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("metric")
    public String getMetric() {
        return metric;
    }

    @DynamoDbAttribute("value")
    public Long getValue() {
        return value;
    }

    @DynamoDbAttribute("last_updated")
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

}
