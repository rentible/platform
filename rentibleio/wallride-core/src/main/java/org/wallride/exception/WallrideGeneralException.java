package org.wallride.exception;

public class WallrideGeneralException extends RuntimeException {

    public WallrideGeneralException() {
        super();
    }

    public WallrideGeneralException(String message) {
        super(message);
    }

    public WallrideGeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public WallrideGeneralException(Throwable cause) {
        super(cause);
    }

    protected WallrideGeneralException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
