package com.krasen.web.configuration.websocket;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextSocketMessage {

    private String key;
    private Object value;

}
