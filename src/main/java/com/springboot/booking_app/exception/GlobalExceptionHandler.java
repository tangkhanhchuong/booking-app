package com.springboot.booking_app.exception;

import com.springboot.booking_app.exception.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";

    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_TO_HTTP_STATUS_CODE = Map.of(
        UserNotFoundException.class, HttpStatus.NOT_FOUND,
        BuildingNotFoundException.class, HttpStatus.NOT_FOUND,
        RoomNotFoundException.class, HttpStatus.NOT_FOUND,
        RoomExistedException.class, HttpStatus.CONFLICT,
        InvalidLoginException.class, HttpStatus.UNAUTHORIZED,
        UserExistedException.class, HttpStatus.CONFLICT,
        AuthorizationDeniedException.class, HttpStatus.UNAUTHORIZED
    );

    private static final Map<Class<? extends RuntimeException>, String> EXCEPTION_TO_ERROR_CODE = Map.of(
        UserNotFoundException.class, "USER_NOT_FOUND",
        BuildingNotFoundException.class, "BUILDING_NOT_FOUND",
        RoomNotFoundException.class, "ROOM_NOT_FOUND",
        RoomExistedException.class, "ROOM_EXISTED",
        InvalidLoginException.class, "INVALID_LOGIN",
        UserExistedException.class, "USER_EXISTED",
        AuthorizationDeniedException.class, "UNAUTHORIZED"
    );

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RuntimeException exception) {
        HttpStatus statusCode = EXCEPTION_TO_HTTP_STATUS_CODE.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        String errorMessage = EXCEPTION_TO_ERROR_CODE.getOrDefault(exception.getClass(), INTERNAL_SERVER_ERROR_CODE);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(statusCode.value())
                .message(errorMessage)
                .build();
        return ResponseEntity.status(statusCode.value()).body(errorResponse);
    }
}