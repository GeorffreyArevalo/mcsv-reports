package co.com.crediya.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Report {

    private String metric;
    private String type;
    private Long value;
    private String timestamp;

}
