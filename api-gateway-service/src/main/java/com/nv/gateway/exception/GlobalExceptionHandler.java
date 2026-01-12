package com.nv.gateway.exception;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;

@Component
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status = resolveStatus(ex);

        return writeError(
                exchange,
                status,
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error"
        );
    }

    /**
     * Centralized error writer (single place)
     */
    private Mono<Void> writeError(ServerWebExchange exchange,
                                  HttpStatus status,
                                  String message) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
              "timestamp": "%s",
              "status": %d,
              "error": "%s",
              "message": "%s",
              "path": "%s"
            }
            """.formatted(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value()
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer =
                exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    /**
     * Exception → HTTP status mapping
     */
    private HttpStatus resolveStatus(Throwable ex) {

        // JWT / authentication errors
        if (ex instanceof SecurityException) {
            return HttpStatus.UNAUTHORIZED;
        }

        // Invalid request / bad input
        if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        
        if (ex instanceof CustomAccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        
        if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        }

        if (ex instanceof ResponseStatusException rse) {
            return HttpStatus.valueOf(rse.getStatusCode().value());
        }
        
        // Anything else
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
