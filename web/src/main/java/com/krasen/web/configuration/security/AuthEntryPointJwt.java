package com.krasen.web.configuration.security;

import java.io.IOException;
import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger( AuthEntryPointJwt.class );

    @Override
    public void commence( HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException ) throws IOException {
        logger.error( "Unauthorized error: {}", authException.getMessage() );
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized" );
    }

}