package com.abn.recipe.api.exception;

import com.abn.recipe.domain.exception.DuplicateRecipeException;
import com.abn.recipe.domain.exception.RecipeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        log.error(ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ErrorCode.RECIPE_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateRecipeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRequestException(DuplicateRecipeException ex) {
        log.error(ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_RECIPE_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error(ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus httpStatus, ErrorCode errorCode, Object error) {
        return ResponseEntity.status(httpStatus).body(ErrorResponse.builder()
                .errorCode(errorCode)
                .errorDetails(error)
                .build());
    }
}
