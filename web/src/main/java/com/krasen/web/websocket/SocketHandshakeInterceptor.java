package com.krasen.web.websocket;

import java.util.Map;

import lombok.NonNull;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class SocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake( @NonNull ServerHttpRequest request,
                                    @NonNull ServerHttpResponse response,
                                    @NonNull WebSocketHandler wsHandler,
                                    @NonNull Map<String, Object> attributes ) throws Exception {
        attributes.put( "test", "value" );
        // response.setStatusCode( HttpStatus.BAD_REQUEST );
        // response.getBody().write( "Test error".getBytes() );
        // response.getBody().flush();
        return true;
    }

    @Override
    public void afterHandshake( @NonNull ServerHttpRequest request,
                                @NonNull ServerHttpResponse response,
                                @NonNull WebSocketHandler wsHandler,
                                Exception exception ) {

    }

}
