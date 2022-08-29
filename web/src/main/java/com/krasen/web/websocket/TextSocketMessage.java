package com.krasen.web.websocket;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextSocketMessage {

    private String key;
    private Object value;

}
