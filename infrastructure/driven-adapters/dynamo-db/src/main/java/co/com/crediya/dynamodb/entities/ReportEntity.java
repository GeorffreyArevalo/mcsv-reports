package co.com.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
@Builder(toBuilder = true)
public class ReportEntity {

    private String metric;
    private String type;
    private Long value;
    private String timestamp;
    private String consultedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("metric")
    public String getMetric() {
        return metric;
    }


    @DynamoDbSortKey
    @DynamoDbAttribute("type")
    public String getType() {
        return type;
    }

    @DynamoDbAttribute("value")
    public Long getValue() {
        return value;
    }

    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @DynamoDbAttribute("consulted_at")
    public String getConsultedAt() {
        return consultedAt;
    }



}
