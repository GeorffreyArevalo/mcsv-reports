package co.com.crediya.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionStatusCode {

    BAD_REQUEST("400-BD"),
    NOT_FOUND("404-NF"),
    INTERNAL_SERVER_ERROR("500-ISE"),
    FORBIDDEN("403-FB"),
    UNAUTHORIZED_INVALID_TOKEN("401-UA-IT"),
    OK("200-OK");

    private final String statusCode;

    ExceptionStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode;
    }

}
