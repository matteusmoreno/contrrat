package br.com.matteusmoreno.contrrat.exception;

public class CustomerAlreadyDisabledException extends RuntimeException {
    public CustomerAlreadyDisabledException(String message) {
        super(message);
    }
}
