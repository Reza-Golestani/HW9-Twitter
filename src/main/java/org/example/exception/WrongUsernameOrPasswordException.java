package org.example.exception;

public class WrongUsernameOrPasswordException extends RuntimeException{
    public WrongUsernameOrPasswordException(){}
    public WrongUsernameOrPasswordException(String message){
        super(message);
    }
}
