package com.krasen.web.enums;

public enum FaceRecognitionModel {
    VggFace( "VGG-Face" ), Facenet( "Facenet" ), Facenet512( "Facenet512" ), OpenFace( "OpenFace" ), DeepFace( "DeepFace" ),
    DeepId( "DeepID" ), ArcFace(
            "ArcFace" ), DLib( "Dlib" ), SFace( "SFace" );


    private final String name;

    FaceRecognitionModel( String name ) {
        this.name = name;
    }

    public boolean equalsName( String otherName ) {
        return name.equals( otherName );
    }

    public String toString() {
        return this.name;
    }
}
