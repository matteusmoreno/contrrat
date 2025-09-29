package br.com.matteusmoreno.contrrat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<String> handleContractNotFoundException(ContractNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SlotsNotAvailableException.class)
    public ResponseEntity<String> handleSlotsNotAvailableException(SlotsNotAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MismatchedArtistsException.class)
    public ResponseEntity<String> handleMismatchedArtistsException(MismatchedArtistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContractStatusException.class)
    public ResponseEntity<String> handleInvalidContractStatusException(InvalidContractStatusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("You do not have permission to access this resource", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ArtistAlreadyPremiumException.class)
    public ResponseEntity<String> handleArtistAlreadyPremiumException(ArtistAlreadyPremiumException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
