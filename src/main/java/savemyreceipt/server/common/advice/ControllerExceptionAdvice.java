package savemyreceipt.server.common.advice;

import io.lettuce.core.RedisCommandExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RedisCommandExecutionException.class)
    protected ApiResponseDto<?> handleRedisCommandExecutionException(
        final RedisCommandExecutionException e) {
        return ApiResponseDto.error(ErrorStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(ApiResponseDto.error(e.getErrorStatus(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errMessage = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                .append(error.getField())
                .append("] ")
                .append(":")
                .append(error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseDto.error(400, errMessage.toString()));
    }
}
