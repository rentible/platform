package org.wallride.exception;

public class GoogleMapException extends Exception {

    public GoogleMapException() {
        super();
    }

    public GoogleMapException(String message) {
        super(message);
    }

    public GoogleMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleMapException(Throwable cause) {
        super(cause);
    }

    protected GoogleMapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
