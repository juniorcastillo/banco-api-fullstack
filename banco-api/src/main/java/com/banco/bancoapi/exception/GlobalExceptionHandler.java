package com.banco.bancoapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Recurso no existe");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(DatosInvalidosException ex) {
        logger.warn("Datos inválidos: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Valida los datos ingresados");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ErrorResponse> manejarOperacionNoPermitida(OperacionNoPermitidaException ex) {
        logger.warn("Operación no permitida: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), "Esta operación no es permitida");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldError() != null 
            ? ex.getBindingResult().getFieldError().getDefaultMessage() 
            : "Error de validación";
        logger.warn("Error de validación: {}", mensaje);
        ErrorResponse error = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validación fallida", mensaje);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> manejarRuntime(RuntimeException ex) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}