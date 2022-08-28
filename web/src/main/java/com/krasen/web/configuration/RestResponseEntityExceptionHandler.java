package com.krasen.web.configuration;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.krasen.web.exceptions.GenericException;

import static java.util.Objects.nonNull;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger( RestResponseEntityExceptionHandler.class );

    @ExceptionHandler( value = { RuntimeException.class } )
    protected ResponseEntity<Object> handleGenericException( RuntimeException ex, WebRequest request ) {
        logger.warn( "[{}]: {}", getPrincipalName( request ), ex.getMessage() );

        return handleExceptionInternal( ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
    }

    @ExceptionHandler( value = { GenericException.class } )
    protected ResponseEntity<Object> handleGenericException( GenericException ex, WebRequest request ) {
        logger.warn( "[{}]: {}", getPrincipalName( request ), ex.getMessage() );

        return handleExceptionInternal( ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
    }

    private String getPrincipalName( WebRequest request ) {
        return nonNull( request.getUserPrincipal() ) ? request.getUserPrincipal().getName() : "No user";
    }

}
