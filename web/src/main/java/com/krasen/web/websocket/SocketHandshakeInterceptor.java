package com.krasen.web.websocket;

import java.util.*;
import javax.servlet.http.Cookie;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

public class SocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake( @NonNull ServerHttpRequest request,
                                    @NonNull ServerHttpResponse response,
                                    @NonNull WebSocketHandler wsHandler,
                                    @NonNull Map<String, Object> attributes ) throws Exception {
        ServletServerHttpRequest servletServerHttpRequest = ( ServletServerHttpRequest ) request;
        Cookie[] cookies = servletServerHttpRequest.getServletRequest().getCookies();

        Cookie roomIdCookie = stream( cookies ).filter( c -> c.getName().equals( "roomId" ) )
                                               .findFirst()
                                               .orElse( null );

        if( isNull( roomIdCookie ) ) {
            response.setStatusCode( HttpStatus.BAD_REQUEST );
            return false;
        }

        try {
            attributes.put( "roomId", UUID.fromString( roomIdCookie.getValue() ) );
        } catch( IllegalArgumentException exception ) {
            response.setStatusCode( HttpStatus.BAD_REQUEST );
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake( @NonNull ServerHttpRequest request,
                                @NonNull ServerHttpResponse response,
                                @NonNull WebSocketHandler wsHandler,
                                Exception exception ) {

    }

}
