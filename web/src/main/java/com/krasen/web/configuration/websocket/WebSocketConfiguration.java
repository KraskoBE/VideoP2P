package com.krasen.web.configuration.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.krasen.web.websocket.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers( WebSocketHandlerRegistry registry ) {
        registry.addHandler( new SocketHandler(), "/socket" ).setAllowedOrigins( "*" ).addInterceptors( new SocketHandshakeInterceptor() );
    }

}