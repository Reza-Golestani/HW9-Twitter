package org.example.exception;

public class DuplicateEmailUsernameException extends RuntimeException {
    public DuplicateEmailUsernameException(String message) {super(message);}
    public DuplicateEmailUsernameException(){};
}
