package co.com.crediya.enums;

import lombok.Getter;

@Getter
public enum CodesMetrics {

    METRIC_INCREASE_REPORTS_APPROVED("count_approved_reports"),
    METRIC_INCREASE_AMOUNT_REPORTS_APPROVED("amount_approved_reports");

    private final String value;

    CodesMetrics(String value) {
        this.value = value;
    }
}
