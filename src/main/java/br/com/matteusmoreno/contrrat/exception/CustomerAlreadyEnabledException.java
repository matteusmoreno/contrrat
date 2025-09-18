package br.com.matteusmoreno.contrrat.exception;

public class CustomerAlreadyEnabledException extends RuntimeException {
    public CustomerAlreadyEnabledException(String message) {
        super(message);
    }
}
