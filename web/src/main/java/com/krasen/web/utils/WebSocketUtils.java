package com.krasen.web.utils;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.krasen.web.websocket.TextSocketMessage;

public class WebSocketUtils {

    public static TextMessage buildTextMessage( TextSocketMessage textSocketMessage ) {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return new TextMessage( objectWriter.writeValueAsString( textSocketMessage ), true );
        } catch( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
    }

    public static TextSocketMessage parseTextMessage( TextMessage textMessage ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( textMessage.getPayload(), TextSocketMessage.class );
        } catch( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String getTextMessageKey( TextMessage textMessage ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree( textMessage.getPayload() );
            return root.path( "key" ).textValue();
        } catch( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String getTextMessageKeyValueByFieldName( TextMessage textMessage, String fieldName ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree( textMessage.getPayload() );
            return root.path( "value" ).path( fieldName ).textValue();
        } catch( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
    }

}