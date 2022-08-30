package com.krasen.web.configuration.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.krasen.web.websocket.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Autowired
    public WebSocketConfiguration( SocketHandler socketHandler ) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void registerWebSocketHandlers( WebSocketHandlerRegistry registry ) {
        registry.addHandler( socketHandler, "/socket" ).setAllowedOrigins( "*" ).addInterceptors( new SocketHandshakeInterceptor() );
    }

}