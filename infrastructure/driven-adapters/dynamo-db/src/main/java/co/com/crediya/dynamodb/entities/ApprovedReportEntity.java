package co.com.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ApprovedReportEntity {

    private String metric;
    private String value;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("metric")
    public String getMetric() {
        return metric;
    }

    @DynamoDbAttribute("value")
    public String getValue() {
        return value;
    }

}
