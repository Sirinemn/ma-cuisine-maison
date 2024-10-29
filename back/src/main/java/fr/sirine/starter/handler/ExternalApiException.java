package fr.sirine.starter.handler;

import org.springframework.http.HttpStatus;

public class ExternalApiException extends RuntimeException {
    private final HttpStatus status;

    public ExternalApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

