package se.lexicon.subscriptionapi.exception;

public class InactivePlanException extends RuntimeException {
    public InactivePlanException(String message) {
        super(message);
    }
}
