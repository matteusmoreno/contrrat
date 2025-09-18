package br.com.matteusmoreno.contrrat.exception;

public class SlotsNotAvailableException extends RuntimeException {
    public SlotsNotAvailableException(String message) {
        super(message);
    }
}
