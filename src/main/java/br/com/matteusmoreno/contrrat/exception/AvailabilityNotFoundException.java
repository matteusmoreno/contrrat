package br.com.matteusmoreno.contrrat.exception;

public class AvailabilityNotFoundException extends RuntimeException {
    public AvailabilityNotFoundException(String message) {
        super(message);
    }
}
