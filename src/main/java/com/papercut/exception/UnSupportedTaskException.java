package com.papercut.exception;

public class UnSupportedTaskException extends Exception{
    public UnSupportedTaskException(String message) {
        super(message);
    }

    public UnSupportedTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
