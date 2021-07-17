package com.papercut.exception;

/**
 * This exception should be thrown when the file provided is invalid.
 */
public class InvalidFileException extends Exception{
    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
