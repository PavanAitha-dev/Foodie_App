package com.improveid.User.exception;

import com.improveid.User.entity.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEntity> handleGeneric(Exception ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError("Internal Server Error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEntity> handleValidation(MethodArgumentNotValidException ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Validation Failed");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorEntity> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("IllegalArgument");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorEntity> handleNotFoundException(NotFoundException ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Not Found");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorEntity> handleBadRequestException(BadRequestException ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Bad Request");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidtDataException.class)
    public ResponseEntity<ErrorEntity> handleInvalidDataException(InvalidtDataException ex) {
        ErrorEntity error=new ErrorEntity();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Invalid Data Error");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(TransactionSystemException.class)
//    public ResponseEntity<ErrorEntity> handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
//        String message = "User Details already Exist";
//
////        Throwable rootCause = ex.getRootCause();
////        if (rootCause != null && rootCause.getMessage() != null) {
////            message = rootCause.getMessage();
////        }
//
//        ErrorEntity error = new ErrorEntity();
//        error.setStatus(HttpStatus.BAD_REQUEST.value());
//        error.setError("Transaction Validation Error");
//        error.setMessage(message);
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

}
