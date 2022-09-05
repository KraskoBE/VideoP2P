package com.krasen.web.dtos.face;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class FaceInformation {

    public Integer age;
    @JsonProperty( "dominant_emotion" )
    public String emotion;
    @JsonProperty( "dominant_race" )
    public String race;
    public String gender;
    public Region region;

}
