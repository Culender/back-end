package com.example.back_end.util.exception;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.example.back_end.util.response.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 전역 예외 처리를 위한 클래스
@ControllerAdvice
public class GlobalExceptionHandler {

    // NotFoundException 처리
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomApiResponse<?>> handleNotFoundException(NotFoundException ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // AlreadyExistsException 처리
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CustomApiResponse<?>> handleAlreadyExistsException(AlreadyExistsException ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 모든 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<?>> handleException(Exception ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
