package com.otel.reservation.web;

import com.otel.reservation.exceptions.GuestNotFoundException;
import com.otel.reservation.exceptions.ReservationNotPossibleException;
import com.otel.reservation.exceptions.RoomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {
    public record ErrorResponse(String message, int code) {}

    @ExceptionHandler({
            GuestNotFoundException.class,
            RoomNotFoundException.class,
            ReservationNotPossibleException.class,
            IllegalArgumentException.class,
            HttpClientErrorException.class,
            ResponseStatusException.class,
            RuntimeException.class
    })
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        var statusCode = resolveStatusCode(exception);
        var message = exception.getMessage();
        return handleExceptionInternal(exception, message, new HttpHeaders(), statusCode, request);
    }

    private static HttpStatusCode resolveStatusCode(Exception exception) {
        return switch (exception) {
            case RoomNotFoundException ignore -> HttpStatus.NOT_FOUND;
            case GuestNotFoundException ignore -> HttpStatus.NOT_FOUND;
            case ReservationNotPossibleException ignore -> HttpStatus.BAD_REQUEST;
            case IllegalArgumentException ignore -> HttpStatus.BAD_REQUEST;
            case HttpClientErrorException e -> e.getStatusCode();
            case ResponseStatusException e -> e.getStatusCode();
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.atLevel(status.is4xxClientError() ? Level.WARN : Level.ERROR)
                .setCause(exception)
                .setMessage(exception.getClass().getName() + " is thrown with status " + status.value())
                .log();
        var error = new ErrorResponse(exception.getMessage(), status.value());
        return Objects.requireNonNullElse(
                super.handleExceptionInternal(exception, error, headers, status, request),
                new ResponseEntity<>(error, headers, status));
    }
}
