package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    DO_NOT_ACCESS_RESOURCE("Doesn't have access to this resource."),
    UNAUTHORIZED_SENT_TOKEN_INVALID("Doesn't access - Unauthorized Sent Token."),
    REPORT_WITH_METRIC_NOT_FOUND("Report with %s not found."),;

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }



}
