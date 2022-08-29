package com.krasen.web.utils;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.krasen.web.websocket.TextSocketMessage;

public class WebSocketUtils {

    public static TextMessage buildTextMessage( TextSocketMessage textSocketMessage ) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return new TextMessage( objectWriter.writeValueAsString( textSocketMessage ), true );
    }

    public static TextSocketMessage parseTextMessage( TextMessage textMessage ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue( textMessage.getPayload(), TextSocketMessage.class );
    }

    public static String getTextMessageKey( TextMessage textMessage ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree( textMessage.getPayload() );
        return root.path( "key" ).textValue();
    }

    public static String getTextMessageKeyValueByFieldName( TextMessage textMessage, String fieldName ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree( textMessage.getPayload() );
        return root.path( "value" ).path( fieldName ).textValue();
    }

}