package br.com.matteusmoreno.contrrat.exception;

public class ArtistAlreadyDisabledException extends RuntimeException {
    public ArtistAlreadyDisabledException(String message) {
        super(message);
    }
}
