package com.krasen.web.configuration;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( value = { ResponseStatusException.class } )
    protected ResponseEntity<Object> handleConflict( ResponseStatusException ex, WebRequest request ) {
        return handleExceptionInternal( ex, ex.getReason(), new HttpHeaders(), ex.getStatus(), request );
    }

}
