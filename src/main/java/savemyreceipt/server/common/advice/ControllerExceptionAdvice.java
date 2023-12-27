package savemyreceipt.server.common.advice;

import io.lettuce.core.RedisCommandExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.exception.ErrorStatus;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RedisCommandExecutionException.class)
    protected ApiResponseDto<?> handleRedisCommandExecutionException(
        final RedisCommandExecutionException e) {
        return ApiResponseDto.error(ErrorStatus.BAD_REQUEST);
    }
}
