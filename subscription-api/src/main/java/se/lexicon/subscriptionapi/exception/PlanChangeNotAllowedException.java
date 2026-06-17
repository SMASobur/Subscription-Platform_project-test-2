package se.lexicon.subscriptionapi.exception;

public class PlanChangeNotAllowedException extends RuntimeException {
    public PlanChangeNotAllowedException(String message) {
        super(message);
    }
}
