package br.com.matteusmoreno.contrrat.exception;

public class ArtistAlreadyPremiumException extends RuntimeException {
    public ArtistAlreadyPremiumException(String message) {
        super(message);
    }
}
