package GGUM_Team3.Server.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    // 요청 본문 데이터 유효성 검사(@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 폼 데이터 및 URL 매개변수 유효성 검사(@ModelAttribute)
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(final BindException e) {
        log.warn(e.getMessage(), e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 잘못된 형식의 데이터 전송
    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageConversionException(final HttpMessageConversionException e) {
        log.warn("HttpMessageConversionException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(final ResponseStatusException e) {
        log.warn("ResponseStatusException", e);
        final ErrorResponse response = ErrorResponse.of(
                Integer.toString(e.getStatusCode().value()),
                e.getReason() != null ? e.getReason() : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        log.warn("RuntimeException", e);
        final ErrorResponse response = ErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage() != null && !e.getMessage().isEmpty()
                ? e.getMessage()
                : ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 서버 에러
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
