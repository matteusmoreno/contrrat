package br.com.matteusmoreno.contrrat.exception;

public class InvalidContractStatusException extends RuntimeException {
    public InvalidContractStatusException(String message) {
        super(message);
    }
}
