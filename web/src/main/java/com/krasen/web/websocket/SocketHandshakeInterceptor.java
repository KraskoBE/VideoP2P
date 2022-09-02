package com.krasen.web.websocket;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SocketHandshakeInterceptor implements HandshakeInterceptor {

    public static final Logger logger = LoggerFactory.getLogger( SocketHandshakeInterceptor.class );

    @Override
    public boolean beforeHandshake( @NonNull ServerHttpRequest request,
                                    @NonNull ServerHttpResponse response,
                                    @NonNull WebSocketHandler wsHandler,
                                    @NonNull Map<String, Object> attributes ) throws Exception {
        logger.info( request.toString() );

        HttpServletRequest servletRequest = ( (ServletServerHttpRequest) request ).getServletRequest();

        String roomIdString = null;
        String roomIdCookie = getRoomIdFromCookies( servletRequest );
        String roomIdQueryParam = getRoomIdFromQueryParams( servletRequest );

        if ( nonNull( roomIdCookie ) ) {
            roomIdString = roomIdCookie;
        }

        if ( nonNull( roomIdQueryParam ) ) {
            roomIdString = roomIdQueryParam;
        }

        if ( isNull( roomIdString ) ) {
            return false;
        }

        try {
            attributes.put( "roomId", UUID.fromString( roomIdString ) );
            return true;
        } catch ( IllegalArgumentException exception ) {
            response.setStatusCode( HttpStatus.BAD_REQUEST );
            return false;
        }
    }

    private String getRoomIdFromCookies( HttpServletRequest servletRequest ) {
        if ( nonNull( servletRequest.getCookies() ) ) {
            return Arrays.stream( servletRequest.getCookies() )
                         .filter( cookie -> cookie.getName().equals( "roomId" ) )
                         .findFirst()
                         .map( Cookie::getValue )
                         .orElse( "" );
        }
        return null;
    }

    private String getRoomIdFromQueryParams( HttpServletRequest servletRequest ) {
        Map<String, String[]> parameters = servletRequest.getParameterMap();
        if ( parameters.containsKey( "roomId" ) ) {
            String[] authTokenParamList = parameters.get( "roomId" );
            return authTokenParamList[0];
        }
        return null;
    }

    @Override
    public void afterHandshake( @NonNull ServerHttpRequest request,
                                @NonNull ServerHttpResponse response,
                                @NonNull WebSocketHandler wsHandler,
                                Exception exception ) {

    }

}
