package br.com.matteusmoreno.contrrat.exception;

public class RedundantStatusChangeException extends RuntimeException {
    public RedundantStatusChangeException(String message) {
        super(message);
    }
}
