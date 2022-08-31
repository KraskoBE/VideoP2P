package com.krasen.web.websocket;

import java.util.*;
import javax.servlet.http.*;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import static java.util.Objects.*;

public class SocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake( @NonNull ServerHttpRequest request,
                                    @NonNull ServerHttpResponse response,
                                    @NonNull WebSocketHandler wsHandler,
                                    @NonNull Map<String, Object> attributes ) throws Exception {
        HttpServletRequest servletRequest = ( ( ServletServerHttpRequest ) request ).getServletRequest();

        String roomIdString = null;
        String roomIdCookie = getRoomIdFromCookies( servletRequest );
        String roomIdQueryParam = getRoomIdFromQueryParams( servletRequest );

        if( nonNull( roomIdCookie ) ) {
            roomIdString = roomIdCookie;
        }

        if( nonNull( roomIdQueryParam ) ) {
            roomIdString = roomIdQueryParam;
        }

        if( isNull( roomIdString ) ) {
            return false;
        }

        try {
            attributes.put( "roomId", UUID.fromString( roomIdString ) );
            return true;
        } catch( IllegalArgumentException exception ) {
            response.setStatusCode( HttpStatus.BAD_REQUEST );
            return false;
        }
    }

    private String getRoomIdFromCookies( HttpServletRequest servletRequest ) {
        if( nonNull( servletRequest.getCookies() ) ) {
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
        if( parameters.containsKey( "roomId" ) ) {
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
