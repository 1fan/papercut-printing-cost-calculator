package com.papercut.exception;

/**
 * This exception should be used when the printing job specified is not supported.
 */
public class UnSupportedTaskException extends Exception{
    public UnSupportedTaskException(String message) {
        super(message);
    }

    public UnSupportedTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
