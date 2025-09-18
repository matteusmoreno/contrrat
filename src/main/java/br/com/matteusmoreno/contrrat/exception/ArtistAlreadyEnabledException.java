package br.com.matteusmoreno.contrrat.exception;

public class ArtistAlreadyEnabledException extends RuntimeException {
    public ArtistAlreadyEnabledException(String message) {
        super(message);
    }
}
