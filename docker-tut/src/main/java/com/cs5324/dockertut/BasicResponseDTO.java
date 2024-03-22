package com.cs5324.dockertut;

public class BasicResponseDTO {
    private final Object message;

    public BasicResponseDTO(String msg){
        this.message = msg;
    }

    public Object getMessage() {
        return message;
    }
}
