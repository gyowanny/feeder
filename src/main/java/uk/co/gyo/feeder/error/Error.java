package uk.co.gyo.feeder.error;

/**
 * Created by gqu04 on 11/01/2017.
 */
public class Error {
    private final String message;
    private final String cause;

    public Error(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }
}
