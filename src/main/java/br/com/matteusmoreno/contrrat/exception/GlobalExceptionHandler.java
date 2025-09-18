package br.com.matteusmoreno.contrrat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<String> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ArtistNotFoundException.class)
    public ResponseEntity<String> handleArtistNotFoundException(ArtistNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArtistAlreadyDisabledException.class)
    public ResponseEntity<String> handleArtistAlreadyDisabledException(ArtistAlreadyDisabledException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArtistAlreadyEnabledException.class)
    public ResponseEntity<String> handleArtistAlreadyEnabledException(ArtistAlreadyEnabledException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyDisabledException.class)
    public ResponseEntity<String> handleCustomerAlreadyDisabledException(CustomerAlreadyDisabledException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyEnabledException.class)
    public ResponseEntity<String> handleCustomerAlreadyEnabledException(CustomerAlreadyEnabledException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AvailabilityNotFoundException.class)
    public ResponseEntity<String> handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<String> handleInvalidTimeRangeException(InvalidTimeRangeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RedundantStatusChangeException.class)
    public ResponseEntity<String> handleRedundantStatusChangeException(RedundantStatusChangeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
