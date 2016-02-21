package com.maoshen.util;

public class IllegalStepException extends RuntimeException {
    public IllegalStepException() {
        super();
    }

    public IllegalStepException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStepException(String s) {
        super(s);
    }

    public IllegalStepException(Throwable cause) {
        super(cause);
    }
}
