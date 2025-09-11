package co.com.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
@Builder
public class ApprovedReportEntity {

    private String metric;
    private Long value;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("metric")
    public String getMetric() {
        return metric;
    }

    @DynamoDbAttribute("value")
    public Long getValue() {
        return value;
    }

}
