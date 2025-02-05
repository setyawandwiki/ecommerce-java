package com.stwn.ecommerce_java.config.exception;

import com.stwn.ecommerce_java.common.errors.*;
import com.stwn.ecommerce_java.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GenericHandler {
    @ExceptionHandler(
            {
                    ResourceNotFoundException.class,
                    UserNotFoundException.class,
                    RoleNotFoundException.class
            })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException exception){
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestException(HttpServletRequest request, BadRequestException exception){
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(HttpServletRequest request, Exception exception){
        log.error("terjadi error. status code: " + HttpStatus.INTERNAL_SERVER_ERROR + " error message "
                + exception.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String errorMessage = objectError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnAuthenticationException(HttpServletRequest request, Exception exception){
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler({
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleConflictException(HttpServletRequest request, Exception exception){
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
