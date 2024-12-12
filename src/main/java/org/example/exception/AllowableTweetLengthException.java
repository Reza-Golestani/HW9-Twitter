package org.example.exception;

public class AllowableTweetLengthException extends RuntimeException{
    public AllowableTweetLengthException(){};
    public AllowableTweetLengthException(String message){
        super(message);
    }
}
